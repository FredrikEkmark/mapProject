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

import java.util.*;

@Service
public class GamePlayerService {

    private final GamePlayerRepository gamePlayerRepository;

    private final MapTileService mapTileService;

    @Autowired
    public GamePlayerService(GamePlayerRepository gamePlayerRepository, MapTileService mapTileService) {
        this.gamePlayerRepository = gamePlayerRepository;
        this.mapTileService = mapTileService;
    }

    public String createNewGamePlayer(GameSetupEntity gameSetup, UUID playerId) {

        List<Player> allPlayerNumbers = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(Player.values(), 0, gameSetup.getMapSize().getMaxPlayers())));

        List<GamePlayerEntity> gamePlayers = getAllGamePlayersByGame(gameSetup.getId());

        if (gamePlayers.size() >= gameSetup.getMapSize().getMaxPlayers()) {
            return "To many players";
        }

        for (GamePlayerEntity gamePlayer: gamePlayers) {
            allPlayerNumbers.remove(gamePlayer.getPlayerNr());
        }

        Player player = allPlayerNumbers.get(0);
        System.out.println(player.getColorHex() + player.name());
        System.out.println(mapTileService.getPlayerStartPosition(player, gameSetup.getId()));

        MapCoordinates startCoordinate = mapTileService.getPlayerStartPosition(player, gameSetup.getId());

        GamePlayerEntity playerEntity = new GamePlayerEntity(gameSetup.getId(), playerId, startCoordinate, player);

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

    }
}
