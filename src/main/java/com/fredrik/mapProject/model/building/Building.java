package com.fredrik.mapProject.model.building;

import com.fredrik.mapProject.config.GameConfig;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.map.terrain.Terrain;

public abstract class Building {

    private final BuildingType type;

    private int progress;

    private String eventLogEntry;

    public Building(BuildingType type, int progress) {
        this.type = type;
        this.progress = progress;
        this.eventLogEntry = "";
    }

    public abstract boolean processProduction(ManaEntity mana, Terrain terrain, MapCoordinates coordinates);

    protected double terrainModifier(Terrain terrain) {

        double terrainModifier = 1.0;

        terrainModifier += GameConfig.getBuildingElevationModifier(getType(), terrain.getElevation());
        terrainModifier += GameConfig.getBuildingTemperatureModifier(getType(), terrain.getTemperature());
        terrainModifier += GameConfig.getBuildingPrecipitationModifier(getType(), terrain.getPrecipitation());

        return terrainModifier;
    }

    public BuildingType getType() {
        return type;
    }

    public int getProgress() {return progress;}

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void addProgress(int added) {
        progress += added;
    }

    public void damage(int damage) {
        progress -= damage;
        if (progress < 0) {
            progress = 0;
        }
    }

    public boolean isCompleted() {
        return progress >= type.getCompleteAtProgress();
    }

    public String getEventLogEntry() {
        return eventLogEntry;
    }

    public void setEventLogEntry(String eventLogEntry) {
        this.eventLogEntry = eventLogEntry;
    }

}
