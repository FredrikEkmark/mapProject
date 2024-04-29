package com.fredrik.mapProject.gamePlayDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;

import java.util.List;

public class PlayerStartPositionDTO {

    Player player;

    MapCoordinates startPosition;

    List<MapTileEntity> startTiles;

    public PlayerStartPositionDTO(Player player, MapCoordinates startPosition, List<MapTileEntity> startTiles) {
        this.player = player;
        this.startPosition = startPosition;
        this.startTiles = startTiles;
    }

    public MapCoordinates getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(MapCoordinates startPosition) {
        this.startPosition = startPosition;
    }

    public List<MapTileEntity> getStartTiles() {
        return startTiles;
    }

    public void setStartTiles(List<MapTileEntity> startTiles) {
        this.startTiles = startTiles;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
