package com.fredrik.mapProject.gameRunDomain.model.building;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameRunDomain.Terrain;

public class Granary extends Building {

    private int baseProtectedFood = 5;

    public Granary(BuildingType type, int progress) {
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

        int protectedFood = (int) (baseProtectedFood * terrainModifier(terrain));

        mana.raiseProtectedFood(protectedFood);

        setEventLogEntry(String.format(
                "Tile %d:%d %s manpower upkeep %d, stored %d Food safely;",
                coordinates.getX(),
                coordinates.getY(),
                getType().getBuilding(),
                getType().getManpowerUpkeep(),
                protectedFood
        ));

        return true;
    }

    @Override
    protected double terrainModifier(Terrain terrain) {
        return 1;
    }
}
