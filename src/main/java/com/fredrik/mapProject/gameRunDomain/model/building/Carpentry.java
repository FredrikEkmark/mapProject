package com.fredrik.mapProject.gameRunDomain.model.building;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gameRunDomain.Terrain;

public class Carpentry extends Building {

    private int baseFurnitureProduction = 5;
    private int woodCost = 10;

    public Carpentry(BuildingType type, int progress) {
        super(type, progress);
    }

    @Override
    public boolean processProduction(ManaEntity mana, Terrain terrain) {

        mana.raisePopulationMax(getType().getPopulationMaxBonus());

        boolean manPowerWithdrawn = mana.withdrawManpower(getType().getManpowerUpkeep());
        boolean woodWithdrawn = false;

        if (manPowerWithdrawn)
            woodWithdrawn = mana.withdrawWood(woodCost);

        if (woodWithdrawn)
            mana.depositFurniture((int) (baseFurnitureProduction * terrainModifier(terrain)));

        return woodWithdrawn;
    }

    @Override
    protected double terrainModifier(Terrain terrain) {
        return 1;
    }
}
