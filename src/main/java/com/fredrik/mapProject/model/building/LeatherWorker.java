package com.fredrik.mapProject.model.building;

import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.map.terrain.Elevation;
import com.fredrik.mapProject.model.map.terrain.Precipitation;
import com.fredrik.mapProject.model.map.terrain.Terrain;

public class LeatherWorker extends Building {

    private int baseSimpleClothesProduction = 5;
    private int leatherCost = 10;

    public LeatherWorker(BuildingType type, int progress) {
        super(
                type,
                progress
        );
    }

    @Override
    public boolean processProduction(ManaEntity mana, Terrain terrain, MapCoordinates coordinates) {

        boolean manpowerWithdrawn = mana.withdrawManpower(getType().getManpowerUpkeep());

        if (!manpowerWithdrawn) {
            setEventLogEntry(String.format(
                    "Tile %d:%d %s manpower upkeep could not be paid, the building took damage from disuse;",
                    coordinates.getX(),
                    coordinates.getY(),
                    getType().getBuilding()
            ));

            return false;
        }

        mana.raisePopulationMax(getType().getPopulationMaxBonus());

        boolean leatherWithdrawn = mana.withdrawLeather(leatherCost);

        if (!leatherWithdrawn) {
            int leatherPayed = mana.withdrawAllLeather();
            double leatherPercentagePayed = leatherPayed / leatherCost;
            int simpleClothesProduction = (int) Math.floor((leatherPercentagePayed * baseSimpleClothesProduction)  * terrainModifier(terrain));

            mana.depositSimpleClothes(simpleClothesProduction);

            setEventLogEntry(String.format(
                    "Tile %d:%d %s manpower upkeep %d, consumed %d Wood and produced %d Furniture;",
                    coordinates.getX(),
                    coordinates.getY(),
                    getType().getBuilding(),
                    getType().getManpowerUpkeep(),
                    leatherCost,
                    simpleClothesProduction
            ));

            return true;
        }

        int simpleClothesProduction = (int) (baseSimpleClothesProduction * terrainModifier(terrain));

        mana.depositSimpleClothes(simpleClothesProduction);

        setEventLogEntry(String.format(
                "Tile %d:%d %s manpower upkeep %d, consumed %d Leather and produced %d Simple Clothes;",
                coordinates.getX(),
                coordinates.getY(),
                getType().getBuilding(),
                getType().getManpowerUpkeep(),
                leatherCost,
                simpleClothesProduction
        ));

        return true;
    }

    @Override
    protected double terrainModifier(Terrain terrain) {

        double terrainModifier = 1;

        // Elevation modifier
        if (terrain.getElevation() == Elevation.HIGHLANDS) {
            terrainModifier -= 0.2;
        }

        // Precipitation modifier
        if (terrain.getPrecipitation() == Precipitation.LOW) {
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
