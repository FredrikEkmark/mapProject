package com.fredrik.mapProject.gamePlayDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;

public class MapTile {

    private MapCoordinates coordinates;
    private Player tileOwner;

    // toDo change tileStyleClass to string but have to change the MapTileEntity to so that it saves it as the class name from the beginning
    private int tileStyleClass;
    private boolean visible;

    public MapTile(MapCoordinates coordinates, Player tileOwner, int tileStyleClass, boolean visible) {
        this.coordinates = coordinates;
        this.tileOwner = tileOwner;
        this.tileStyleClass = tileStyleClass;
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

    public int getTileStyleClass() {
        return tileStyleClass;
    }

    public void setTileStyleClass(int tileStyleClass) {
        this.tileStyleClass = tileStyleClass;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
