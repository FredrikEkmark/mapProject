package com.fredrik.mapProject.model.building;

import com.fredrik.mapProject.config.GameConfig;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.mana.StorableManaTypes;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.map.terrain.Terrain;

public class Ranch extends Building {

    private final int baseFoodOutput = GameConfig.getBuildingBaseOutput(getType(), StorableManaTypes.FOOD);
    private final int baseLeatherOutput = GameConfig.getBuildingBaseOutput(getType(), StorableManaTypes.LEATHER);

    public Ranch(BuildingType type, int progress) {
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

        int foodProduction = (int) (baseFoodOutput * terrainModifier(terrain));
        int leatherProduction = (int) (baseLeatherOutput * terrainModifier(terrain));

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
}
