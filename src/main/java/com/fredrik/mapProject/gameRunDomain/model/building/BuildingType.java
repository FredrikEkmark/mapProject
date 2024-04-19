package com.fredrik.mapProject.gameRunDomain.model.building;

public enum BuildingType {

    NONE(0,0, 0),
    FARM(50, 100, 400),
    GRANARY(0, 50, 500),
    QUARRY(0, 100, 500),
    LUMBER_CAMP(50, 100, 400),
    CARPENTRY(0, 50, 500),
    RANCH(50, 100, 500),
    LEATHER_WORKER(0, 50, 500),
    FISHERY(50, 100, 400),
    VILLAGE(500, 300, 800),
    TOWN(2000, 600, 2000),
    CITY(5000, 1000, 5000);

    private final int populationMaxBonus;

    private final int manpowerUpkeep;

    private final int completeAtProgress;

    BuildingType(int populationMaxBonus, int manpowerUpkeep, int completeAtProgress) {
        this.populationMaxBonus = populationMaxBonus;
        this.manpowerUpkeep = manpowerUpkeep;
        this.completeAtProgress = completeAtProgress;
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
}
