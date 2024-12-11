package com.recommender.newshub.web.controller;

import com.recommender.newshub.constants.SessionConst;
import com.recommender.newshub.converter.NewsConverter;
import com.recommender.newshub.domain.News;
import com.recommender.newshub.domain.User;
import com.recommender.newshub.resolver.PageCheck;
import com.recommender.newshub.service.NewsQueryService;
import com.recommender.newshub.service.RecommenderService;
import com.recommender.newshub.web.dto.NewsResponseDto.GetNewsResultDto;
import com.recommender.newshub.web.dto.NewsResponseDto.GetFixedNewsResultDto;
import com.recommender.newshub.web.dto.common.ApiResponse;
import com.recommender.newshub.web.dto.enums.ControllerNewsCategory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "뉴스 기사 조회")
public class NewsController {

    private final NewsQueryService newsQueryService;
    private final RecommenderService recommenderService;

    @GetMapping("/top")
    @Operation(summary = "카테고리별 주요 뉴스 기사 조회 API")
    public ApiResponse<GetFixedNewsResultDto> getTopNews(@RequestParam ControllerNewsCategory category) {
        List<News> topNewsList = newsQueryService.getTopNews(category);
        return ApiResponse.onSuccess(HttpStatus.OK, NewsConverter.toGetFixedNewsResultDto(topNewsList));
    }

    @GetMapping("/search")
    @Operation(summary = "키워드 검색 뉴스 기사 조회 API")
    public ApiResponse<GetNewsResultDto> getSearchNews(@RequestParam String keyword,
                                                       @PageCheck Integer page) {
        Page<News> searchNews = newsQueryService.getSearchNews(keyword, page);
        return ApiResponse.onSuccess(HttpStatus.OK, NewsConverter.toGetNewsResultDto(searchNews));
    }

    @GetMapping
    @Operation(summary = "카테고리별 최신 뉴스 기사 조회 API")
    public ApiResponse<GetNewsResultDto> getCategoryNews(@RequestParam ControllerNewsCategory category,
                                                         @PageCheck Integer page) {
        Page<News> categoryNews = newsQueryService.getCategoryNews(category, page);
        return ApiResponse.onSuccess(HttpStatus.OK, NewsConverter.toGetNewsResultDto(categoryNews));
    }

    @GetMapping("/recommended")
    @Operation(summary = "카테고리별 추천 뉴스 기사 조회 API")
    public ApiResponse<GetFixedNewsResultDto> getRecommendedNews(@RequestParam ControllerNewsCategory category,
                                                                 @SessionAttribute(name = SessionConst.USER) User user) {
        List<News> recommendedNews = recommenderService.getRecommendedNews(category, user);
        return ApiResponse.onSuccess(HttpStatus.OK, NewsConverter.toGetFixedNewsResultDto(recommendedNews));
    }
}
