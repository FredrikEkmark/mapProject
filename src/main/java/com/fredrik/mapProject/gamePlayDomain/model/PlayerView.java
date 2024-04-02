package com.fredrik.mapProject.gamePlayDomain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gameSetupDomain.MapSizes;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerView {

    final private UUID gameId;

    final private UUID playerId;

    final private String playerName;
    final private MapSizes mapSize;
    private MapCoordinates startCoordinates;
    final private Player playerNr;
    private List<MapTile> map;

    public PlayerView(UUID gameId,
                      UUID playerId,
                      String playerName,
                      MapSizes mapSize,
                      List<MapTileEntity> tileList,
                      MapCoordinates startCoordinates,
                      Player playerNr
    ) throws JsonProcessingException {
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.mapSize = mapSize;
        this.startCoordinates = startCoordinates;
        this.playerNr = playerNr;
        this.map = convertMapTileEntityListToMap(tileList , mapSize, playerNr);
    }

    private List<MapTile> convertMapTileEntityListToMap(List<MapTileEntity> tileList, MapSizes mapSize, Player playerNr) {

        int initialCapacity = mapSize.getWidth() * mapSize.getHeight();
        List<MapTile> map = new ArrayList<>(initialCapacity);

        for (MapTileEntity tile : tileList) {
            int y = tile.getMaptileId().getCoordinates().getY();
            int x = tile.getMaptileId().getCoordinates().getX();

            MapTile rePackagedTile = new MapTile(tile.getMaptileId().getCoordinates(), tile.getTileOwner(), tile.getTileValue(), tile.isVisible(playerNr.number()));
            map.add(rePackagedTile);
        }
            return map;

    }

    public UUID getGameId() {
        return gameId;
    }


    public UUID getPlayerId() {
        return playerId;
    }


    public String getPlayerName() {
        return playerName;
    }


    public MapSizes getMapSize() {
        return mapSize;
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


    public List<MapTile> getMap() {
        return map;
    }

    public void setMap(List<MapTileEntity> tileList) {
        this.map = convertMapTileEntityListToMap(tileList , mapSize, playerNr);
    }
}
