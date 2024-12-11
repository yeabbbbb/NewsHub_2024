package com.recommender.newshub.service;

import com.recommender.newshub.converter.NewsConverter;
import com.recommender.newshub.domain.News;
import com.recommender.newshub.exception.ex.ApiFetchException;
import com.recommender.newshub.repository.NewsRepository;
import com.recommender.newshub.web.dto.AdminResponseDto.AddNewsResultDto;
import com.recommender.newshub.web.dto.NewsApiDto.NewsDetailDto;
import com.recommender.newshub.web.dto.NewsApiDto.SearchNewsResultDto;
import com.recommender.newshub.web.dto.NewsApiDto.TopNewsResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.recommender.newshub.constants.NewsApiConst.*;

@Slf4j
@Service
@Transactional
public class NewsApiService {

    private final NewsRepository newsRepository;
    private final WebClient webClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${news-api-key}")
    public String API_KEY;

    public NewsApiService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
        this.webClient = getWebClient();
    }

    public AddNewsResultDto fetchGeneralNews(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        String uri = buildSearchNewsUri(startDateTime, endDateTime);
        SearchNewsResultDto fetchResult = callNewsApi(uri, SearchNewsResultDto.class);

        List<News> savedNews = saveNews(fetchResult.getNewsDetailDtos(), false);
        return new AddNewsResultDto(fetchResult.getAvailable(), savedNews.size());
    }

    public AddNewsResultDto fetchTopNews(LocalDate date) {
        String uri = buildTopNewsUri(date);
        TopNewsResultDto topNewsResultDto = callNewsApi(uri, TopNewsResultDto.class);

        List<NewsDetailDto> newsDetailDtos = new ArrayList<>();
        topNewsResultDto.getTopNewsDtos()
                        .forEach(topNewsDtos -> newsDetailDtos.add(topNewsDtos.getNewsDetailDtos().get(0)));

        List<News> savedNews = saveNews(newsDetailDtos, true);
        return new AddNewsResultDto(newsDetailDtos.size(), savedNews.size());
    }

    private <T> T callNewsApi(String uri, Class<T> responseType) {
        return webClient.get()
                .uri(uri)
                .headers(headers -> headers.set("x-api-key", API_KEY))
                .retrieve()
                .onStatus(HttpStatusCode::isError, this::handleFetchError)
                .bodyToMono(responseType)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))
                        .filter(throwable -> throwable instanceof WebClientResponseException.TooManyRequests))
                .block();
    }

    private List<News> saveNews(List<NewsDetailDto> newsDetailDtos, Boolean isTopNews) {
        List<News> news = newsDetailDtos.stream()
                .filter(this::validateNewsDetailDto)
                .map(newsDetailDto -> NewsConverter.toNews(newsDetailDto, isTopNews))
                .toList();
        return newsRepository.saveAll(news);
    }

    private boolean validateNewsDetailDto(NewsDetailDto newsDetailDto) {
        return newsDetailDto.getId() != null
                && newsDetailDto.getTitle() != null
                && newsDetailDto.getUrl() != null;
    }

    private static WebClient getWebClient() {
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                        .build())
                .baseUrl(BASE_URL)
                .build();
    }

    private String buildSearchNewsUri(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return UriComponentsBuilder.fromPath(SEARCH_NEWS_PATH)
                .queryParam("language", ENGLISH)
                .queryParam("news-sources", String.join(",", NEWS_SOURCES))
                .queryParam("categories", String.join(",", CATEGORIES))
                .queryParam("offset", 0)
                .queryParam("number", 100)
                .queryParam("earliest-publish-date", startDateTime.format(formatter))
                .queryParam("latest-publish-date", endDateTime.format(formatter))
                .build().toUriString();
    }

    private String buildTopNewsUri(LocalDate date) {
        return UriComponentsBuilder.fromPath(TOP_NEWS_PATH)
                .queryParam("source-country", USA)
                .queryParam("language", ENGLISH)
                .queryParam("headlines-only", Boolean.FALSE.toString())
                .queryParam("date", date.toString())
                .build().toUriString();
    }

    private Mono<Throwable> handleFetchError(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                .flatMap(responseBody -> {
                    log.error("[NewsApi] fetch error: {}", responseBody);
                    return Mono.error(new ApiFetchException(responseBody));
                });
    }
}
