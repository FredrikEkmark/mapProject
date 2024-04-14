package com.fredrik.mapProject.gameRunDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "event_log")
public class EventLogEntity {

    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.AUTO)
    private EventLogId eventLogId;

    private String log;

    public EventLogEntity() {
    }

    public String getLog() {
        return log;
    }

    public UUID getGameId() {
        return eventLogId.getGameId();
    }


    public Player getPlayerNr() {
        return eventLogId.getPlayerNr();
    }


    public int getTurn() {
        return eventLogId.getTurn();
    }


    public int getLogIndex() {
        return eventLogId.getLogIndex();
    }


}
