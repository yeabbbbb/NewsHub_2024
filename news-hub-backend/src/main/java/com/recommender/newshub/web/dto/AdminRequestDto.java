package com.recommender.newshub.web.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AdminRequestDto {
    @Getter
    public static class FetchGeneralNewsDto {
        @NotNull
        private LocalDateTime startDateTime;

        @NotNull
        private LocalDateTime endDateTime;
    }

    @Getter
    public static class FetchTopNewsDto {
        @NotNull
        private LocalDate date;
    }
}
