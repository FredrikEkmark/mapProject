package com.fredrik.mapProject.GameRunDomain.Model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "game_map")
public class GameMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
