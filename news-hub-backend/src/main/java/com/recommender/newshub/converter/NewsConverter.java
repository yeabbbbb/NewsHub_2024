package com.recommender.newshub.converter;

import com.recommender.newshub.domain.News;
import com.recommender.newshub.domain.enums.NewsCategory;
import com.recommender.newshub.web.dto.NewsApiDto.NewsDetailDto;
import com.recommender.newshub.web.dto.NewsResponseDto.GetNewsResultDto;
import com.recommender.newshub.web.dto.NewsResponseDto.GetFixedNewsResultDto;
import com.recommender.newshub.web.dto.NewsResponseDto.NewsDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import java.util.List;

public class NewsConverter {

    public static News toNews(NewsDetailDto newsDetailDto, Boolean isTopNews) {
        NewsCategory category = newsDetailDto.getCategory() == null ?
                NewsCategory.UNKNOWN : NewsCategory.valueOf(StringUtils.upperCase(newsDetailDto.getCategory()));

        return News.builder()
                .id(newsDetailDto.getId())
                .title(newsDetailDto.getTitle())
                .summary(newsDetailDto.getSummary())
                .author(newsDetailDto.getAuthor())
                .url(newsDetailDto.getUrl())
                .imageUrl(newsDetailDto.getImage())
                .isTopNews(isTopNews)
                .publishDate(newsDetailDto.getPublishDate())
                .category(category)
                .build();
    }

    public static GetFixedNewsResultDto toGetFixedNewsResultDto(List<News> newsList) {
        List<NewsDto> newsDtoList = newsList.stream()
                .map(NewsConverter::toNewsDto)
                .toList();

        return new GetFixedNewsResultDto(newsDtoList.size(), newsDtoList);
    }

    public static GetNewsResultDto toGetNewsResultDto(Page<News> newsPage) {
        List<NewsDto> newsDtoList = newsPage.getContent().stream()
                .map(NewsConverter::toNewsDto)
                .toList();

        return GetNewsResultDto.builder()
                .isFirst(newsPage.isFirst())
                .isLast(newsPage.isLast())
                .number(newsDtoList.size())
                .totalPages(newsPage.getTotalPages())
                .totalElements(newsPage.getTotalElements())
                .news(newsDtoList)
                .build();
    }

    public static NewsDto toNewsDto(News news) {
        return NewsDto.builder()
                .id(news.getId())
                .title(news.getTitle())
                .summary(news.getSummary())
                .url(news.getUrl())
                .imageUrl(news.getImageUrl())
                .publishDate(news.getPublishDate())
                .author(news.getAuthor())
                .category(news.getCategory().toString())
                .build();
    }
}
