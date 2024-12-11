package com.recommender.newshub.web.controller;

import com.recommender.newshub.constants.SessionConst;
import com.recommender.newshub.domain.User;
import com.recommender.newshub.service.NewsClickLogService;
import com.recommender.newshub.web.dto.NewsRequestDto.LogClickDataDto;
import com.recommender.newshub.web.dto.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/click-log")
@RequiredArgsConstructor
@Tag(name = "News Click Log", description = "뉴스 클릭 기록 로깅")
public class NewsClickLogController {

    private final NewsClickLogService newsClickLogService;

    @PostMapping
    @Operation(summary = "뉴스 클릭 로깅 API")
    public ApiResponse<Void> logClickData(@Valid @RequestBody LogClickDataDto logClickDataDto,
                                          @SessionAttribute(name = SessionConst.USER) User user) {
        newsClickLogService.logClickData(user, logClickDataDto.getNewsId());
        return ApiResponse.onSuccess(HttpStatus.OK, null);
    }
}
