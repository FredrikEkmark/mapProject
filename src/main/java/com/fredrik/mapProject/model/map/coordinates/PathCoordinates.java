package com.fredrik.mapProject.model.map.coordinates;

import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;

public class PathCoordinates {

    MapCoordinates coordinates;

    int pathIteration;

    public PathCoordinates(MapCoordinates coordinates, int pathIteration) {
        this.coordinates = coordinates;
        this.pathIteration = pathIteration;
    }

    public PathCoordinates(MapCoordinates coordinates) {
        this.coordinates = coordinates;
        this.pathIteration = 0;
    }

    public MapCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(MapCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public int getPathIteration() {
        return pathIteration;
    }

    public int getNextPathIteration() {
        return pathIteration + 1;
    }

}
