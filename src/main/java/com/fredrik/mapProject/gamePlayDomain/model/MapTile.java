package com.fredrik.mapProject.gamePlayDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;

public class MapTile {

    private MapCoordinates coordinates;
    private Player tileOwner;
    private int tileTerrainValue;
    private boolean visible;

    public MapTile(MapCoordinates coordinates, Player tileOwner, int tileTerrainValue, boolean visible) {
        this.coordinates = coordinates;
        this.tileOwner = tileOwner;
        this.tileTerrainValue = tileTerrainValue;
        this.visible = visible;
    }

    public MapCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(MapCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Player getTileOwner() {
        return tileOwner;
    }

    public void setTileOwner(Player tileOwner) {
        this.tileOwner = tileOwner;
    }

    public int getTileTerrainValue() {
        return tileTerrainValue;
    }

    public void setTileTerrainValue(int tileTerrainValue) {
        this.tileTerrainValue = tileTerrainValue;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
