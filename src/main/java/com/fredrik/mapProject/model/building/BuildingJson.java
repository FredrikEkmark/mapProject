package com.fredrik.mapProject.model.building;

public class BuildingJson {

    private BuildingType type;

    private int progress;

    public BuildingJson() {
    }

    public BuildingType getType() {
        return type;
    }

    public void setType(BuildingType type) {
        this.type = type;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
