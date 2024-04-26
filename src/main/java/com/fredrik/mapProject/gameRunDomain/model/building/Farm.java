package com.fredrik.mapProject.gameRunDomain.model.building;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameRunDomain.Terrain;

public class Farm extends Building {

    private final int baseFoodProduction = 3;

    public Farm(BuildingType type, int progress) {
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

        switch (terrain) {
            case GLACIER, GLACIAL_HEIGHTS, MOUNTAINS, DEEP_WATER, COASTAL_WATER -> { // ILLEGAL SHOULD NEVER HAPPEN
                return 1;
            }
            case HOT_DESERT, COLD_DESERT, HOT_DESERT_HILLS, COLD_DESERT_HILLS -> {
                return 1;
            }
            case HOT_STEPPE, HOT_STEPPE_HILLS -> {
                return 1;
            }
            default -> {
                return 1;
            }
        }
    }
}
