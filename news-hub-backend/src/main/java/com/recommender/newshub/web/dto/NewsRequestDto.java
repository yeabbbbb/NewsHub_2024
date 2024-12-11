package com.recommender.newshub.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class NewsRequestDto {

    @Getter
    public static class LogClickDataDto {
        @NotNull
        private Long newsId;
    }
}
