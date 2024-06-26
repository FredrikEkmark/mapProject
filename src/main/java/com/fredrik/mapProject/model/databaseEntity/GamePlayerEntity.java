package com.fredrik.mapProject.model.databaseEntity;

import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.id.PlayerGameId;
import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Table(name = "game_player")
public class GamePlayerEntity {

    @EmbeddedId
    private PlayerGameId playerGameId;

    @Embedded
    private MapCoordinates startCoordinates;

    @Column(nullable = false, length = 24)
    @Enumerated(EnumType.STRING)
    private Player playerNr;

    private final UUID manaId = UUID.randomUUID();

    @Transient
    private String username; // Transient field to hold username temporarily

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getManaId() {
        return manaId;
    }
}
