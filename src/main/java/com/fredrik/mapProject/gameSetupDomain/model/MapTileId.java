package com.fredrik.mapProject.gameSetupDomain.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class MapTileId implements Serializable {

    private UUID gameId;
    private int x;
    private int y;
    public MapTileId() {
    }
    public MapTileId(UUID gameId, int x, int y) {
        this.gameId = gameId;
        this.x = x;
        this.y = y;
    }

    // Getters and setters
    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapTileId mapTileId = (MapTileId) o;
        return x == mapTileId.x &&
                y == mapTileId.y &&
                Objects.equals(gameId, mapTileId.gameId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, x, y);
    }
}
