package com.prs.api.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.prs.api.entity.Reviews;
import com.prs.api.entity.TagLines;
import com.prs.api.response.ReviewResponse;

@Mapper(componentModel = "spring", imports = MapperUtils.class)
public interface ReviewsMapper {

	@Mapping(source = "tagLines", target = "tagLines")
	@Mapping(target = "reviewerName", expression = "java(MapperUtils.mapReviewerName(review.getPublicUser()))")
    ReviewResponse toDto(Reviews review);

    default List<String> mapTagLinesToStrings(List<TagLines> tagLines) {
        if (tagLines == null) return new ArrayList<>();
        return tagLines.stream()
                       .map(TagLines::getTags)
                       .collect(Collectors.toList());
    }

}
