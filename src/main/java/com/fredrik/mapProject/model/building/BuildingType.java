package com.fredrik.mapProject.model.building;

import com.fredrik.mapProject.model.map.terrain.Elevation;

import java.util.Arrays;
import java.util.List;

public enum BuildingType {

    NONE("none",0,0, 0, new Elevation[] {}),
    FARM("Farm",50, 100, 400, new Elevation[] {Elevation.LOWLANDS, Elevation.HIGHLANDS}),
    GRANARY("Granary",0, 50, 500, new Elevation[] {Elevation.LOWLANDS, Elevation.HIGHLANDS}),
    QUARRY("Quarry",0, 100, 500, new Elevation[] {Elevation.LOWLANDS, Elevation.HIGHLANDS, Elevation.MOUNTAIN}),
    LUMBER_CAMP("Lumber camp",50, 100, 400, new Elevation[] {Elevation.LOWLANDS, Elevation.HIGHLANDS}),
    CARPENTRY("Carpentry",0, 50, 500, new Elevation[] {Elevation.LOWLANDS, Elevation.HIGHLANDS}),
    RANCH("Ranch",50, 150, 500, new Elevation[] {Elevation.LOWLANDS, Elevation.HIGHLANDS, Elevation.MOUNTAIN}),
    LEATHER_WORKER("Leather worker",0, 50, 500, new Elevation[] {Elevation.LOWLANDS, Elevation.HIGHLANDS}),
    FISHERY("Fishery",50, 100, 400, new Elevation[] {Elevation.SHALLOW, Elevation.DEEP}),
    VILLAGE("Village",500, 300, 800, new Elevation[] {Elevation.LOWLANDS, Elevation.HIGHLANDS}),
    TOWN("Town",2000, 600, 2000, new Elevation[] {Elevation.LOWLANDS, Elevation.HIGHLANDS}),
    CITY("City",5000, 1000, 5000, new Elevation[] {Elevation.LOWLANDS, Elevation.HIGHLANDS});

    private final int populationMaxBonus;

    private final int manpowerUpkeep;

    private final int completeAtProgress;

    private final String building;

    private List<Elevation> buildableElevation;

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
