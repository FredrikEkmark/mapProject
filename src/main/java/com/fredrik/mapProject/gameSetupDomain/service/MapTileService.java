package com.fredrik.mapProject.gameSetupDomain.service;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameSetupDomain.MapSizes;
import com.fredrik.mapProject.gameSetupDomain.mapGenerator.MapGenerator;
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

    public void createNewGameMap(GameSetupEntity gameSetup) {
        MapSizes mapSize = gameSetup.getMapSize();

        MapTileEntity[][] map = MapGenerator.generateMap(
                gameSetup.getId(),
                gameSetup.getSeed(),
                mapSize.getHeight(),
                mapSize.getWidth()
        );

        List<MapCoordinates> startLocations = MapGenerator.generateStartLocations(map);

        for (int i = 0; i < startLocations.size(); i++) {
            map[startLocations.get(i).getY()][startLocations.get(i).getX()].setTileOwner(Player.values()[i]);
        }

        List<MapTileEntity> flatList = new ArrayList<>();
        for (MapTileEntity[] row : map) {
            for (MapTileEntity tile : row) {
                if (tile != null) {
                    flatList.addLast(tile);
                }
            }
        }

        mapTileRepository.saveAll(flatList);
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

    public MapCoordinates getPlayerStartPosition(Player player, UUID gameId) {
        Optional<MapTileEntity> startTile = mapTileRepository.findFirstByTileOwnerAndGameId(player, gameId);
        if (startTile.isEmpty())
            return null;

        return startTile.get().getMapTileId().getCoordinates();
    }

    public void updateTileVisibilityForPlayer(List<MapTileId> mapTileIds, Player player) {
        List<MapTileEntity> mapTiles = mapTileRepository.findAllById(mapTileIds);
        for (MapTileEntity tile: mapTiles) {
            tile.setPlayerVisibility(true, player.number());
        }
        mapTileRepository.updateMapTileEntities(mapTiles);

    }

    public void updateGameMap(List<MapTileEntity> gameMap) {
        // ToDO add logic here
    }
}
