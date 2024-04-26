package com.fredrik.mapProject.gameRunDomain.model.building;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameRunDomain.Terrain;

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

    protected abstract double terrainModifier(Terrain terrain);

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
