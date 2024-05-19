package com.fredrik.mapProject.model.map.tile;

import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.player.Player;

public class MapTile {

    private MapCoordinates coordinates;
    private Player tileOwner;
    private int tileTerrainValue;
    private boolean visible;

    private String building;

    private ArmyEntity army;

    public MapTile(MapCoordinates coordinates,
                   Player tileOwner,
                   int tileTerrainValue,
                   boolean visible,
                   String building,
                   ArmyEntity army) {
        this.coordinates = coordinates;
        this.tileOwner = tileOwner;
        this.tileTerrainValue = tileTerrainValue;
        this.visible = visible;
        this.building = building;
        this.army = army;
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

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public ArmyEntity getArmy() {return army;}

    public void setArmy(ArmyEntity army) {
        this.army = army;
    }
}
