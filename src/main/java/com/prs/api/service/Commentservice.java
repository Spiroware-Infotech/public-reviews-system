package com.prs.api.service;

import com.prs.api.dto.CommentRequestDto;
import com.prs.api.response.dto.CommentResponse;

public interface Commentservice {

	CommentResponse addComment(CommentRequestDto requestdto);

}
