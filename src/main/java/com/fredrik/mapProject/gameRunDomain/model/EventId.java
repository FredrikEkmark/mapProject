package com.fredrik.mapProject.gameRunDomain.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class EventId implements Serializable {

    private UUID gameId;

    private UUID eventId;

    public EventId() {
    }

    public EventId(UUID gameId, UUID eventId) {
        this.gameId = gameId;
        this.eventId = eventId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventId eventId1 = (EventId) o;
        return Objects.equals(gameId, eventId1.gameId) && Objects.equals(eventId, eventId1.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, eventId);
    }
}
