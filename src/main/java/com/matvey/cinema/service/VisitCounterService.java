package com.matvey.cinema.service;

import com.matvey.cinema.model.entities.Visit;
import java.util.List;

public interface VisitCounterService {

    void writeVisit(String url);

    int getVisitCount(String url);

    List<Visit> getVisits();
}
