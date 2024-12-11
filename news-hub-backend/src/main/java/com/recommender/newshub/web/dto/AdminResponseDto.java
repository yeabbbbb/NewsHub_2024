package com.recommender.newshub.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AdminResponseDto {

    @Getter
    @AllArgsConstructor
    public static class AddNewsResultDto {
        private Integer available;
        private Integer savedNumber;
    }

}
