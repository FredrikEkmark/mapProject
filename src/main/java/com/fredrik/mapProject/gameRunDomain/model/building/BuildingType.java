package com.fredrik.mapProject.gameRunDomain.model.building;

public enum BuildingType {

    NONE("none",0,0, 0),
    FARM("Farm",50, 100, 400),
    GRANARY("Granary",0, 50, 500),
    QUARRY("Quarry",0, 100, 500),
    LUMBER_CAMP("Lumber camp",50, 100, 400),
    CARPENTRY("Carpentry",0, 50, 500),
    RANCH("Ranch",50, 100, 500),
    LEATHER_WORKER("Leather worker",0, 50, 500),
    FISHERY("Fishery",50, 100, 400),
    VILLAGE("Village",500, 300, 800),
    TOWN("Town",2000, 600, 2000),
    CITY("City",5000, 1000, 5000);

    private final int populationMaxBonus;

    private final int manpowerUpkeep;

    private final int completeAtProgress;

    private final String building;

    BuildingType(String building, int populationMaxBonus, int manpowerUpkeep, int completeAtProgress) {
        this.building = building;
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

    public String getBuilding() {return building;}
}
