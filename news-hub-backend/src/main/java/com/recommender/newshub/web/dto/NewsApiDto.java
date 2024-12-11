package com.recommender.newshub.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class NewsApiDto {

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchNewsResultDto {
        private int number;
        private int available;
        @JsonProperty("news")
        private List<NewsDetailDto> newsDetailDtos;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TopNewsResultDto {
        @JsonProperty("top_news")
        private List<TopNewsDtos> topNewsDtos;

        @Getter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TopNewsDtos {
            @JsonProperty("news")
            private List<NewsDetailDto> newsDetailDtos;
        }
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NewsDetailDto {
        private Long id;
        private String title;
        private String summary;
        private String url;
        private String image;
        @JsonProperty("publish_date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime publishDate;
        private String author;
        @JsonProperty("catgory")
        private String category;
    }
}
