package com.prs.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prs.api.dto.ReviewRequest;
import com.prs.api.entity.Organization;
import com.prs.api.entity.PublicUser;
import com.prs.api.entity.Reviews;
import com.prs.api.entity.TagLines;
import com.prs.api.entity.UploadFiles;
import com.prs.api.enums.Sentiments;
import com.prs.api.exceptions.ReviewEngagementException;
import com.prs.api.exceptions.ReviewOwnershipException;
import com.prs.api.mapper.ReviewsMapper;
import com.prs.api.org.repository.OrganizationRepository;
import com.prs.api.repository.PublicUserRepository;
import com.prs.api.repository.ReviewRepository;
import com.prs.api.response.ReviewResponse;
import com.prs.api.service.ReviewService;
import com.prs.api.utils.CommentFilter;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

	private final ReviewRepository reviewRepository;
	private final PublicUserRepository publicUserRepository;
	private final OrganizationRepository organizationRepository;
	private final ReviewsMapper reviewsMapper;

	@Override
	public void submitReview(ReviewRequest request, String username) throws Exception {
		// 1. Fetch user and organization
		PublicUser user = publicUserRepository.findById(request.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		Organization org = organizationRepository.findById(request.getOrganizationId())
				.orElseThrow(() -> new RuntimeException("Organization not found"));

		// 2. Build the Reviews object
		Reviews review = new Reviews();
		review.setSubject(request.getSubject());
		review.setComment(request.getComment());
		review.setRating(request.getRating());
		review.setLikes(0);
		review.setDislikes(0);
		review.setSubmittedAt(LocalDateTime.now());
		review.setPublicUser(user);
		review.setOrganization(org);

		Sentiments sentiment = analyzeReviewComment(request.getComment());
		review.setSentiment(sentiment);

		if (CommentFilter.containsBadWords(request.getComment())
				|| CommentFilter.containsNegativeKeyword(request.getComment())) {
			List<String> badWords = CommentFilter.getMatchedBadWords(request.getComment());
			log.info("⚠️ Bad words detected in comment: " + badWords);
			review.setBadFlag("Y");
		} else {
			review.setBadFlag("N");
		}

		// 3. Add KeyLine if any
		if (request.getTagLines() != null && !request.getTagLines().isEmpty()) {
			List<TagLines> lines = new ArrayList<>();
			for (String lineText : request.getTagLines()) {
				TagLines line = new TagLines();
				line.setTags(lineText);
				line.setReview(review);
				lines.add(line);
			}
			review.setTagLines(lines);
		}

		// 4. Handle image uploads
		if (request.getImagePaths() != null && !request.getImagePaths().isEmpty()) {
			List<UploadFiles> files = new ArrayList<>();
			for (String imagePath : request.getImagePaths()) {
				UploadFiles file = new UploadFiles();
				file.setFileUrl(imagePath);
				file.setReview(review);
				files.add(file);
			}
			review.setReviewImages(files);
		}

		// 5. Save the review (cascades children)
		reviewRepository.save(review);
	}

	@Override
	@Transactional
	public boolean deleteReview(Long userId, Long reviewId)  throws Exception {
		Optional<Reviews> optionalReview = reviewRepository.findById(reviewId);

		PublicUser user = publicUserRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("User not found"));

		if (optionalReview.isEmpty()) {
			log.warn("Review with ID {} not found", reviewId);
			return false;
		}
			
		Reviews review = optionalReview.get();
		
		if (!user.getId().equals(review.getPublicUser().getId())) {
		    log.warn("User {} attempted to delete a review (ID {}) they do not own",
		             user.getUser().getUsername(), reviewId);
		    throw new ReviewOwnershipException("You are not authorized to delete this review.");
		}

		if (review.getLikes() != 0 || review.getDislikes() != 0) {
		    log.warn("User {} attempted to delete review (ID {}) which has likes/dislikes",
		             user.getUser().getUsername(), reviewId);
		    throw new ReviewEngagementException("User : "+user.getUser().getUsername()+" , attempted to delete review (ID : "+reviewId+") which has likes/dislikes");
		}
		
		int deletedCount = reviewRepository.deleteReviewById(reviewId);
	    if (deletedCount == 0) {
	        throw new EntityNotFoundException("No review found with id: " + reviewId);
	    }
		log.info("Review with ID {} deleted by user {}", reviewId, user.getUser().getUsername());
		return true;
		
	}

	@Override
	public List<ReviewResponse> getAllReviews() {
		List<Reviews> reviews = reviewRepository.findAll();
		List<ReviewResponse> resp = reviews.stream().map(review -> {
			ReviewResponse dto = reviewsMapper.toDto(review); // Use MapStruct or custom mapper
			if (review.getPublicUser() != null) {
				dto.setReviewerName(review.getPublicUser().getFirstname());
			} else {
				dto.setReviewerName("Anonymous");
			}
			return dto;
		}).collect(Collectors.toList());

		return resp;
	}

	@Override
	public List<ReviewResponse> getReviewsByOrganizationId(Long orgId) {
		//Organization organization = organizationRepository.findById(orgId)
		//		.orElseThrow(() -> new EntityNotFoundException("Organization not found"));

		List<Reviews> reviews = reviewRepository.findByOrganizationId(orgId);
		List<ReviewResponse> responseList = reviews.stream().map(review -> {
			ReviewResponse dto = reviewsMapper.toDto(review); // Use MapStruct or custom mapper
			List<String> tagLines = review.getTagLines().stream().map(TagLines::getTags).collect(Collectors.toList());

			dto.setTagLines(tagLines);

			if (review.getPublicUser() != null) {
				dto.setReviewerName(review.getPublicUser().getFirstname());
			} else {
				dto.setReviewerName("Anonymous");
			}
			return dto;
		}).collect(Collectors.toList());

		return responseList;
	}

	@Override
	public ReviewResponse getReviewById(Long id) {
		Reviews review = reviewRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Review not found with ID: " + id));
		ReviewResponse resp = reviewsMapper.toDto(review);
		resp.setReviewerName(review.getPublicUser().getFirstname());
		return resp;
	}

	public Sentiments analyzeReviewComment(String comment) {
		// Example using keyword logic
		String lowerText = comment.toLowerCase();

		if (CommentFilter.containsNegativeKeyword(lowerText)) {
			return Sentiments.NEGATIVE;
		} else if (CommentFilter.containsBadWords(lowerText)) {
			return Sentiments.BAD;
		} else {
			return Sentiments.POSITIVE;
		}
	}

	@Override
	public List<ReviewResponse> getReviewsByUserId(Long userId) {
		List<Reviews> reviews = reviewRepository.findByPublicUserId(userId);
		List<ReviewResponse> responseList = reviews.stream().map(review -> {
			ReviewResponse dto = reviewsMapper.toDto(review); // Use MapStruct or custom mapper
			List<String> tagLines = review.getTagLines().stream().map(TagLines::getTags).collect(Collectors.toList());

			dto.setTagLines(tagLines);

			if (review.getPublicUser() != null) {
				dto.setReviewerName(review.getPublicUser().getFirstname());
			} else {
				dto.setReviewerName("Anonymous");
			}
			return dto;
		}).collect(Collectors.toList());

		return responseList;
	}

}
