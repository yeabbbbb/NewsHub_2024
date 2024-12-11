package com.recommender.newshub.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class NewsResponseDto {

    @Getter
    @AllArgsConstructor
    public static class GetFixedNewsResultDto {
        private Integer number;
        private List<NewsDto> news;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class GetNewsResultDto {
        private Boolean isFirst;
        private Boolean isLast;
        private Integer number;
        private Integer totalPages;
        private Long totalElements;
        private List<NewsDto> news;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class NewsDto {
        private Long id;
        private LocalDateTime publishDate;
        private String author;
        private String imageUrl;
        private String summary;
        private String title;
        private String url;
        private String category;
    }
}
