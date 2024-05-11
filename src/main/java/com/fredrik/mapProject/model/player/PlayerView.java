package com.fredrik.mapProject.model.player;

import com.fredrik.mapProject.config.Roles;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.map.MapSizes;
import com.fredrik.mapProject.model.map.tile.MapTile;
import com.fredrik.mapProject.model.databaseEntity.EventLogEntity;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;

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
    private int turn;
    private String turnChange;
    private List<MapTile> map;
    private final ManaEntity mana;
    private final List<EventLogEntity> eventLog;
    private final boolean isUpdating;
    private final boolean isAdmin;

    public PlayerView(UUID gameId,
                      UUID playerId,
                      String playerName,
                      MapSizes mapSize,
                      List<MapTileEntity> tileList,
                      MapCoordinates startCoordinates,
                      Player playerNr,
                      Roles userRole,
                      int turn,
                      String turnChange,
                      ManaEntity mana,
                      List<EventLogEntity> eventLog,
                      boolean isUpdating
    ) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.mapSize = mapSize;
        this.startCoordinates = startCoordinates;
        this.playerNr = playerNr;
        this.turn = turn;
        this.turnChange = turnChange;
        this.map = convertMapTileEntityListToMap(tileList , mapSize, playerNr);
        this.mana = mana;
        this.eventLog = eventLog;
        this.isUpdating = isUpdating;
        this.isAdmin = (userRole == Roles.ADMIN);
    }

    private List<MapTile> convertMapTileEntityListToMap(List<MapTileEntity> tileList, MapSizes mapSize, Player playerNr) {

        int initialCapacity = mapSize.getWidth() * mapSize.getHeight();
        List<MapTile> map = new ArrayList<>(initialCapacity);

        for (MapTileEntity tile : tileList) {

            MapTile rePackagedTile = new MapTile(
                    tile.getMapTileId().getCoordinates(),
                    tile.getTileOwner(),
                    tile.getTileValue(),
                    tile.isVisible(playerNr.number()),
                    tile.getBuildingJsonString());
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

    public ManaEntity getMana() {
        return mana;
    }

    public List<EventLogEntity> getEventLog() {
        return eventLog;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getTurnChange() {
        return turnChange;
    }

    public void setTurnChange(String turnChange) {
        this.turnChange = turnChange;
    }

    public boolean isUpdating() {
        return isUpdating;
    }

    public boolean isAdmin() {return isAdmin;}
}