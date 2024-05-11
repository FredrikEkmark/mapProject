package com.fredrik.mapProject.model.building;

import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.map.terrain.Elevation;
import com.fredrik.mapProject.model.map.terrain.Terrain;

public class Fishery extends Building {

    private int baseFoodProduction = 5;

    public Fishery(BuildingType type, int progress) {
        super(
                type,
                progress
        );
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

        mana.depositFood(foodProduction);

        setEventLogEntry(String.format(
                "Tile %d:%d %s manpower upkeep %d, produced %d Food;",
                coordinates.getX(),
                coordinates.getY(),
                getType().getBuilding(),
                getType().getManpowerUpkeep(),
                foodProduction
        ));

        return true;
    }

    @Override
    protected double terrainModifier(Terrain terrain) {

        double terrainModifier = 1;

        // Elevation modifier
        if (terrain.getElevation() == Elevation.DEEP) {
            terrainModifier += 0.2;
        }

        if (terrainModifier < 0) {
            terrainModifier = 0;
        }

        return terrainModifier;

    }
}
