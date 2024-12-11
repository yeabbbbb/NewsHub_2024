package com.recommender.newshub.config;

import com.recommender.newshub.service.NewsApiService;
import com.recommender.newshub.web.dto.AdminResponseDto.AddNewsResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerConfig {

    private final NewsApiService newsApiService;

    @Value("${scheduler.use}")
    private boolean useSchedule;

    @Scheduled(cron = "${scheduler.general-cron}")
    public void fetchGeneralNews() {
        if (useSchedule) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime sixHoursAgo = now.minusHours(6);
            AddNewsResultDto addNewsResultDto = newsApiService.fetchGeneralNews(sixHoursAgo, now);
            log.info("[Scheduler] Fetch General News : {}/{}", addNewsResultDto.getSavedNumber(),addNewsResultDto.getAvailable());
        }
    }

    @Scheduled(cron = "${scheduler.top-cron}")
    public void fetchTopNews() {
        if (useSchedule) {
            LocalDate today = LocalDate.now();
            AddNewsResultDto addNewsResultDto = newsApiService.fetchTopNews(today);
            log.info("[Scheduler] Fetch Top News : {}/{}", addNewsResultDto.getSavedNumber(),addNewsResultDto.getAvailable());
        }
    }
}
