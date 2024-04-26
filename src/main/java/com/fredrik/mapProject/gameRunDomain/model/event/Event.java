package com.fredrik.mapProject.gameRunDomain.model.event;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameRunDomain.model.GameMapManager;

import java.util.UUID;

public abstract class Event {

    private UUID eventId;

    private Player playerNr;

    private int turn;

    private MapCoordinates primaryTileCoordinates;

    private final EventType eventType;

    private boolean persistent;

    private String eventLogEntry;

    public Event(UUID eventId, Player playerNr, int turn, MapCoordinates primaryTileCoordinates, EventType eventType, boolean persistent) {
        this.eventId = eventId;
        this.playerNr = playerNr;
        this.turn = turn;
        this.primaryTileCoordinates = primaryTileCoordinates;
        this.eventType = eventType;
        this.persistent = persistent;
        this.eventLogEntry = "";
    }

    // Setup functions for child classes

    public abstract String stringifyEventData();

    public abstract void parseFromEventData(String eventData);

    public abstract void parseFromCost(String cost);

    // Event Processing functions

    public abstract boolean processEvent(ManaEntity mana, GameMapManager gameMap);


    // Getters and Setters

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public Player getPlayerNr() {
        return playerNr;
    }

    public void setPlayerNr(Player playerNr) {
        this.playerNr = playerNr;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public EventType getEventType() {
        return eventType;
    }

    public MapCoordinates getPrimaryTileCoordinates() {
        return primaryTileCoordinates;
    }

    public void setPrimaryTileCoordinates(MapCoordinates primaryTileCoordinates) {
        this.primaryTileCoordinates = primaryTileCoordinates;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public String getEventLogEntry() {
        return eventLogEntry;
    }

    public void setEventLogEntry(String eventLogEntry) {
        this.eventLogEntry = eventLogEntry;
    }
}
