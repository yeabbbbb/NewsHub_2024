package com.recommender.newshub.domain;

import com.recommender.newshub.domain.enums.NewsCategory;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class News {

    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(length = 750)
    private String author;

    @Column(nullable = false)
    private String url;

    private String imageUrl;

    private Boolean isTopNews;

    private LocalDateTime publishDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NewsCategory category;
}
