package com.matvey.cinema.service.impl;

import com.matvey.cinema.exception.CustomNotFoundException;
import com.matvey.cinema.model.entities.Visit;
import com.matvey.cinema.service.VisitCounterService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class VisitCounterServiceImpl implements VisitCounterService {

    private static final Logger log = LoggerFactory.getLogger(VisitCounterServiceImpl.class);
    private final Map<String, Integer> visitCounts = new HashMap<>();
    private final List<Visit> visits = new ArrayList<>();

    public synchronized void writeVisit(String url) {
        try {
            visitCounts.put(url, visitCounts.getOrDefault(url, 0) + 1);
            Visit visit = new Visit(String.valueOf(visits.size() + 1), url, LocalDateTime.now());
            visits.add(visit);
            log.info("Запись посещения: URL = {}, общее количество = {}",
                    url, visitCounts.get(url));
        } catch (Exception e) {
            log.error("Ошибка при записи посещения для URL {}: {}", url, e.getMessage(), e);
            throw new RuntimeException("Ошибка при записи посещения", e);
        }
    }

    public synchronized int getVisitCount(String url) {
        try {
            int count = visitCounts.getOrDefault(url, 0);
            log.info("Получение количества посещений для URL {}: {}", url, count);
            return count;
        } catch (Exception e) {
            log.error("Ошибка при получении количества посещений для URL {}: {}",
                    url, e.getMessage(), e);
            throw new CustomNotFoundException("URL не найден: " + url);
        }
    }

    public synchronized List<Visit> getVisits() {
        try {
            log.info("Получение списка всех посещений, количество: {}", visits.size());
            return new ArrayList<>(visits);
        } catch (Exception e) {
            log.error("Ошибка при получении списка посещений: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении списка посещений", e);
        }
    }
}