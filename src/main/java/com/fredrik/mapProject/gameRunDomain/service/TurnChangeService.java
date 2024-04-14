package com.fredrik.mapProject.gameRunDomain.service;

import com.fredrik.mapProject.gameRunDomain.TurnChange;
import com.fredrik.mapProject.gameRunDomain.model.EventEntity;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.fredrik.mapProject.gameSetupDomain.service.GameSetupService;
import com.fredrik.mapProject.gameSetupDomain.service.MapTileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnChangeService {

    private final GameSetupService gameSetupService;
    private final EventService eventService;
    private final MapTileService mapTileService;

    @Autowired
    public TurnChangeService(GameSetupService gameSetupService, EventService eventService, MapTileService mapTileService) {
        this.gameSetupService = gameSetupService;
        this.eventService = eventService;
        this.mapTileService = mapTileService;
    }

    public void runTurnChange(int hour, int min) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23.");
        }
        List<GameSetupEntity> gameSetups = gameSetupService.findAllByTurnChange(hour, min);

        for (GameSetupEntity game: gameSetups) {
            game.setUpdating(true);
        }
        gameSetupService.updateAllGameSetups(gameSetups);

        for (GameSetupEntity game: gameSetups) {
            List<EventEntity> eventList = eventService.findAllByGameID(game.getId());
            List<MapTileEntity> gameMap = mapTileService.getGameMap(game.getId());

            TurnChange turnChange = new TurnChange(game, gameMap, eventList);
            turnChange.update();

            mapTileService.updateGameMap(turnChange.getGameMap());
            eventService.resetEventsAndSavePersistentEvents(turnChange.getEventList(), game.getId());
            gameSetupService.updateGameSetup(turnChange.getGameSetup());
        }
    }
}
