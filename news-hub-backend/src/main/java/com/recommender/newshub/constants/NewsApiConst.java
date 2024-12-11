package com.recommender.newshub.constants;

import java.util.Arrays;
import java.util.List;

public class NewsApiConst {
    public static final String BASE_URL = "https://api.worldnewsapi.com";
    public static final String SEARCH_NEWS_PATH = "/search-news";
    public static final String TOP_NEWS_PATH = "/top-news";
    public static final String ENGLISH = "en";
    public static final String USA = "us";
    public static final List<String> NEWS_SOURCES = Arrays.asList(
            "bbc.co.uk", "aljazeera.com", "nytimes.com",
            "theguardian.com", "independent.co.uk", "ft.com",
            "vox.com", "financialpost.com", "businessnews.com.au",
            "techcentral.ie", "chronicle.com",
            "techgenyz.com", "theconversation.com", "globalissues.org",
            "techcentral.ie", "theatlantic.com", "newyorker.com"
    );
    public static final List<String> CATEGORIES = Arrays.asList(
            "politics", "sports", "business", "technology",
            "entertainment", "health", "science", "lifestyle",
            "travel", "culture", "education", "environment"
    );
}
