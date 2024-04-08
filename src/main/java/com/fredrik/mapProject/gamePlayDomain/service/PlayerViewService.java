package com.fredrik.mapProject.gamePlayDomain.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerGameId;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerView;
import com.fredrik.mapProject.gameRunDomain.model.BuildEvent;
import com.fredrik.mapProject.gameSetupDomain.MapSizes;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.fredrik.mapProject.gameSetupDomain.service.MapTileService;
import com.fredrik.mapProject.userDomain.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PlayerViewService {

    private final MapTileService mapTileService;
    private final GamePlayerService gamePlayerService;

    public PlayerViewService(MapTileService mapTileService, GamePlayerService gamePlayerService) {
        this.mapTileService = mapTileService;
        this.gamePlayerService = gamePlayerService;
    }

    public PlayerView getPlayerView(GameSetupEntity gameSetup, UserEntity user) throws JsonProcessingException {

        PlayerGameId playerGameId = new PlayerGameId(gameSetup.getId(), user.getId());
        GamePlayerEntity gamePlayer = gamePlayerService.getGamePlayer(playerGameId).get();
        Player playerNr = gamePlayer.getPlayerNr();
        List<MapTileEntity> mapTileEntities = mapTileService.getPlayerGameMap(gameSetup.getId(), playerNr);

        MapCoordinates startCoordinates = gamePlayer.getStartCoordinates();

        PlayerView playerView = new PlayerView(
                gameSetup.getId(),
                user.getId(),
                user.getUsername(),
                gameSetup.getMapSize(),
                mapTileEntities,
                startCoordinates,
                playerNr
        );

        return playerView;
    }
}
