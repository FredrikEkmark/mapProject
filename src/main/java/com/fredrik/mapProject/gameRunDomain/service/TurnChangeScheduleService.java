package com.fredrik.mapProject.gameRunDomain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class TurnChangeScheduleService {

    final private TurnChangeService turnChangeService;

    @Autowired
    public TurnChangeScheduleService(TurnChangeService turnChangeService) {
        this.turnChangeService = turnChangeService;
    }

    @Scheduled(cron = "0 0 * * * *") // Run on the hour, every hour
    private void turnChangeScan() {
        ZoneId gmtZoneId = ZoneId.of("GMT");
        ZonedDateTime gmtDateTime = ZonedDateTime.now(gmtZoneId);
        int hour = gmtDateTime.getHour();
        System.out.printf("Turn change process started for hour %d...%n", hour);
        turnChangeService.runTurnChange(hour);
        System.out.printf("Turn change process completed for hour %d.%n", hour);
    }
}
