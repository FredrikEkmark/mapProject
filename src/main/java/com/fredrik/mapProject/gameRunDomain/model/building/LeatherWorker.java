package com.fredrik.mapProject.gameRunDomain.model.building;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gameRunDomain.Terrain;

public class LeatherWorker extends Building {

    private int baseSimpleClothesProduction = 5;
    private int leatherCost = 10;

    public LeatherWorker(BuildingType type, int progress) {
        super(type, progress);
    }

    @Override
    public boolean processProduction(ManaEntity mana, Terrain terrain) {

        mana.raisePopulationMax(getType().getPopulationMaxBonus());

        boolean manPowerWithdrawn = mana.withdrawManpower(getType().getManpowerUpkeep());
        boolean leatherWithdrawn = false;

        if (manPowerWithdrawn)
            leatherWithdrawn = mana.withdrawLeather(leatherCost);

        if (leatherWithdrawn)
            mana.depositSimpleClothes((int) (baseSimpleClothesProduction * terrainModifier(terrain)));

        return leatherWithdrawn;
    }

    @Override
    protected double terrainModifier(Terrain terrain) {
        return 1;
    }
}
