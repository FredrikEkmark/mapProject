package com.fredrik.mapProject.gamePlayDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gameSetupDomain.MapSizes;
import com.fredrik.mapProject.gameSetupDomain.mapGenerator.tile.TileInterpretation;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileId;

import java.util.List;
import java.util.UUID;

public class PlayerView {

    private UUID gameId;

    private UUID playerId;

    private String playerName;
    private MapSizes mapSize;
    private MapCoordinates startCoordinates;
    private Player playerNr;
    private MapTile[][] map;


    public PlayerView() {}
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

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public MapSizes getMapSize() {
        return mapSize;
    }

    public void setMapSize(MapSizes mapSize) {
        this.mapSize = mapSize;
    }

    public MapCoordinates getStartCoordinates() {
        return startCoordinates;
    }

    public void setStartCoordinates(MapCoordinates startCoordinates) {
        this.startCoordinates = startCoordinates;
    }

    public Player getPlayerNr() {
        return playerNr;
    }

    public void setPlayerNr(Player playerNr) {
        this.playerNr = playerNr;
    }

    public MapTile[][] getMap() {
        return map;
    }

    public void setMap(MapTile[][] map) {
        this.map = map;
    }
}
