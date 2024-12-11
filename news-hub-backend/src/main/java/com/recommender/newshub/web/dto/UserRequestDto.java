package com.recommender.newshub.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

public class UserRequestDto {

    @Getter
    public static class SignUpDto {
        @NotBlank
        @Length(max = 20)
        private String loginId;

        @NotBlank
        @Length(max = 20)
        private String password;
    }

    @Getter
    public static class LoginDto {
        @NotBlank
        @Length(max = 20)
        private String loginId;

        @NotBlank
        @Length(max = 20)
        private String password;
    }
}
