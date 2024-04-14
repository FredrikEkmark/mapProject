package com.fredrik.mapProject.gamePlayDomain.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "game_player_mana")
public class ManaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID gameId;

    // Resources:
    @Column(nullable = false)
    private int population;
    @Column(nullable = false)
    private int populationMax;
    // Temporary:
    @Column(nullable = false)
    private int manpower;
    // Storable:
    @Column(nullable = false)
    private int food;
    @Column(nullable = false)
    private int stone;
    @Column(nullable = false)
    private int wood;
    @Column(nullable = false)
    private int leather;
    @Column(nullable = false)
    // Luxury:
    private int furniture;
    @Column(nullable = false)
    private int simpleClothes;

    public ManaEntity(UUID id, UUID gameId) {
        this.id = id;
        this.gameId = gameId;
    }

    public ManaEntity() {
    }



    // getters, only for playerView

    public int getPopulation() {
        return population;
    }

    public int getPopulationMax() {
        return populationMax;
    }

    public int getManpower() {
        return manpower;
    }

    public int getFood() {
        return food;
    }

    public int getStone() {
        return stone;
    }

    public int getWood() {
        return wood;
    }

    public int getLeather() {
        return leather;
    }

    public int getFurniture() {
        return furniture;
    }

    public int getSimpleClothes() {
        return simpleClothes;
    }
}
