package com.prs.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prs.api.dto.CommentRequestDto;
import com.prs.api.entity.Blog;
import com.prs.api.entity.Comment;
import com.prs.api.entity.User;
import com.prs.api.repository.BlogRepository;
import com.prs.api.repository.CommentRepository;
import com.prs.api.repository.UserRepository;
import com.prs.api.response.dto.CommentResponse;
import com.prs.api.service.Commentservice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements Commentservice {

	private final CommentRepository commentRepo;
	private final BlogRepository blogRepo;
	private final UserRepository userRepo;

	@Override
	public CommentResponse addComment(CommentRequestDto requestdto) {
       log.info("CommentServiceImpl --> addComment ");
		Blog blog = blogRepo.findById(requestdto.getBlogId())
				.orElseThrow(() -> new RuntimeException("Blogs not found"));
		User user = userRepo.findById(requestdto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

		Comment comment = new Comment();

		comment.setContent(requestdto.getContent());

		comment.setAuthor(user);
		comment.setBlog(blog);
		Comment savedComment = commentRepo.save(comment);
		CommentResponse response = new CommentResponse();

		return response;
	}

}
