package com.fredrik.mapProject.model.event.dto;

import com.fredrik.mapProject.model.event.EventType;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.player.Player;

import java.util.UUID;

public class NewEventDTO {

    private UUID gameId;

    private Player playerNr;

    private int turn;

    private MapCoordinates primaryTileCoordinates;

    private EventType eventType;

    private String eventData;

    private String cost;

    public NewEventDTO() {
    }

    public NewEventDTO(UUID gameId,
                       Player playerNr,
                       int turn,
                       MapCoordinates primaryTileCoordinates,
                       EventType eventType,
                       String eventData,
                       String cost) {
        this.gameId = gameId;
        this.playerNr = playerNr;
        this.turn = turn;
        this.primaryTileCoordinates = primaryTileCoordinates;
        this.eventType = eventType;
        this.eventData = eventData;
        this.cost = cost;
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

    public String getCost() {return cost;}
}
