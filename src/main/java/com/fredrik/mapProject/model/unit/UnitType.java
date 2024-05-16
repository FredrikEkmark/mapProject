package com.fredrik.mapProject.model.unit;

import com.fredrik.mapProject.config.GameConfig;

public enum UnitType {
    INFANTRY("Infantry",
            GameConfig.getUnitBaseMovement("INFANTRY"),
            GameConfig.getUnitRegimentAmount("INFANTRY")),
    ARCHERS("Archers",
            GameConfig.getUnitBaseMovement("ARCHERS"),
            GameConfig.getUnitRegimentAmount("ARCHERS")),
    CAVALRY("Cavalry",
            GameConfig.getUnitBaseMovement("CAVALRY"),
            GameConfig.getUnitRegimentAmount("CAVALRY"));

    private final String unitName;

    private final int baseMovement;

    private final int regimentAmount;


    UnitType(String unitName, int baseMovement, int regimentAmount) {
        this.unitName = unitName;
        this.baseMovement = baseMovement;
        this.regimentAmount = regimentAmount;
    }

    public int getBaseMovement() {
        return baseMovement;
    }

    public int getRegimentAmount() {
        return regimentAmount;
    }

    public String getUnitName() {return unitName;}
}
