package com.fredrik.mapProject.gamePlayDomain.service;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerGameId;
import com.fredrik.mapProject.gamePlayDomain.repository.GamePlayerRepository;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileId;
import com.fredrik.mapProject.gameSetupDomain.service.MapTileService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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

        MapCoordinates startCoordinate = mapTileService.getPlayerStartPosition(Player.PLAYER_ONE, gameSetup.getId());

        GamePlayerEntity playerEntity = new GamePlayerEntity(gameSetup.getId(), player, startCoordinate, Player.PLAYER_ONE);

        List<MapTileId> mapTileIdList = new ArrayList<MapTileId>();

        int[] xOffsets = {-1, -1, 0, 0, 0, 1, 1};
        int[] yOffsets = {0, 1, -1, 1, 0, 0, 1};

        if (startCoordinate.getX() % 2 == 0) {
            yOffsets[1] = -1;
            yOffsets[6] = -1;
        }

        for (int i = 0; i < xOffsets.length; i++) {
            mapTileIdList.add(new MapTileId(gameSetup.getId(), startCoordinate.getX() + xOffsets[i], startCoordinate.getY() + yOffsets[i]));
        }

        mapTileService.updateTileVisibilityForPlayer(mapTileIdList, playerEntity.getPlayerNr());

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
