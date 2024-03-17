package com.fredrik.mapProject.gamePlayDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gameSetupDomain.MapSizes;
import com.fredrik.mapProject.gameSetupDomain.mapGenerator.tile.TileInterpretation;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileId;

import java.util.List;
import java.util.UUID;

public class PlayerView {

    private final UUID gameId;

    private final UUID playerId;

    private final String playerName;
    private final MapSizes mapSize;
    private final MapCoordinates startCoordinates;
    private final Player playerNr;
    private MapTile[][] map;

    public PlayerView(UUID gameId,
                      UUID playerId,
                      String playerName,
                      MapSizes mapSize,
                      List<MapTileEntity> tileList,
                      MapCoordinates startCoordinates,
                      Player playerNr
    ) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.mapSize = mapSize;
        this.startCoordinates = startCoordinates;
        this.playerNr = playerNr;
        this.map = convertMapTileEntityListToMap(tileList , mapSize, playerNr);

    }

    private MapTile[][] convertMapTileEntityListToMap(List<MapTileEntity> tileList, MapSizes mapSize, Player playerNr) {

            MapTile[][] map = new MapTile[mapSize.getY()][mapSize.getX()];

        for (MapTileEntity tile : tileList) {
            int y = tile.getMaptileId().getCoordinates().getY();
            int x = tile.getMaptileId().getCoordinates().getX();

            map[y][x] = new MapTile(tile.getMaptileId().getCoordinates(), tile.getTileOwner(), tile.getTileValue(), tile.isVisible(playerNr.number()));
        }
            return map;

    }
}
