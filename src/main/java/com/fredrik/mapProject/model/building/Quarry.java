package com.fredrik.mapProject.model.building;

import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.map.terrain.Elevation;
import com.fredrik.mapProject.model.map.terrain.Precipitation;
import com.fredrik.mapProject.model.map.terrain.Terrain;

public class Quarry extends Building {

    private int baseStoneProduction = 5;

    public Quarry(BuildingType type, int progress) {
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

        int stoneProduction = (int) (baseStoneProduction * terrainModifier(terrain));

        mana.depositStone(stoneProduction);

        setEventLogEntry(String.format(
                "Tile %d:%d %s manpower upkeep %d, produced %d Stone;",
                coordinates.getX(),
                coordinates.getY(),
                getType().getBuilding(),
                getType().getManpowerUpkeep(),
                stoneProduction
        ));

        return true;
    }

    @Override
    protected double terrainModifier(Terrain terrain) {

        double terrainModifier = 1;

        // Elevation modifier
        if (terrain.getElevation() == Elevation.HIGHLANDS) {
            terrainModifier += 0.2;
        } else if (terrain.getElevation() == Elevation.MOUNTAIN) {
            terrainModifier += 0.4;
        }

        // Precipitation modifier
        if (terrain.getPrecipitation() == Precipitation.NONE) {
            terrainModifier += 0.2;
        } else if (terrain.getPrecipitation() == Precipitation.HIGH) {
            terrainModifier -= 0.2;
        }

        if (terrainModifier < 0) {
            terrainModifier = 0;
        }

        return terrainModifier;
    }
}
