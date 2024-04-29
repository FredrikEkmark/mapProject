package com.fredrik.mapProject.gameRunDomain.service;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.service.ManaService;
import com.fredrik.mapProject.gameRunDomain.TurnChange;
import com.fredrik.mapProject.gameRunDomain.model.entity.EventEntity;
import com.fredrik.mapProject.gameRunDomain.model.GameMapManager;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
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
    private final ManaService manaService;
    private final EventLogService eventLogService;

    @Autowired
    public TurnChangeService(GameSetupService gameSetupService, EventService eventService, MapTileService mapTileService, ManaService manaService, EventLogService eventLogService) {
        this.gameSetupService = gameSetupService;
        this.eventService = eventService;
        this.mapTileService = mapTileService;
        this.manaService = manaService;
        this.eventLogService = eventLogService;
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
            GameMapManager gameMap = new GameMapManager(mapTileService.getGameMap(game.getId()), game);
            List<ManaEntity> manaList = manaService.findAllManaByGameId(game.getId());

            TurnChange turnChange = new TurnChange(game, gameMap, eventList, manaList);
            turnChange.update();

            mapTileService.updateGameMap(turnChange.getGameMap().getUpdatedTiles());
            eventService.resetEventsAndSavePersistentEvents(turnChange.getEventEntityList(), game.getId());
            gameSetupService.updateGameSetup(turnChange.getGameSetup());
            manaService.updateAll(turnChange.getManaList());
            eventLogService.save(turnChange.getEventLog());
        }
    }
}
