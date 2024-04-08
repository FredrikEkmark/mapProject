package com.fredrik.mapProject.gamePlayDomain.controller;

import com.fredrik.mapProject.gameRunDomain.model.EventId;
import com.fredrik.mapProject.gameRunDomain.model.EventLogEntity;
import com.fredrik.mapProject.gameRunDomain.service.EventLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class EventLogRestController { // toDO add security to this controller

    private final EventLogService eventLogService;

    @Autowired
    public EventLogRestController(EventLogService eventLogService) {
        this.eventLogService = eventLogService;
    }

    @PostMapping("/api/event")
    public void postNewEvent(@RequestBody EventLogEntity event) {
        eventLogService.save(event);
    }

    @GetMapping("/api/event/{gameId}")
    public List<EventLogEntity> getAllEventByGameId(@PathVariable("gameId") UUID gameId) {
        return eventLogService.findAllByGameID(gameId);
    }

    @DeleteMapping("/api/event/{eventId}")
    public void deleteEvent(@PathVariable("eventId") UUID eventId) {
        eventLogService.deleteByEventId(eventId);
    }
}
