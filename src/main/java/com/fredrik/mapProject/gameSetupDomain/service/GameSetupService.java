package com.fredrik.mapProject.gameSetupDomain.service;

import com.fredrik.mapProject.gamePlayDomain.service.GamePlayerService;
import com.fredrik.mapProject.gameRunDomain.service.EventService;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.repository.GameSetupRepository;
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

    @Autowired
    public GameSetupService(
            GameSetupRepository gameSetupRepository,
            MapTileService mapTileService,
            GamePlayerService gamePlayerService, EventService eventService) {
        this.gameSetupRepository = gameSetupRepository;
        this.mapTileService = mapTileService;
        this.gamePlayerService = gamePlayerService;
        this.eventService = eventService;
    }

    public void createNewGameSetup(GameSetupEntity gameSetup) {
        gameSetupRepository.save(gameSetup);

        if (gameSetupRepository.findById(gameSetup.getId()).isPresent()) {

            mapTileService.createNewGameMap(gameSetup);
            gamePlayerService.createNewGamePlayer(gameSetup, gameSetup.getOwner().getId());

        } else {
            System.out.printf("ERROR Game setup with id %s was not saved properly", gameSetup.getId());
        }
    }

    public GameSetupEntity findById(UUID id) {
        Optional<GameSetupEntity> gameSetup = gameSetupRepository.findById(id);
        if (gameSetup.isEmpty()) {
            return null;
        }

        return gameSetup.get();
    }

    public void delete(GameSetupEntity gameSetup) {
        gameSetupRepository.delete(gameSetup);

        if (gameSetupRepository.findById(gameSetup.getId()).isEmpty()) {

            mapTileService.deleteGameMap(gameSetup.getId());
            gamePlayerService.deleteAllGamePlayerByGameId(gameSetup.getId());
            eventService.deleteAllByGameId(gameSetup.getId());

        } else {
            System.out.printf("ERROR Game setup with id %s was not deleted properly", gameSetup.getId());
        }
    }

    public List<GameSetupEntity> findAllByTurnChange(int hour, int min) {
        return gameSetupRepository.findAllByTurnChange(hour);
    }

    public void updateGameSetup(GameSetupEntity gameSetup) {
        gameSetupRepository.save(gameSetup);
    }

    public void updateAllGameSetups(List<GameSetupEntity> gameSetups) {
        gameSetupRepository.saveAll(gameSetups);
    }
}
