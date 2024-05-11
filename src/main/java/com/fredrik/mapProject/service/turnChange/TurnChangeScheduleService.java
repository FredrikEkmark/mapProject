package com.fredrik.mapProject.service.turnChange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TurnChangeScheduleService {

    final private TurnChangeService turnChangeService;

    @Autowired
    public TurnChangeScheduleService(TurnChangeService turnChangeService) {
        this.turnChangeService = turnChangeService;
    }

    @Scheduled(cron = "05 */10 * * * *") // Run every 10 min
    private void turnChangeScan() {

        ZoneId gmtZoneId = ZoneId.of("GMT");
        ZonedDateTime gmtDateTime = ZonedDateTime.now(gmtZoneId);
        String turnTime = String.format("%02d:%02d", gmtDateTime.getHour(), gmtDateTime.getMinute());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        ZonedDateTime currentTime = ZonedDateTime.now();
        String formattedCurrentTime = formatter.format(currentTime);
        System.out.printf("%s   Turn change process started for %s GMT...%n", formattedCurrentTime, turnTime);

        turnChangeService.runTurnChangeByTime(gmtDateTime.getHour(), gmtDateTime.getMinute());

        currentTime = ZonedDateTime.now();
        formattedCurrentTime = formatter.format(currentTime);
        System.out.printf("%s   Turn change process completed for %s GMT.%n",formattedCurrentTime, turnTime );
    }
}
