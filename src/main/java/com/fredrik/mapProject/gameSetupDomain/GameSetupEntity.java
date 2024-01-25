package com.fredrik.mapProject.gameSetupDomain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "game_setup")
public class GameSetupEntity {
    @Id
    private UUID id;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
