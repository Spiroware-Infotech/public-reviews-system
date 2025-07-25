package com.prs.api.service.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prs.api.dto.BlogRequestDto;
import com.prs.api.entity.Blog;
import com.prs.api.entity.Tag;
import com.prs.api.entity.User;
import com.prs.api.mapper.BlogsMapper;
import com.prs.api.repository.BlogRepository;
import com.prs.api.repository.TagRepository;
import com.prs.api.repository.UserRepository;
import com.prs.api.response.dto.BlogResponseDto;
import com.prs.api.service.BlogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {

	private final BlogRepository blogRepository;
	private final UserRepository userRepo;
	private final TagRepository tagRepo;
	private final BlogsMapper blogsMapper;

	@Override
	public BlogResponseDto createBlog(BlogRequestDto dto) {

		Blog blog = Blog.builder().title(dto.getTitle()).slug(dto.getSlug()).content(dto.getContent())
				.excerpt(dto.getExcerpt()).featuredImageUrl(dto.getFeaturedImageUrl()).status(dto.getStatus())
				.publishedAt(LocalDateTime.now()).metaTitle(dto.getMetaTitle())
				.metaDescription(dto.getMetaDescription()).build();

		User author = userRepo.findById(dto.getAuthorId()).orElseThrow(() -> new RuntimeException("Author not found"));
		blog.setAuthor(author);

		Set<Tag> tags = new HashSet<>();
		if (dto.getTagIds() != null) {
			for (Long tagId : dto.getTagIds()) {
				Tag tag = tagRepo.findById(tagId)
						.orElseThrow(() -> new RuntimeException("Tag not found with ID: " + tagId));
				tags.add(tag);
			}
		}
		blog.setTags(tags);

		Blog savedBlog = blogRepository.save(blog);

		BlogResponseDto responseDto = blogsMapper.toDto(savedBlog);

		return responseDto;
	}

	public BlogResponseDto getBlogById(long id) {
		Optional<Blog> blogresponse = blogRepository.findById(id);

		log.info("retrived blog response");
		if (blogresponse.isEmpty()) {
			throw new RuntimeException("Blog not found with this id " + id);

		}

		BlogResponseDto response = blogsMapper.toDto(blogresponse.get());

		return response;
	}

	public List<BlogResponseDto> getAllBlogs() {
		List<Blog> blogList = blogRepository.findAll();

		if (blogList.isEmpty()) {
			throw new RuntimeException("No blogs are present");
		}

		List<BlogResponseDto> responseList = blogList.stream().map(blogsMapper::toDto).collect(Collectors.toList());
		return responseList;
	}

}
