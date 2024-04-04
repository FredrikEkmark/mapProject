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

    @Column(nullable = false)
    private int seed;

    @ManyToOne()
    @JoinColumn(name = "users_id")
    private UserEntity owner;

    @Column(length = 24)
    @Enumerated(EnumType.STRING)
    private TurnLength turnLength;

    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private MapSizes mapSize;

    public GameSetupEntity(UserEntity owner, TurnLength turnLength, MapSizes mapSize) {
        this.owner = owner;
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

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
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
