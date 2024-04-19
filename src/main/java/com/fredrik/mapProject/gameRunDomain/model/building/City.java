package com.fredrik.mapProject.gameRunDomain.model.building;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gameRunDomain.Terrain;

public class City extends Building {

    private int baseFoodProduction = 8;
    public City(BuildingType type, int progress) {
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
        return 1;
    }
}
