package com.recommender.newshub.converter;

import com.recommender.newshub.domain.User;
import com.recommender.newshub.web.dto.UserRequestDto.SignUpDto;

import static com.recommender.newshub.web.dto.UserRequestDto.LoginDto;

public class UserConverter {
    public static User toUser(SignUpDto signUpDto) {
        return new User(signUpDto.getLoginId(), signUpDto.getPassword());
    }

    public static User toUser(LoginDto loginDto) {
        return new User(loginDto.getLoginId(), loginDto.getPassword());
    }
}
