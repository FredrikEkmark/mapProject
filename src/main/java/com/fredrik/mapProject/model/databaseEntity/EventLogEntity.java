package com.fredrik.mapProject.model.databaseEntity;

import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.id.EventLogId;
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

    public EventLogEntity(String log, UUID gameId, Player playerNr, int turn, int logIndex) {
        this.eventLogId = new EventLogId(gameId, playerNr, turn, logIndex);
        this.log = log;
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
