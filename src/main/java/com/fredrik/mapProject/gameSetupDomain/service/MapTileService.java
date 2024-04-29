package com.fredrik.mapProject.gameSetupDomain.service;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerGameId;
import com.fredrik.mapProject.gameSetupDomain.MapSizes;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileId;
import com.fredrik.mapProject.gameSetupDomain.repository.MapTileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MapTileService {

    private final MapTileRepository mapTileRepository;

    @Autowired
    public MapTileService(MapTileRepository mapTileRepository) {
        this.mapTileRepository = mapTileRepository;
    }

    public void deleteGameMap(UUID gameId) {
        mapTileRepository.deleteByGameId(gameId);
    }

    public List<MapTileEntity> getGameMap(UUID gameId) {
        return mapTileRepository.findByGameId(gameId);
    }

    public List<MapTileEntity> getPlayerGameMap(UUID gameId, Player player) {
        List<MapTileEntity> fullMap = mapTileRepository.findByGameIdAndPlayerVisibility(gameId, player.number());
        List<MapTileEntity> playerMap = new ArrayList<MapTileEntity>();

        for (MapTileEntity tile: fullMap) {
            if (tile.isVisible(player.number()))
            playerMap.add(tile);
        }
        return fullMap;
    }

    public MapTileEntity findGameTile(MapTileId id) {
        return mapTileRepository.findByMapTileId(id).get();
    }

    public void updateGameTile(MapTileEntity tile) {
        mapTileRepository.updateMapTileEntityByMapTileId(
                tile.getMapTileId(),
                tile.getTileValue(),
                tile.getTileOwner(),
                tile.getVisibility(),
                tile.getBuildingJsonString(),
                tile.getUnit());
    }

    public void updateGameMap(List<MapTileEntity> gameMap) {
        mapTileRepository.updateMapTileEntities(gameMap);
    }

    public void removePlayerInfluenceFromAllTiles(GamePlayerEntity gamePlayer) {
        List<MapTileEntity> gameMap = mapTileRepository.findByGameId(gamePlayer.getPlayerGameId().getGameId());

        List<MapTileEntity> updatedList = new ArrayList<>();

        for (MapTileEntity tile: gameMap) {
            if (tile.getTileOwner() == gamePlayer.getPlayerNr()) {
                tile.setTileOwner(Player.NONE);
                updatedList.add(tile);
            }
        }
        mapTileRepository.saveAll(updatedList);
    }
}
