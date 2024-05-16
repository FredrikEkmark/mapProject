package com.fredrik.mapProject.service.turnChange;

import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.turnChange.TurnChange;
import com.fredrik.mapProject.model.databaseEntity.EventEntity;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.databaseEntity.GameSetupEntity;
import com.fredrik.mapProject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TurnChangeService {

    private final GameSetupService gameSetupService;
    private final EventService eventService;
    private final MapTileService mapTileService;
    private final ManaService manaService;
    private final EventLogService eventLogService;

    private final ArmyService armyService;

    @Autowired
    public TurnChangeService(GameSetupService gameSetupService,
                             EventService eventService,
                             MapTileService mapTileService,
                             ManaService manaService,
                             EventLogService eventLogService,
                             ArmyService armyService) {
        this.gameSetupService = gameSetupService;
        this.eventService = eventService;
        this.mapTileService = mapTileService;
        this.manaService = manaService;
        this.eventLogService = eventLogService;
        this.armyService = armyService;
    }

    public void runTurnChangeByTime(int hour, int min) {
        if (hour < 0 || hour > 23) {
            throw new IllegalArgumentException("Hour must be between 0 and 23.");
        }
        List<GameSetupEntity> gameSetups = gameSetupService.findAllByTurnChange(hour, min);

        for (GameSetupEntity game: gameSetups) {
            game.setUpdating(true);
        }
        gameSetupService.updateAllGameSetups(gameSetups);

        for (GameSetupEntity game: gameSetups) {
            runTurnChange(game);
        }
    }

    public void runTurnChangeByGameId(UUID gameID) {

        GameSetupEntity gameSetup = gameSetupService.findById(gameID);

        gameSetup.setUpdating(true);

        gameSetupService.updateGameSetup(gameSetup);

        runTurnChange(gameSetup);

    }

    private void runTurnChange(GameSetupEntity game) {
        List<EventEntity> eventList = eventService.findAllByGameID(game.getId());
        List<ArmyEntity> armies = armyService.findAllByGameID(game.getId());
        GameMapManager gameMap = new GameMapManager(mapTileService.getGameMap(game.getId()), game, armies);
        List<ManaEntity> manaList = manaService.findAllManaByGameId(game.getId());

        TurnChange turnChange = new TurnChange(game, gameMap, eventList, manaList);
        turnChange.update();

        mapTileService.updateGameMap(turnChange.getGameMap().getUpdatedTiles());
        eventService.resetEventsAndSavePersistentEvents(turnChange.getEventEntityList(), game.getId());
        armyService.updateActiveArmies(turnChange.getGameMap().getAllArmies(),turnChange.getGameMap().getRemovedArmies());
        gameSetupService.updateGameSetup(turnChange.getGameSetup());
        manaService.updateAll(turnChange.getManaList());
        eventLogService.save(turnChange.getEventLog());
    }
}
