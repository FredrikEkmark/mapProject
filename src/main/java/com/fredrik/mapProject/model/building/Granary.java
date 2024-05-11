package com.fredrik.mapProject.model.building;

import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.map.terrain.Precipitation;
import com.fredrik.mapProject.model.map.terrain.Temperature;
import com.fredrik.mapProject.model.map.terrain.Terrain;

public class Granary extends Building {

    private int baseProtectedFood = 10;

    public Granary(BuildingType type, int progress) {
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

        double terrainModifier = 1;

        // Temperature modifier
        if (terrain.getTemperature() == Temperature.ARCTIC) {
            terrainModifier += 0.2;
        } else if (terrain.getTemperature() == Temperature.SUBTROPICAL) {
            terrainModifier -= 0.2;
        } else if (terrain.getTemperature() == Temperature.TROPICAL) {
            terrainModifier -= 0.4;
        }

        // Precipitation modifier
        if (terrain.getPrecipitation() == Precipitation.NONE) {
            terrainModifier += 0.4;
        } else if (terrain.getPrecipitation() == Precipitation.LOW) {
            terrainModifier += 0.2;
        } else if (terrain.getPrecipitation() == Precipitation.HIGH) {
            terrainModifier -= 0.4;
        }

        if (terrainModifier < 0) {
            terrainModifier = 0;
        }

        return terrainModifier;

    }
}
