package com.fredrik.mapProject.gamePlayDomain.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.*;
import com.fredrik.mapProject.gameRunDomain.model.entity.EventLogEntity;
import com.fredrik.mapProject.gameRunDomain.service.EventLogService;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.fredrik.mapProject.gameSetupDomain.service.MapTileService;
import com.fredrik.mapProject.userDomain.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerViewService {

    private final MapTileService mapTileService;
    private final GamePlayerService gamePlayerService;
    private final ManaService manaService;
    private final EventLogService eventLogService;

    public PlayerViewService(MapTileService mapTileService, GamePlayerService gamePlayerService, ManaService manaService, EventLogService eventLogService) {
        this.mapTileService = mapTileService;
        this.gamePlayerService = gamePlayerService;
        this.manaService = manaService;
        this.eventLogService = eventLogService;
    }

    public PlayerView getPlayerView(GameSetupEntity gameSetup, UserEntity user) throws JsonProcessingException {

        PlayerGameId playerGameId = new PlayerGameId(gameSetup.getId(), user.getId());
        GamePlayerEntity gamePlayer = gamePlayerService.getGamePlayer(playerGameId).get();
        Player playerNr = gamePlayer.getPlayerNr();

        if (gameSetup.isUpdating()) {
            PlayerView playerView = new PlayerView(
                    gameSetup,
                    user.getId(),
                    user.getUsername(),
                    playerNr,
                    user.getRole()
            );

            return playerView;
        }

        List<MapTileEntity> mapTileEntities = mapTileService.getPlayerGameMap(gameSetup.getId(), playerNr);
        ManaEntity mana = manaService.findManaById(gamePlayer.getManaId()).get();
        List<EventLogEntity> eventLog = eventLogService.findPlayerEventLog(gamePlayer.getPlayerNr(), gameSetup.getId());

        MapCoordinates startCoordinates = gamePlayer.getStartCoordinates();

        PlayerView playerView = new PlayerView(
                gameSetup.getId(),
                user.getId(),
                user.getUsername(),
                gameSetup.getMapSize(),
                mapTileEntities,
                startCoordinates,
                playerNr,
                user.getRole(),
                gameSetup.getTurn(),
                gameSetup.getTurnChange(),
                mana,
                eventLog,
                gameSetup.isUpdating()
        );

        return playerView;
    }
}
