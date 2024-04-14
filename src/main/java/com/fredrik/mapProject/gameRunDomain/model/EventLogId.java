package com.fredrik.mapProject.gameRunDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class EventLogId implements Serializable {

    private UUID gameId;

    @Enumerated(EnumType.STRING)
    private Player playerNr;

    private int turn;

    private int logIndex;

    public EventLogId() {
    }

    public EventLogId(UUID gameId, Player playerNr, int turn, int logIndex) {
        this.gameId = gameId;
        this.playerNr = playerNr;
        this.turn = turn;
        this.logIndex = logIndex;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
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

    public int getLogIndex() {
        return logIndex;
    }

    public void setLogIndex(int logIndex) {
        this.logIndex = logIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventLogId that = (EventLogId) o;
        return turn == that.turn && logIndex == that.logIndex && Objects.equals(gameId, that.gameId) && playerNr == that.playerNr;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, playerNr, turn, logIndex);
    }
}
