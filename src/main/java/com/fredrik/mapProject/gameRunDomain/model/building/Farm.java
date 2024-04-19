package com.fredrik.mapProject.gameRunDomain.model.building;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gameRunDomain.Terrain;

public class Farm extends Building {

    private final int baseFoodProduction = 5;

    public Farm(BuildingType type, int progress) {
        super(type, progress);
    }

    @Override
    public boolean processProduction(ManaEntity mana, Terrain terrain) {

        mana.raisePopulationMax(getType().getPopulationMaxBonus());

        boolean manPowerWithdrawn = mana.withdrawManpower(getType().getManpowerUpkeep());

        if (manPowerWithdrawn)
            mana.depositFood((int) (baseFoodProduction * terrainModifier(terrain)));

        return manPowerWithdrawn;
    }

    @Override
    protected double terrainModifier(Terrain terrain) {

        switch (terrain) {
            case GLACIER, GLACIAL_HEIGHTS, MOUNTAINS, DEEP_WATER, COASTAL_WATER -> { // ILLEGAL SHOULD NEVER HAPPEN
                return 1;
            }
            case HOT_DESERT, COLD_DESERT, HOT_DESERT_HILLS, COLD_DESERT_HILLS -> {
                return 1;
            }
            case HOT_STEPPE, HOT_STEPPE_HILLS -> {
                return 1;
            }
            default -> {
                return 1;
            }
        }
    }
}
