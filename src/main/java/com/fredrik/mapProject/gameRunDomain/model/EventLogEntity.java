package com.fredrik.mapProject.gameRunDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "event_log")
public class EventLogEntity {

    @EmbeddedId
    private EventId eventId;

    @Enumerated(EnumType.STRING)
    private Player playerNr;

    private int turn;

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
}
