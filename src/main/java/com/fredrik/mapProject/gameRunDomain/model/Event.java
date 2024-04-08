package com.fredrik.mapProject.gameRunDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;

public abstract class Event {

    private EventId eventId;

    private Player playerNr;

    private int turn;

    private final EventType eventType;

    public Event(EventId eventId, Player playerNr, int turn, EventType eventType) {
        this.eventId = eventId;
        this.playerNr = playerNr;
        this.turn = turn;
        this.eventType = eventType;
    }

    public abstract String parseToEventData();

    public EventId getEventId() {
        return eventId;
    }

    public void setEventId(EventId eventId) {
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
}
