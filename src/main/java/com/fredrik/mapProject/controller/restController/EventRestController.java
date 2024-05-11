package com.fredrik.mapProject.controller.restController;

import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.databaseEntity.GamePlayerEntity;
import com.fredrik.mapProject.model.event.NewEventDTO;
import com.fredrik.mapProject.model.id.PlayerGameId;
import com.fredrik.mapProject.service.GamePlayerService;
import com.fredrik.mapProject.service.PlayerViewService;
import com.fredrik.mapProject.model.databaseEntity.EventEntity;
import com.fredrik.mapProject.service.EventService;
import com.fredrik.mapProject.service.GameSetupService;
import com.fredrik.mapProject.model.databaseEntity.UserEntity;
import com.fredrik.mapProject.service.user.SecurityUtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class EventRestController {

    private final EventService eventService;
    private final GamePlayerService gamePlayerService;
    private final SecurityUtilityService securityUtilityService;
    private final GameSetupService gameSetupService;

    @Autowired
    public EventRestController(EventService eventService, SecurityUtilityService securityUtilityService, PlayerViewService playerViewService, GamePlayerService gamePlayerService, SecurityUtilityService securityUtilityService1, GameSetupService gameSetupService) {
        this.eventService = eventService;
        this.gamePlayerService = gamePlayerService;
        this.securityUtilityService = securityUtilityService1;
        this.gameSetupService = gameSetupService;
    }

    @PostMapping("/api/event")
    public ResponseEntity<List<EventEntity>> postNewEvent(@RequestBody NewEventDTO newEventDTO) {

        GamePlayerEntity gamePlayer = getValidatedPlayerEntity(newEventDTO.getGameId());

        if (gamePlayer == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ArrayList<>());
        }

        if (gameSetupService.isUpdatingById(newEventDTO.getGameId())) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<>());
        }

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

        List<EventEntity> eventList =  eventService.findAllByGameIDAndPlayerNr(event.getGameId(), event.getPlayerNr());

        return ResponseEntity.status(HttpStatus.CREATED).body(eventList);
    }

    @GetMapping("/api/event/{gameId}/{player}")
    public ResponseEntity<List<EventEntity>> getAllEventByGameIdAndPlayerNr(@PathVariable("gameId") UUID gameId, @PathVariable("player") Player playerNr) {

        GamePlayerEntity gamePlayer = getValidatedPlayerEntity(gameId);

        if (gamePlayer == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ArrayList<>());
        }

        if (gameSetupService.isUpdatingById(gameId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<>());
        }

        List<EventEntity> eventList =  eventService.findAllByGameIDAndPlayerNr(gameId, gamePlayer.getPlayerNr());

        return ResponseEntity.status(HttpStatus.CREATED).body(eventList);
    }

    @DeleteMapping("/api/event/{gameId}/{eventId}")
    public ResponseEntity<List<EventEntity>> deleteEvent(@PathVariable("gameId") UUID gameId, @PathVariable("eventId") UUID eventId) {

        GamePlayerEntity gamePlayer = getValidatedPlayerEntity(gameId);

        if (gamePlayer == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ArrayList<>());
        }

        if (gameSetupService.isUpdatingById(gameId)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<>());
        }

        Optional<EventEntity> event = eventService.findByEventId(eventId);

        eventService.deleteByEventId(eventId);

        List<EventEntity> eventList =  eventService.findAllByGameIDAndPlayerNr(gameId, gamePlayer.getPlayerNr());

        return ResponseEntity.status(HttpStatus.CREATED).body(eventList);
    }

    private GamePlayerEntity getValidatedPlayerEntity(UUID gameId) {

        UserEntity user = securityUtilityService.getCurrentUser();
        Optional<GamePlayerEntity> gamePlayer = gamePlayerService.getGamePlayer(
                new PlayerGameId(gameId, user.getId()));

        if (gamePlayer.isEmpty()) {
            return null;
        }

        boolean userAndGamePlayerIDMatch = user.getId().equals(gamePlayer.get().getPlayerGameId().getUserId());

        if (!userAndGamePlayerIDMatch) {
            return null;
        }

        return gamePlayer.get();
    }
}



