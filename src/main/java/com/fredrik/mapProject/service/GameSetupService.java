package com.fredrik.mapProject.service;

import com.fredrik.mapProject.model.databaseEntity.GameSetupEntity;
import com.fredrik.mapProject.repository.GameSetupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GameSetupService {

    private final GameSetupRepository gameSetupRepository;
    private final MapTileService mapTileService;
    private final GamePlayerService gamePlayerService;
    private final EventService eventService;
    private final EventLogService eventLogService;

    @Autowired
    public GameSetupService(
            GameSetupRepository gameSetupRepository,
            MapTileService mapTileService,
            GamePlayerService gamePlayerService,
            EventService eventService,
            EventLogService eventLogService) {
        this.gameSetupRepository = gameSetupRepository;
        this.mapTileService = mapTileService;
        this.gamePlayerService = gamePlayerService;
        this.eventService = eventService;
        this.eventLogService = eventLogService;
    }

    public void createNewGameSetup(GameSetupEntity gameSetup) {

        gameSetupRepository.save(gameSetup);

        if (gameSetupRepository.findById(gameSetup.getId()).isPresent()) {

            gamePlayerService.createNewGamePlayer(gameSetup, gameSetup.getOwner().getId());

        } else {
            System.out.printf("ERROR Game setup with id %s was not saved properly", gameSetup.getId());
        }
    }

    public GameSetupEntity findById(UUID id) {
        Optional<GameSetupEntity> gameSetup = gameSetupRepository.findById(id);
        return gameSetup.orElse(null);

    }

    public void delete(GameSetupEntity gameSetup) {
        gameSetupRepository.delete(gameSetup);

        if (gameSetupRepository.findById(gameSetup.getId()).isEmpty()) {

            mapTileService.deleteGameMap(gameSetup.getId());
            gamePlayerService.deleteAllGamePlayerByGameId(gameSetup.getId());
            eventService.deleteAllByGameId(gameSetup.getId());
            eventLogService.deleteGameEventLog(gameSetup.getId());

        } else {
            System.out.printf("ERROR Game setup with id %s was not deleted properly", gameSetup.getId());
        }
    }

    public List<GameSetupEntity> findAllByTurnChange(int hour, int min) {
        String minuteString = String.format("%02d", min);
        return gameSetupRepository.findAllByTurnChange(hour, minuteString);
    }

    public void updateGameSetup(GameSetupEntity gameSetup) {
        gameSetupRepository.save(gameSetup);
    }

    public void updateAllGameSetups(List<GameSetupEntity> gameSetups) {
        gameSetupRepository.saveAll(gameSetups);
    }

    public boolean isUpdatingById(UUID gameId) {
        Optional<GameSetupEntity> gameSetup = gameSetupRepository.findById(gameId);
        return gameSetup.map(GameSetupEntity::isUpdating).orElse(true);
    }
}
