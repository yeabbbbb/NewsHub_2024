package com.recommender.newshub.web.controller;

import com.recommender.newshub.web.dto.common.ApiResponse;
import com.recommender.newshub.converter.UserConverter;
import com.recommender.newshub.domain.User;
import com.recommender.newshub.service.UserService;
import com.recommender.newshub.constants.SessionConst;
import com.recommender.newshub.web.dto.UserRequestDto.LoginDto;
import com.recommender.newshub.web.dto.UserRequestDto.SignUpDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "User", description = "회원가입/로그인/로그아웃")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "회원가입 API")
    public ApiResponse<Void> signup(@Valid @RequestBody SignUpDto signUpDto) {
        userService.signUp(UserConverter.toUser(signUpDto));
        return ApiResponse.onSuccess(HttpStatus.CREATED, null);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인 API")
    public ApiResponse<Void> login(@Valid @RequestBody LoginDto loginDto,
            HttpServletRequest request)
    {
        User user = userService.login(UserConverter.toUser(loginDto));

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.USER, user);

        return ApiResponse.onSuccess(HttpStatus.OK, null);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃 API")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return ApiResponse.onSuccess(HttpStatus.OK, null);
    }
}
