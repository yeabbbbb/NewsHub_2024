package com.recommender.newshub.repository;

import com.recommender.newshub.domain.User;
import com.recommender.newshub.domain.mapping.NewsClickLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsClickLogRepository extends JpaRepository<NewsClickLog, Long> {
    Boolean existsByUser(User user);
}
