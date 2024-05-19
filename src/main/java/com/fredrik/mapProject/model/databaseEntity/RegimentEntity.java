package com.fredrik.mapProject.model.databaseEntity;

import com.fredrik.mapProject.config.GameConfig;
import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.unit.UnitType;
import com.fredrik.mapProject.model.unit.nameGenerator.UnitNameGenerator;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "unit_regiment")
public class RegimentEntity {

    @Id
    private UUID regimentId;

    @Column(nullable = false)
    private UUID armyId;

    @Column(nullable = false, length = 24)
    @Enumerated(EnumType.STRING)
    private Player playerNr;

    @Column(nullable = false)
    private String regimentName;

    @Column( nullable = false)
    private int unitAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private UnitType unitType;

    @Column(nullable = false)
    private int moral;

    @Column(nullable = false)
    private int equipmentModifier;

    @Column(nullable = false)
    private int movement;

    public RegimentEntity(UUID armyId,
                          UnitType unitType,
                          Player owner,
                          int regimentNumber,
                          int equipmentModifier) {
        this.armyId = armyId;
        this.regimentId = UUID.randomUUID();
        this.playerNr = owner;
        this.regimentName = UnitNameGenerator.getGeneratedRegimentName(regimentNumber + 1, unitType);
        this.unitAmount = unitType.getRegimentAmount();
        this.unitType = unitType;
        this.moral = 1; // toDo gameConfig
        this.equipmentModifier = equipmentModifier;
        this.movement = unitType.getBaseMovement();
    }

    public RegimentEntity() {
    }

    public UUID getRegimentId() {
        return regimentId;
    }

    public void setArmyId(UUID armyId) {
        this.armyId = armyId;
    }

    public Player getPlayerNr() {
        return playerNr;
    }

    public void setPlayerNr(Player playerNr) {
        this.playerNr = playerNr;
    }

    public String getRegimentName() {
        return regimentName;
    }

    public void setRegimentName(String regimentName) {
        this.regimentName = regimentName;
    }

    public int getUnitAmount() {
        return unitAmount;
    }

    public void setUnitAmount(int unitAmount) {
        this.unitAmount = unitAmount;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public int getMoral() {
        return moral;
    }

    public void setMoral(int moral) {
        this.moral = moral;
    }

    public int getEquipmentModifier() {
        return equipmentModifier;
    }

    public void setEquipmentModifier(int equipmentModifier) {
        this.equipmentModifier = equipmentModifier;
    }

    public int getMovement() {
        return movement;
    }

    public void setMovement(int movement) {
        this.movement = movement;
    }

    public UUID getArmyId() {
        return armyId;
    }
}
