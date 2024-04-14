package com.fredrik.mapProject.gamePlayDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gameRunDomain.model.EventType;
import jakarta.persistence.Embedded;

import java.util.UUID;

public class NewEventDTO {

    private UUID gameId;

    private Player playerNr;

    private int turn;

    private MapCoordinates primaryTileCoordinates;

    private EventType eventType;

    private String eventData;

    public NewEventDTO() {
    }

    public NewEventDTO(UUID gameId, Player playerNr, int turn, MapCoordinates primaryTileCoordinates, EventType eventType, String eventData) {
        this.gameId = gameId;
        this.playerNr = playerNr;
        this.turn = turn;
        this.primaryTileCoordinates = primaryTileCoordinates;
        this.eventType = eventType;
        this.eventData = eventData;
    }

    public UUID getGameId() {
        return gameId;
    }

    public Player getPlayerNr() {
        return playerNr;
    }

    public int getTurn() {
        return turn;
    }

    public MapCoordinates getPrimaryTileCoordinates() {
        return primaryTileCoordinates;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getEventData() {
        return eventData;
    }
}
