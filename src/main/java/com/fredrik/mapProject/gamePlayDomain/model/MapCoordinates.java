package com.fredrik.mapProject.gamePlayDomain.model;

public class MapCoordinates {

    private int longitude; // X
    private int latitude; // Y

    public MapCoordinates() {
    }

    public MapCoordinates(int longitude, int latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Optional: If you want to keep getX() and getY() methods
    public int getX() {
        return longitude;
    }

    public void setX(int x) {
        this.longitude = x;
    }

    public int getY() {
        return latitude;
    }

    public void setY(int y) {
        this.latitude = y;
    }
}
