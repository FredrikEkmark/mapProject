package com.fredrik.mapProject.gameRunDomain.model.building;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gameRunDomain.Terrain;

public abstract class Building {

    private final BuildingType type;

    private int progress;

    public Building(BuildingType type, int progress) {
        this.type = type;
        this.progress = progress;
    }

    public abstract boolean processProduction(ManaEntity mana, Terrain terrain);

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

    public boolean isCompleted() {
        return progress >= type.getCompleteAtProgress();
    }
}
