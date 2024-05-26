package com.fredrik.mapProject.model.unit;

import com.fredrik.mapProject.model.battle.dice.Dice;
import com.fredrik.mapProject.model.battle.dice.DiceRolls;

import java.util.Random;
import java.util.UUID;

public abstract class Unit {

    private UUID id;

    private UnitType unitType;

    private int unitAmount;

    private int unitMoral;

    private int incapacitatedUnits;

    private int equipmentModifier;

    private boolean isFortified;

    public Unit(UUID id, UnitType unitType, int unitAmount, int unitMoral, int equipmentModifier, boolean isFortified) {
        this.id = id;
        this.unitType = unitType;
        this.unitAmount = unitAmount;
        this.unitMoral = unitMoral;
        this.incapacitatedUnits = 0;
        this.equipmentModifier = equipmentModifier;
        this.isFortified = isFortified;
    }

    public abstract void battle(Unit unit);

    public UUID getId() {
        return id;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public int getActiveUnitAmount() {
        return unitAmount - incapacitatedUnits;
    }

    public int getUnitAmount() {
        return unitAmount;
    }

    public int getUnitMoral() {
        return unitMoral;
    }

    public int getIncapacitatedUnits() {
        return incapacitatedUnits;
    }

    public int getEquipmentModifier() {
        return equipmentModifier;
    }

    public void reduceMoral(int moralReduction) {
        this.unitMoral = Math.max(0, this.unitMoral - moralReduction);
    }

    public void incapacitateUnits(int unitsIncapacitated) {
        this.incapacitatedUnits += unitsIncapacitated;
    }

    public boolean isFortified() {
        return isFortified;
    }

    int rollForAttackDamage(Dice dice)  {
        int roll = DiceRolls.rollDice(dice) + equipmentModifier;
        double moralPercentage = unitMoral / 100;
        double amountPercentage = getActiveUnitAmount() / 100;
        int moralBasedDamage = (int) (roll * moralPercentage);
        int amountBasedDamage = (int) (roll * amountPercentage);
        return amountBasedDamage + moralBasedDamage;
    }
}
