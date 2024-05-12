package com.fredrik.mapProject.model.databaseEntity;

import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.mana.EventManaCost;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "game_player_mana")
public class ManaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID gameId;

    @Column(nullable = false, length = 24)
    @Enumerated(EnumType.STRING)
    private Player playerNr;

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

    @Transient
    private int protectedFood;

    public ManaEntity(UUID id, UUID gameId, Player playerNr) {
        this.id = id;
        this.gameId = gameId;
        this.population = 500;
        this.manpower = 200;
        this.food = 10;
        this.wood = 100;
        this.playerNr = playerNr;
    }

    public ManaEntity() {
    }

    public Player getPlayerNr() {
        return playerNr;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public void setPopulationMax(int populationMax) {
        this.populationMax = populationMax;
    }

    public void setManpower(int manpower) {
        this.manpower = manpower;
    }

    // adjustment methods

    public void lowerPopulation(int less) {
        population -= less;
    }

    public void raisePopulation(int more) {
        population += more;
    }

    public void lowerPopulationMax(int less) {
        populationMax -= less;
    }

    public void raisePopulationMax(int more) {
        populationMax += more;
    }

    public void lowerProtectedFood(int less) {
        protectedFood -= less;
    }

    public void raiseProtectedFood(int more) {
        protectedFood += more;
    }


    // transaction methods

    public boolean withdrawManpower(int withdrawal) {
        if (this.manpower < withdrawal) {
            return false;
        }
        this.manpower -= withdrawal;
        return true;
    }

    public int withdrawAllManpower() {
        int withdrawal = manpower;
        manpower = 0;
        return withdrawal;
    }

    public void depositManpower(int deposit) {
        manpower += deposit;
    }

    public boolean withdrawFood(int withdrawal) {
        if (this.food < withdrawal) {
            return false;
        }
        this.food -= withdrawal;
        return true;
    }

    public int withdrawAllFood() {
        int withdrawal = food;
        food  = 0;
        return withdrawal;
    }

    public void depositFood(int deposit) {
        food += deposit;
    }

    public boolean withdrawWood(int withdrawal) {
        if (this.wood < withdrawal) {
            return false;
        }
        this.wood -= withdrawal;
        return true;
    }

    public int withdrawAllWood() {
        int withdrawal = wood;
        wood  = 0;
        return withdrawal;
    }

    public void depositWood(int deposit) {
        wood += deposit;
    }

    public boolean withdrawStone(int withdrawal) {
        if (this.stone < withdrawal) {
            return false;
        }
        this.stone -= withdrawal;
        return true;
    }

    public int withdrawAllStone() {
        int withdrawal = stone;
        stone  = 0;
        return withdrawal;
    }

    public void depositStone(int deposit) {
        stone += deposit;
    }

    public boolean withdrawLeather(int withdrawal) {
        if (this.leather < withdrawal) {
            return false;
        }
        this.leather -= withdrawal;
        return true;
    }

    public int withdrawAllLeather() {
        int withdrawal = leather;
        leather  = 0;
        return withdrawal;
    }

    public void depositLeather(int deposit) {
        leather += deposit;
    }

    public boolean withdrawFurniture(int withdrawal) {
        if (this.furniture < withdrawal) {
            return false;
        }
        this.furniture -= withdrawal;
        return true;
    }

    public int withdrawAllFurniture() {
        int withdrawal = furniture;
        furniture  = 0;
        return withdrawal;
    }

    public void depositFurniture(int deposit) {
        furniture += deposit;
    }

    public boolean withdrawSimpleClothes(int withdrawal) {
        if (this.simpleClothes < withdrawal) {
            return false;
        }
        this.simpleClothes -= withdrawal;
        return true;
    }

    public int withdrawAllSimpleClothes() {
        int withdrawal = simpleClothes;
        simpleClothes  = 0;
        return withdrawal;
    }

    public void depositSimpleClothes(int deposit) {
        simpleClothes += deposit;
    }

    // Progression functions

    public int foodSpoilage() {
        int perishableFood = Math.max(food - protectedFood, 0);
        int spoiledFood = (int) (Math.floor(perishableFood * 0.2));
        food -= spoiledFood;
        return spoiledFood;
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

    public int getProtectedFood() {return protectedFood;}

    public void setProtectedFood(int protectedFood) {
        this.protectedFood = protectedFood;
    }

    public boolean payInFull(EventManaCost eventManaCost) {

        if (!validateCanPayInFull(eventManaCost)) {
            return false;
        }

        withdrawManpower(eventManaCost.getManpower());
        withdrawWood(eventManaCost.payWood());
        withdrawFood(eventManaCost.payFood());
        withdrawLeather(eventManaCost.payLeather());
        withdrawStone(eventManaCost.payStone());
        withdrawFurniture(eventManaCost.payFurniture());
        withdrawSimpleClothes(eventManaCost.paySimpleClothes());

        return true;
    }

    public boolean validateCanPayInFull(EventManaCost eventManaCost) {
        if (eventManaCost.getManpower() > this.manpower) {
            return false;
        }

        if (eventManaCost.getFood() > this.food) {
            return false;
        }

        if (eventManaCost.getWood() > this.wood) {
            return false;
        }

        if (eventManaCost.getStone() > this.stone) {
            return false;
        }

        if (eventManaCost.getLeather() > this.leather) {
            return false;
        }

        if (eventManaCost.getFurniture() > this.furniture) {
            return false;
        }

        if (eventManaCost.getSimpleClothes() > this.simpleClothes) {
            return false;
        }
        return true;
    }
}
