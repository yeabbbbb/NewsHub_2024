package com.recommender.newshub.repository;

import com.recommender.newshub.domain.News;
import com.recommender.newshub.domain.enums.NewsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("SELECT n FROM News n WHERE n.isTopNews = true AND n.category IN :categories ORDER BY n.publishDate DESC")
    List<News> findTopNewsByCategories(@Param("categories") List<NewsCategory> categories, Pageable pageable);

    @Query("SELECT n FROM News n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY n.publishDate DESC")
    Page<News> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT n FROM News n WHERE n.category IN :categories ORDER BY n.publishDate DESC" )
    Page<News> findByCategories(@Param("categories") List<NewsCategory> categories, Pageable pageable);

    List<News> findByIdIn(List<Long> ids);

    @Query("SELECT COUNT(n) FROM News n WHERE n.category IN :categories")
    Long countNewsByCategories(@Param("categories") List<NewsCategory> categories);

    @Query("SELECT n FROM News n WHERE n.category IN :categories")
    List<News> findNewsByCategories(List<NewsCategory> categories, Pageable pageable);
}
