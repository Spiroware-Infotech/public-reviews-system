package com.prs.api.mapper;

import com.prs.api.entity.Blog;
import com.prs.api.entity.Comment;
import com.prs.api.entity.Tag;
import com.prs.api.response.dto.BlogResponseDto;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-22T22:26:39+0800",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.37.0.v20240215-1558, environment: Java 21.0.2 (Eclipse Adoptium)"
)
@Component
public class BlogsMapperImpl implements BlogsMapper {

    @Override
    public BlogResponseDto toDto(Blog blog) {
        if ( blog == null ) {
            return null;
        }

        BlogResponseDto blogResponseDto = new BlogResponseDto();

        blogResponseDto.setAuthor( blog.getAuthor() );
        Set<Comment> set = blog.getComments();
        if ( set != null ) {
            blogResponseDto.setComments( new LinkedHashSet<Comment>( set ) );
        }
        blogResponseDto.setContent( blog.getContent() );
        blogResponseDto.setCreatedAt( blog.getCreatedAt() );
        blogResponseDto.setExcerpt( blog.getExcerpt() );
        blogResponseDto.setFeaturedImageUrl( blog.getFeaturedImageUrl() );
        blogResponseDto.setId( blog.getId() );
        blogResponseDto.setMetaDescription( blog.getMetaDescription() );
        blogResponseDto.setMetaTitle( blog.getMetaTitle() );
        blogResponseDto.setPublishedAt( blog.getPublishedAt() );
        blogResponseDto.setSlug( blog.getSlug() );
        blogResponseDto.setStatus( blog.getStatus() );
        Set<Tag> set1 = blog.getTags();
        if ( set1 != null ) {
            blogResponseDto.setTags( new LinkedHashSet<Tag>( set1 ) );
        }
        blogResponseDto.setTitle( blog.getTitle() );
        blogResponseDto.setUpdatedAt( blog.getUpdatedAt() );
        blogResponseDto.setVersion( blog.getVersion() );

        return blogResponseDto;
    }
}
