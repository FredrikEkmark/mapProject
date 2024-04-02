package com.fredrik.mapProject.gamePlayDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Table(name = "game_player")
public class GamePlayerEntity {

    @EmbeddedId
    private PlayerGameId playerGameId;

    @Embedded
    private MapCoordinates startCoordinates;

    @Enumerated(EnumType.STRING)
    private Player playerNr;

    public GamePlayerEntity(UUID gameId, UUID playerId, MapCoordinates startCoordinates, Player playerNr) {
        this.playerGameId = new PlayerGameId(gameId, playerId);
        this.playerNr = playerNr;
        this.startCoordinates = startCoordinates;
    }


    public GamePlayerEntity() {
    }

    public PlayerGameId getPlayerGameId() {
        return playerGameId;
    }

    public void setPlayerGameId(PlayerGameId playerGameId) {
        this.playerGameId = playerGameId;
    }

    public MapCoordinates getStartCoordinates() {
        return startCoordinates;
    }

    public void setStartCoordinates(MapCoordinates startCoordinates) {
        this.startCoordinates = startCoordinates;
    }

    public Player getPlayerNr() {
        return playerNr;
    }

    public void setPlayerNr(Player playerNr) {
        this.playerNr = playerNr;
    }
}