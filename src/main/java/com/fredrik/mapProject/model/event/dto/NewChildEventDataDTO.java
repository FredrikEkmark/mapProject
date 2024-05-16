package com.fredrik.mapProject.model.event.dto;

import com.fredrik.mapProject.model.event.EventType;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.player.Player;

import java.util.UUID;

public class NewChildEventDataDTO {

    private UUID eventId;
    private Player playerNr;
    private int turn;
    private MapCoordinates primaryTileCoordinates;
    private EventType eventType;
    private String eventData;
    private String cost;

    public NewChildEventDataDTO(UUID eventId,
                                Player playerNr,
                                int turn,
                                MapCoordinates primaryTileCoordinates,
                                EventType eventType,
                                String eventData,
                                String cost) {
        this.eventId = eventId;
        this.playerNr = playerNr;
        this.turn = turn;
        this.primaryTileCoordinates = primaryTileCoordinates;
        this.eventType = eventType;
        this.eventData = eventData;
        this.cost = cost;
    }

    public UUID getEventId() {
        return eventId;
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

    public String getCost() {
        return cost;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public void setPlayerNr(Player playerNr) {
        this.playerNr = playerNr;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setPrimaryTileCoordinates(MapCoordinates primaryTileCoordinates) {
        this.primaryTileCoordinates = primaryTileCoordinates;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }
}
