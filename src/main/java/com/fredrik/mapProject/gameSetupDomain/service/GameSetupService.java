package com.fredrik.mapProject.gameSetupDomain.service;

import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.repository.GameSetupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GameSetupService {

    private GameSetupRepository gameSetupRepository;

    @Autowired
    public GameSetupService(GameSetupRepository gameSetupRepository) {
        this.gameSetupRepository = gameSetupRepository;
    }

    public void createNewGameSetup(GameSetupEntity gameSetup) {
        gameSetupRepository.save(gameSetup);
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
    }
}
