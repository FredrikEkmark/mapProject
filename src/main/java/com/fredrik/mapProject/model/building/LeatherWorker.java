package com.fredrik.mapProject.model.building;

import com.fredrik.mapProject.config.GameConfig;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.mana.StorableManaTypes;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.map.terrain.Terrain;

public class LeatherWorker extends Building {

    private final int baseSimpleClothesOutput = GameConfig.getBuildingBaseOutput(getType(), StorableManaTypes.SIMPLE_CLOTHES);
    private final int baseLeatherInput = GameConfig.getBuildingBaseInput(getType(), StorableManaTypes.LEATHER);

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

        boolean leatherWithdrawn = mana.withdrawLeather(baseLeatherInput);

        if (!leatherWithdrawn) {
            int leatherPayed = mana.withdrawAllLeather();
            double leatherPercentagePayed = (double) leatherPayed / baseLeatherInput;
            int simpleClothesProduction = (int) Math.floor((leatherPercentagePayed * baseSimpleClothesOutput)  * terrainModifier(terrain));

            mana.depositSimpleClothes(simpleClothesProduction);

            setEventLogEntry(String.format(
                    "Tile %d:%d %s manpower upkeep %d, consumed %d Wood and produced %d Furniture;",
                    coordinates.getX(),
                    coordinates.getY(),
                    getType().getBuilding(),
                    getType().getManpowerUpkeep(),
                    baseLeatherInput,
                    simpleClothesProduction
            ));

            return true;
        }

        int simpleClothesProduction = (int) (baseSimpleClothesOutput * terrainModifier(terrain));

        mana.depositSimpleClothes(simpleClothesProduction);

        setEventLogEntry(String.format(
                "Tile %d:%d %s manpower upkeep %d, consumed %d Leather and produced %d Simple Clothes;",
                coordinates.getX(),
                coordinates.getY(),
                getType().getBuilding(),
                getType().getManpowerUpkeep(),
                baseLeatherInput,
                simpleClothesProduction
        ));

        return true;
    }
}
