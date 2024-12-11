package com.recommender.newshub.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

public class RecommenderDto {

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecommendedNewsDto {
        @JsonProperty("recommended_news_ids")
        private List<Long> recommendedNewsIds;
    }
}
