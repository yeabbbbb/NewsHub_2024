package com.recommender.newshub.service;

import com.recommender.newshub.domain.News;
import com.recommender.newshub.domain.User;
import com.recommender.newshub.domain.mapping.NewsClickLog;
import com.recommender.newshub.exception.ex.NewsNotFoundException;
import com.recommender.newshub.repository.NewsClickLogRepository;
import com.recommender.newshub.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsClickLogService {

    private final NewsRepository newsRepository;
    private final NewsClickLogRepository newsClickLogRepository;

    public void logClickData(User user, Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new NewsNotFoundException("News article not found"));

        NewsClickLog newsClickLog = new NewsClickLog(user, news);
        newsClickLogRepository.save(newsClickLog);
    }
}
