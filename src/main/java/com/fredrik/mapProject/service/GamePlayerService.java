package com.fredrik.mapProject.service;

import com.fredrik.mapProject.model.databaseEntity.GamePlayerEntity;
import com.fredrik.mapProject.model.id.PlayerGameId;
import com.fredrik.mapProject.model.player.PlayerStartPositionDTO;
import com.fredrik.mapProject.repository.GamePlayerRepository;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.databaseEntity.GameSetupEntity;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GamePlayerService {

    private final GamePlayerRepository gamePlayerRepository;
    private final MapTileService mapTileService;
    private final ManaService manaService;

    @Autowired
    public GamePlayerService(GamePlayerRepository gamePlayerRepository, MapTileService mapTileService, ManaService manaService) {
        this.gamePlayerRepository = gamePlayerRepository;
        this.mapTileService = mapTileService;
        this.manaService = manaService;
    }

    public String createNewGamePlayer(GameSetupEntity gameSetup, UUID playerId) {

        List<MapTileEntity> gameMap = mapTileService.getGameMap(gameSetup.getId());

        GameMapManager gameMapManager = new GameMapManager(gameMap, gameSetup);

        PlayerStartPositionDTO playerStartPosition = gameMapManager.createNewStartPosition();

        GamePlayerEntity playerEntity = new GamePlayerEntity(
                gameSetup.getId(),
                playerId,
                playerStartPosition.getStartPosition(),
                playerStartPosition.getPlayer());

        mapTileService.updateGameMap(playerStartPosition.getStartTiles());
        gamePlayerRepository.save(playerEntity);
        manaService.createNewMana(playerEntity.getManaId(),
                playerEntity.getPlayerGameId().getGameId(),
                playerEntity.getPlayerNr());

        return "new Player added";
    }

    public Optional<GamePlayerEntity> getGamePlayer(PlayerGameId playerGameId) {
        return gamePlayerRepository.findByPlayerGameIdGameIdAndPlayerGameIdUserId(playerGameId.getGameId(), playerGameId.getUserId());
    }

    public List<GamePlayerEntity> getAllGamePlayersByGame(UUID gameId) {
        return gamePlayerRepository.findAllByPlayerGameIdGameId(gameId);
    }

    public List<GamePlayerEntity> getAllGamePlayersByPlayer(UUID userId) {
        return gamePlayerRepository.findAllByPlayerGameIdUserId(userId);
    }

    @Transactional
    public void deleteAllGamePlayerByGameId(UUID gameId) {
        gamePlayerRepository.deleteAllByGameId(gameId);
        manaService.deleteAllManaByGameID(gameId);
    }

    public void deleteGamePlayerById(GamePlayerEntity gamePlayer) {
        gamePlayerRepository.deleteById(gamePlayer.getPlayerGameId());
        manaService.deleteManaById(gamePlayer.getManaId());
        mapTileService.removePlayerInfluenceFromAllTiles(gamePlayer);
    }
}
