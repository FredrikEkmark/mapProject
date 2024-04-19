package com.fredrik.mapProject.gamePlayDomain.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MapCoordinates other = (MapCoordinates) obj;
        return longitude == other.longitude && latitude == other.latitude;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude);
    }
}
