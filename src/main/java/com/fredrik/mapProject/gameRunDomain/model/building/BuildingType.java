package com.fredrik.mapProject.gameRunDomain.model.building;

public enum BuildingType {

    NONE(0,0, 0),
    FARM(100, 100, 200),
    GRANARY(100, 100, 200),
    QUARRY(100, 100, 200),
    LUMBER_CAMP(100, 100, 200),
    CARPENTRY(100, 100, 200),
    RANCH(100, 100, 200),
    LEATHER_WORKER(100, 100, 200),
    FISHERY(100, 100, 200),
    VILLAGE(1000, 300, 200),
    TOWN(5000, 1000, 200),
    CITY(15000, 3000, 200);

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
