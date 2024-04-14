package com.fredrik.mapProject.gamePlayDomain.controller;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerGameId;
import com.fredrik.mapProject.gamePlayDomain.service.GamePlayerService;
import com.fredrik.mapProject.gamePlayDomain.service.PlayerViewService;
import com.fredrik.mapProject.gameRunDomain.model.EventLogEntity;
import com.fredrik.mapProject.gameRunDomain.service.EventLogService;
import com.fredrik.mapProject.userDomain.model.UserEntity;
import com.fredrik.mapProject.userDomain.service.SecurityUtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class EventLogRestController { // toDO add security to this controller

    private final EventLogService eventLogService;
    private final GamePlayerService gamePlayerService;
    private final SecurityUtilityService securityUtilityService;

    @Autowired
    public EventLogRestController(EventLogService eventLogService, SecurityUtilityService securityUtilityService, PlayerViewService playerViewService, GamePlayerService gamePlayerService, SecurityUtilityService securityUtilityService1) {
        this.eventLogService = eventLogService;
        this.gamePlayerService = gamePlayerService;
        this.securityUtilityService = securityUtilityService1;
    }

    @PostMapping("/api/event")
    @PreAuthorize("hasAuthority('POST')")
    public List<EventLogEntity> postNewEvent(@RequestBody EventLogEntity event) {

        UserEntity user = securityUtilityService.getCurrentUser();

        Optional<GamePlayerEntity> gamePlayer = gamePlayerService.getGamePlayer(new PlayerGameId(event.getGameId(), user.getId()));

        if (gamePlayer.isEmpty())
            return null;

        if (gamePlayer.get().getPlayerNr() != event.getPlayerNr())
            return null;

        eventLogService.save(event);
        return eventLogService.findAllByGameIDAndPlayerNr(event.getGameId(), event.getPlayerNr());
    }

    @GetMapping("/api/event/{gameId}/{player}")
    public List<EventLogEntity> getAllEventByGameIdAndPlayerNr(@PathVariable("gameId") UUID gameId, @PathVariable("player") Player playerNr) {

        UserEntity user = securityUtilityService.getCurrentUser();

        Optional<GamePlayerEntity> gamePlayer = gamePlayerService.getGamePlayer(new PlayerGameId(gameId, user.getId()));

        if (gamePlayer.isEmpty())
            return null;

        if (gamePlayer.get().getPlayerNr() != playerNr)
            return null;

        return eventLogService.findAllByGameIDAndPlayerNr(gameId, playerNr);
    }

    @DeleteMapping("/api/event/{gameId}/{player}/{eventId}")
    public List<EventLogEntity> deleteEvent(@PathVariable("gameId") UUID gameId, @PathVariable("player") Player playerNr, @PathVariable("eventId") UUID eventId) {

        UserEntity user = securityUtilityService.getCurrentUser();

        Optional<GamePlayerEntity> gamePlayer = gamePlayerService.getGamePlayer(new PlayerGameId(gameId, user.getId()));

        if (gamePlayer.isEmpty())
            return null;

        if (gamePlayer.get().getPlayerNr() != playerNr)
            return null;

        eventLogService.deleteByEventId(eventId);
        return eventLogService.findAllByGameIDAndPlayerNr(gameId, playerNr);
    }
}


