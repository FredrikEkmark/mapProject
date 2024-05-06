package com.fredrik.mapProject.gameRunDomain.model.building;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameRunDomain.model.terrain.Elevation;
import com.fredrik.mapProject.gameRunDomain.model.terrain.Precipitation;
import com.fredrik.mapProject.gameRunDomain.model.terrain.Temperature;
import com.fredrik.mapProject.gameRunDomain.model.terrain.Terrain;

public class LumberCamp extends Building {

    private int baseWoodProduction = 5;

    public LumberCamp(BuildingType type, int progress) {
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

        int woodProduction = (int) (baseWoodProduction * terrainModifier(terrain));

        mana.depositFood(woodProduction);

        setEventLogEntry(String.format(
                "Tile %d:%d %s manpower upkeep %d, produced %d Wood;",
                coordinates.getX(),
                coordinates.getY(),
                getType().getBuilding(),
                getType().getManpowerUpkeep(),
                woodProduction
        ));

        return true;
    }

    @Override
    protected double terrainModifier(Terrain terrain) {

        double terrainModifier = 1;

        // Temperature modifier
        if (terrain.getTemperature() == Temperature.ARCTIC) {
            terrainModifier -= 0.6;
        }

        // Precipitation modifier
        if (terrain.getPrecipitation() == Precipitation.NONE) {
            terrainModifier -= 0.8;
        } else if (terrain.getPrecipitation() == Precipitation.HIGH) {
            terrainModifier += 0.4;
        } else if (terrain.getPrecipitation() == Precipitation.MODERATE) {
            terrainModifier += 0.2;
        }

        if (terrainModifier < 0) {
            terrainModifier = 0;
        }

        return terrainModifier;
    }
}
