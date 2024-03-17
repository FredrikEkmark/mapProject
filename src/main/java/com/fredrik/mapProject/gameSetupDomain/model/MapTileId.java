package com.fredrik.mapProject.gameSetupDomain.model;

import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class MapTileId implements Serializable {

    private UUID gameId;

    @Embedded
    private MapCoordinates coordinates;
    public MapTileId() {
    }
    public MapTileId(UUID gameId, int x, int y) {
        this.gameId = gameId;
        this.coordinates = new MapCoordinates(x, y);
    }

    // Getters and setters
    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public MapCoordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(MapCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapTileId mapTileId = (MapTileId) o;
        return Objects.equals(gameId, mapTileId.gameId) && Objects.equals(coordinates, mapTileId.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, coordinates);
    }
}
