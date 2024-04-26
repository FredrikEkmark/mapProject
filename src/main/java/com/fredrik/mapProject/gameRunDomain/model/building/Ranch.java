package com.fredrik.mapProject.gameRunDomain.model.building;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameRunDomain.Terrain;

public class Ranch extends Building {

    private int baseFoodProduction = 3;
    private int baseLeatherProduction = 3;

    public Ranch(BuildingType type, int progress) {
        super(type, progress);
    }

    @Override
    public boolean processProduction(ManaEntity mana, Terrain terrain, MapCoordinates coordinates) {


        boolean manPowerWithdrawn = mana.withdrawManpower(getType().getManpowerUpkeep());

        if (!manPowerWithdrawn) {
            setEventLogEntry(String.format(
                    "Tile %d:%d %s manpower upkeep could not be paid, the building took damage from disuse;",
                    coordinates.getX(),
                    coordinates.getY(),
                    getType().getBuilding()
            ));

            return false;
        }

        mana.raisePopulationMax(getType().getPopulationMaxBonus());

        int foodProduction = (int) (baseFoodProduction * terrainModifier(terrain));
        int leatherProduction = (int) (baseLeatherProduction * terrainModifier(terrain));

        mana.depositFood(foodProduction);
        mana.depositLeather(leatherProduction);

        setEventLogEntry(String.format(
                "Tile %d:%d %s manpower upkeep %d, produced %d Food and %d Leather;",
                coordinates.getX(),
                coordinates.getY(),
                getType().getBuilding(),
                getType().getManpowerUpkeep(),
                foodProduction,
                leatherProduction
        ));

        return true;
    }

    @Override
    protected double terrainModifier(Terrain terrain) {
        return 1;
    }
}
