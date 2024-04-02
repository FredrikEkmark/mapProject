package com.fredrik.mapProject.gamePlayDomain.service;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerGameId;
import com.fredrik.mapProject.gamePlayDomain.repository.GamePlayerRepository;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.service.MapTileService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GamePlayerService {

    private final GamePlayerRepository gamePlayerRepository;

    private final MapTileService mapTileService;

    @Autowired
    public GamePlayerService(GamePlayerRepository gamePlayerRepository, MapTileService mapTileService) {
        this.gamePlayerRepository = gamePlayerRepository;
        this.mapTileService = mapTileService;
    }

    public void createNewGamePlayer(GameSetupEntity gameSetup, UUID player) {

        // ToDo write code to random out good starting locations
        MapCoordinates startCoordinate = mapTileService.getPlayerStartPosition(Player.PLAYER_ONE);

        GamePlayerEntity playerEntity = new GamePlayerEntity(gameSetup.getId(), player, startCoordinate, Player.PLAYER_ONE);

        // ToDo add owner change of the starting position tiles

        gamePlayerRepository.save(playerEntity);
    }

    public Optional<GamePlayerEntity> getGamePlayer(PlayerGameId playerGameId) {
        return gamePlayerRepository.findByPlayerGameIdGameIdAndPlayerGameIdUserId(playerGameId.getGameId(), playerGameId.getUserId());
    }

    @Transactional
    public void deleteAllGamePlayerByGameId(UUID gameId) {
        gamePlayerRepository.deleteAllByGameId(gameId);

    }
}
