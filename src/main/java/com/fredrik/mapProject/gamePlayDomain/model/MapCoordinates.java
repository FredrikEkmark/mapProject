package com.fredrik.mapProject.gamePlayDomain.model;

public class MapCoordinates {

    private int longitude;
    private int latitude;

    public MapCoordinates() {
    }

    public MapCoordinates(int longitude, int latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
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
