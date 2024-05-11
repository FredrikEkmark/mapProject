package com.fredrik.mapProject.model.building;

import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.map.terrain.Elevation;
import com.fredrik.mapProject.model.map.terrain.Terrain;

public class Carpentry extends Building {

    private int baseFurnitureProduction = 5;
    private int woodCost = 10;

    public Carpentry(BuildingType type, int progress) {
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

        boolean woodWithdrawn = mana.withdrawWood(woodCost);

        if (!woodWithdrawn) {
            int woodPayed = mana.withdrawAllWood();
            double woodPercentagePayed = woodPayed / woodCost;
            int furnitureProduction = (int) Math.floor((woodPercentagePayed * baseFurnitureProduction)  * terrainModifier(terrain));

            mana.depositFurniture(furnitureProduction);

            setEventLogEntry(String.format(
                    "Tile %d:%d %s manpower upkeep %d, consumed %d Wood and produced %d Furniture;",
                    coordinates.getX(),
                    coordinates.getY(),
                    getType().getBuilding(),
                    getType().getManpowerUpkeep(),
                    woodCost,
                    furnitureProduction
            ));

            return true;
        }

        int furnitureProduction = (int) (baseFurnitureProduction * terrainModifier(terrain));

        mana.depositFurniture(furnitureProduction);

        setEventLogEntry(String.format(
                "Tile %d:%d %s manpower upkeep %d, consumed %d Wood and produced %d Furniture;",
                coordinates.getX(),
                coordinates.getY(),
                getType().getBuilding(),
                getType().getManpowerUpkeep(),
                woodCost,
                furnitureProduction
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

        if (terrainModifier < 0) {
            terrainModifier = 0;
        }

        return terrainModifier;

    }
}
