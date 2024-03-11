package com.fredrik.mapProject.gameSetupDomain.service;

import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.repository.GameSetupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GameSetupService {

    private final GameSetupRepository gameSetupRepository;

    private final MapTileService mapTileService;

    @Autowired
    public GameSetupService(GameSetupRepository gameSetupRepository, MapTileService mapTileService) {
        this.gameSetupRepository = gameSetupRepository;
        this.mapTileService = mapTileService;
    }

    public void createNewGameSetup(GameSetupEntity gameSetup) {
        gameSetupRepository.save(gameSetup);

        if (gameSetupRepository.findById(gameSetup.getId()).isPresent()) {

            mapTileService.createNewGameMap(gameSetup);

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

        } else {
            System.out.printf("ERROR Game setup with id %s was not deleted properly", gameSetup.getId());
        }
    }
}
