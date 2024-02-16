package com.fredrik.mapProject.gameSetupDomain.model;

import com.fredrik.mapProject.gameSetupDomain.MapSizes;
import com.fredrik.mapProject.gameSetupDomain.TurnLength;
import com.fredrik.mapProject.userDomain.model.UserEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "game_setup")
public class GameSetupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne()
    @JoinColumn(name = "users_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private TurnLength turnLength;

    @Enumerated(EnumType.STRING)
    private MapSizes mapSize;

    public GameSetupEntity(UserEntity user, TurnLength turnLength, MapSizes mapSize) {
        this.user = user;
        this.turnLength = turnLength;
        this.mapSize = mapSize;
    }

    public GameSetupEntity() {

    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public TurnLength getTurnLength() {
        return turnLength;
    }

    public void setTurnLength(TurnLength turnLength) {
        this.turnLength = turnLength;
    }

    public MapSizes getMapSize() {
        return mapSize;
    }

    public void setMapSize(MapSizes mapSize) {
        this.mapSize = mapSize;
    }
}
