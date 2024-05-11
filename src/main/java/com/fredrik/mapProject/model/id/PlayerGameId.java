package com.fredrik.mapProject.model.id;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class PlayerGameId implements Serializable {

    private UUID gameId;
    private UUID userId;

    public PlayerGameId() {
    }

    public PlayerGameId(UUID gameId, UUID userId) {
        this.gameId = gameId;
        this.userId = userId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerGameId that = (PlayerGameId) o;
        return Objects.equals(gameId, that.gameId) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, userId);
    }
}
