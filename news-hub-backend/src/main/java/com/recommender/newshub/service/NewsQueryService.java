package com.recommender.newshub.service;

import com.recommender.newshub.domain.News;
import com.recommender.newshub.domain.enums.NewsCategory;
import com.recommender.newshub.repository.NewsRepository;
import com.recommender.newshub.web.dto.enums.ControllerNewsCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NewsQueryService {

    private final NewsRepository newsRepository;

    public static final int DEFAULT_SIZE = 10;

    private static final List<NewsCategory> ETC_CATEGORIES = Arrays.asList(
        NewsCategory.TECHNOLOGY, NewsCategory.HEALTH, NewsCategory.EDUCATION,
        NewsCategory.SCIENCE, NewsCategory.LIFESTYLE, NewsCategory.TRAVEL,
        NewsCategory.CULTURE, NewsCategory.ENVIRONMENT
    );

    public List<News> getTopNews(ControllerNewsCategory category) {
        List<NewsCategory> categories = getNewsCategories(category);
        return newsRepository.findTopNewsByCategories(categories, PageRequest.of(0, DEFAULT_SIZE));
    }

    public Page<News> getSearchNews(String keyword, Integer page) {
        return newsRepository.findByTitleContaining(keyword, PageRequest.of(page, DEFAULT_SIZE));
    }

    public Page<News> getCategoryNews(ControllerNewsCategory category, Integer page) {
        List<NewsCategory> categories = getNewsCategories(category);
        return newsRepository.findByCategories(categories, PageRequest.of(page, DEFAULT_SIZE));
    }

    public static List<NewsCategory> getNewsCategories(ControllerNewsCategory category) {
        List<NewsCategory> categories;

        if (category.equals(ControllerNewsCategory.ETC)) {
            categories = ETC_CATEGORIES;
        } else if (category.equals(ControllerNewsCategory.ALL)) {
            categories = List.of(NewsCategory.values());
        } else {
            categories = List.of(NewsCategory.valueOf(category.name()));
        }

        return categories;
    }
}
