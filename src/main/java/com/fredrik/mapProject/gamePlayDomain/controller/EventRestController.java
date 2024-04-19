package com.fredrik.mapProject.gamePlayDomain.controller;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gamePlayDomain.model.NewEventDTO;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerGameId;
import com.fredrik.mapProject.gamePlayDomain.service.GamePlayerService;
import com.fredrik.mapProject.gamePlayDomain.service.PlayerViewService;
import com.fredrik.mapProject.gameRunDomain.model.entity.EventEntity;
import com.fredrik.mapProject.gameRunDomain.service.EventService;
import com.fredrik.mapProject.userDomain.model.UserEntity;
import com.fredrik.mapProject.userDomain.service.SecurityUtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class EventRestController { // toDO add security to this controller

    private final EventService eventService;
    private final GamePlayerService gamePlayerService;
    private final SecurityUtilityService securityUtilityService;

    @Autowired
    public EventRestController(EventService eventService, SecurityUtilityService securityUtilityService, PlayerViewService playerViewService, GamePlayerService gamePlayerService, SecurityUtilityService securityUtilityService1) {
        this.eventService = eventService;
        this.gamePlayerService = gamePlayerService;
        this.securityUtilityService = securityUtilityService1;
    }

    @PostMapping("/api/event")
    public List<EventEntity> postNewEvent(@RequestBody NewEventDTO newEventDTO) {

        UserEntity user = securityUtilityService.getCurrentUser();

        Optional<GamePlayerEntity> gamePlayer = gamePlayerService.getGamePlayer(new PlayerGameId(newEventDTO.getGameId(), user.getId()));


        if (gamePlayer.isEmpty())
            System.out.println("No gamePlayer");

        if (gamePlayer.get().getPlayerNr() != newEventDTO.getPlayerNr())
            System.out.println("Wrong player");

        EventEntity event = new EventEntity(
                newEventDTO.getGameId(),
                newEventDTO.getPlayerNr(),
                newEventDTO.getTurn(),
                newEventDTO.getPrimaryTileCoordinates(),
                newEventDTO.getEventType(),
                newEventDTO.getEventData(),
                newEventDTO.getCost()
        );

        eventService.save(event);
        return eventService.findAllByGameIDAndPlayerNr(event.getGameId(), event.getPlayerNr());
    }

    @GetMapping("/api/event/{gameId}/{player}")
    public List<EventEntity> getAllEventByGameIdAndPlayerNr(@PathVariable("gameId") UUID gameId, @PathVariable("player") Player playerNr) {

        UserEntity user = securityUtilityService.getCurrentUser();

        Optional<GamePlayerEntity> gamePlayer = gamePlayerService.getGamePlayer(new PlayerGameId(gameId, user.getId()));

        if (gamePlayer.isEmpty())
            return null;

        if (gamePlayer.get().getPlayerNr() != playerNr)
            return null;

        return eventService.findAllByGameIDAndPlayerNr(gameId, playerNr);
    }

    @DeleteMapping("/api/event/{gameId}/{eventId}")
    public List<EventEntity> deleteEvent(@PathVariable("gameId") UUID gameId, @PathVariable("eventId") UUID eventId) {

        UserEntity user = securityUtilityService.getCurrentUser();

        Optional<GamePlayerEntity> gamePlayer = gamePlayerService.getGamePlayer(new PlayerGameId(gameId, user.getId()));
        Optional<EventEntity> event = eventService.findByEventId(eventId);

        if (gamePlayer.isEmpty() || event.isEmpty())
            return null;

        if (gamePlayer.get().getPlayerNr() != event.get().getPlayerNr())
            return null;

        eventService.deleteByEventId(eventId);
        return eventService.findAllByGameIDAndPlayerNr(gameId, event.get().getPlayerNr());
    }
}



