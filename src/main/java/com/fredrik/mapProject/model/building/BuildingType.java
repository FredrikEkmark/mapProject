package com.fredrik.mapProject.model.building;

import com.fredrik.mapProject.config.GameConfig;
import com.fredrik.mapProject.model.map.terrain.Elevation;

import java.util.Arrays;
import java.util.List;

public enum BuildingType {

    NONE("none", 0,0, 0, new Elevation[] {}),
    FARM("Farm",
            GameConfig.getBuildingPopulationMaxBonus("FARM"),
            GameConfig.getBuildingManpowerUpkeep("FARM"),
            GameConfig.getBuildingCompleteAtProgress("FARM"),
            GameConfig.getBuildingBuildableElevations("FARM")),
    GRANARY("Granary",
            GameConfig.getBuildingPopulationMaxBonus("GRANARY"),
            GameConfig.getBuildingManpowerUpkeep("GRANARY"),
            GameConfig.getBuildingCompleteAtProgress("GRANARY"),
            GameConfig.getBuildingBuildableElevations("GRANARY")),
    QUARRY("Quarry",
            GameConfig.getBuildingPopulationMaxBonus("QUARRY"),
            GameConfig.getBuildingManpowerUpkeep("QUARRY"),
            GameConfig.getBuildingCompleteAtProgress("QUARRY"),
            GameConfig.getBuildingBuildableElevations("QUARRY")),
    LUMBER_CAMP("Lumber camp",
            GameConfig.getBuildingPopulationMaxBonus("LUMBER_CAMP"),
            GameConfig.getBuildingManpowerUpkeep("LUMBER_CAMP"),
            GameConfig.getBuildingCompleteAtProgress("LUMBER_CAMP"),
            GameConfig.getBuildingBuildableElevations("LUMBER_CAMP")),
    CARPENTRY("Carpentry",
            GameConfig.getBuildingPopulationMaxBonus("CARPENTRY"),
            GameConfig.getBuildingManpowerUpkeep("CARPENTRY"),
            GameConfig.getBuildingCompleteAtProgress("CARPENTRY"),
            GameConfig.getBuildingBuildableElevations("CARPENTRY")),
    RANCH("Ranch",
            GameConfig.getBuildingPopulationMaxBonus("RANCH"),
            GameConfig.getBuildingManpowerUpkeep("RANCH"),
            GameConfig.getBuildingCompleteAtProgress("RANCH"),
            GameConfig.getBuildingBuildableElevations("RANCH")),
    LEATHER_WORKER("Leather worker",
            GameConfig.getBuildingPopulationMaxBonus("LEATHER_WORKER"),
            GameConfig.getBuildingManpowerUpkeep("LEATHER_WORKER"),
            GameConfig.getBuildingCompleteAtProgress("LEATHER_WORKER"),
            GameConfig.getBuildingBuildableElevations("LEATHER_WORKER")),
    FISHERY("Fishery",
            GameConfig.getBuildingPopulationMaxBonus("FISHERY"),
            GameConfig.getBuildingManpowerUpkeep("FISHERY"),
            GameConfig.getBuildingCompleteAtProgress("FISHERY"),
            GameConfig.getBuildingBuildableElevations("FISHERY")),
    VILLAGE("Village",
            GameConfig.getBuildingPopulationMaxBonus("VILLAGE"),
            GameConfig.getBuildingManpowerUpkeep("VILLAGE"),
            GameConfig.getBuildingCompleteAtProgress("VILLAGE"),
            GameConfig.getBuildingBuildableElevations("VILLAGE")),
    TOWN("Town",
            GameConfig.getBuildingPopulationMaxBonus("TOWN"),
            GameConfig.getBuildingManpowerUpkeep("TOWN"),
            GameConfig.getBuildingCompleteAtProgress("TOWN"),
            GameConfig.getBuildingBuildableElevations("TOWN")),
    CITY("City",
            GameConfig.getBuildingPopulationMaxBonus("CITY"),
            GameConfig.getBuildingManpowerUpkeep("CITY"),
            GameConfig.getBuildingCompleteAtProgress("CITY"),
            GameConfig.getBuildingBuildableElevations("CITY"));

    private final int populationMaxBonus;

    private final int manpowerUpkeep;

    private final int completeAtProgress;

    private final String building;

    private final List<Elevation> buildableElevation;

    BuildingType(String building,
                 int populationMaxBonus,
                 int manpowerUpkeep,
                 int completeAtProgress,
                 Elevation[] buildableElevation) {
        this.building = building;
        this.populationMaxBonus = populationMaxBonus;
        this.manpowerUpkeep = manpowerUpkeep;
        this.completeAtProgress = completeAtProgress;
        this.buildableElevation = Arrays.stream(buildableElevation).toList();
    }

    public int getPopulationMaxBonus() {
        return populationMaxBonus;
    }

    public int getManpowerUpkeep() {
        return manpowerUpkeep;
    }

    public int getCompleteAtProgress() {
        return completeAtProgress;
    }

    public String getBuilding() {return building;}

    public List<Elevation> getBuildableElevation() {
        return buildableElevation;
    }
}
