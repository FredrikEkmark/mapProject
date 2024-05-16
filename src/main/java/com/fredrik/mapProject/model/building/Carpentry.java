package com.fredrik.mapProject.model.building;

import com.fredrik.mapProject.config.GameConfig;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.mana.StorableManaTypes;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.map.terrain.Terrain;

public class Carpentry extends Building {

    private final int baseWoodInput = GameConfig.getBuildingBaseInput(getType(), StorableManaTypes.WOOD);

    private final int baseFurnitureOutput = GameConfig.getBuildingBaseOutput(getType(), StorableManaTypes.FURNITURE);

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


        boolean woodWithdrawn = mana.withdrawWood(baseWoodInput);

        if (!woodWithdrawn) {
            int woodPayed = mana.withdrawAllWood();
            double woodPercentagePayed = (double) woodPayed / baseWoodInput;
            int furnitureProduction = (int) Math.floor((woodPercentagePayed * baseFurnitureOutput)  * terrainModifier(terrain));

            mana.depositFurniture(furnitureProduction);

            setEventLogEntry(String.format(
                    "Tile %d:%d %s manpower upkeep %d, consumed %d Wood and produced %d Furniture;",
                    coordinates.getX(),
                    coordinates.getY(),
                    getType().getBuilding(),
                    getType().getManpowerUpkeep(),
                    baseWoodInput,
                    furnitureProduction
            ));

            return true;
        }

        int furnitureProduction = (int) (baseFurnitureOutput * terrainModifier(terrain));

        mana.depositFurniture(furnitureProduction);

        setEventLogEntry(String.format(
                "Tile %d:%d %s manpower upkeep %d, consumed %d Wood and produced %d Furniture;",
                coordinates.getX(),
                coordinates.getY(),
                getType().getBuilding(),
                getType().getManpowerUpkeep(),
                baseWoodInput,
                furnitureProduction
        ));

        return true;
    }
}
