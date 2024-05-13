package com.fredrik.mapProject.service;

import com.fredrik.mapProject.model.databaseEntity.*;
import com.fredrik.mapProject.model.databaseEntity.UserEntity;
import com.fredrik.mapProject.model.id.PlayerGameId;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.player.PlayerView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Optional<PlayerView> getPlayerView(GameSetupEntity gameSetup, UserEntity user) {

        PlayerGameId playerGameId = new PlayerGameId(gameSetup.getId(), user.getId());
        GamePlayerEntity gamePlayer;
        Player playerNr;
        List<MapTileEntity> mapTileEntities;
        ManaEntity mana;
        List<EventLogEntity> eventLog;
        MapCoordinates startCoordinates;

        try {
            Optional<GamePlayerEntity> optionalGamePlayerEntity = gamePlayerService.getGamePlayer(playerGameId);
            if (optionalGamePlayerEntity.isEmpty()) {return Optional.empty();}
            gamePlayer = optionalGamePlayerEntity.get();
            playerNr = gamePlayer.getPlayerNr();
            mapTileEntities = mapTileService.getPlayerGameMap(gameSetup.getId(), playerNr);
            Optional<ManaEntity> optionalManaEntity = manaService.findManaById(gamePlayer.getManaId());
            if (optionalManaEntity.isEmpty()) {return Optional.empty();}
            mana = optionalManaEntity.get();
            eventLog = eventLogService.findPlayerEventLog(gamePlayer.getPlayerNr(), gameSetup.getId());
            startCoordinates = gamePlayer.getStartCoordinates();
        } catch (Exception e) {
            return Optional.empty();
        }

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
                gameSetup.getNextTurnChange(),
                mana,
                eventLog,
                gameSetup.isUpdating()
        );

        return Optional.of(playerView);
    }
}
