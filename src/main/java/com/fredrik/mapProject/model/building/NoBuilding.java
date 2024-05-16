package com.fredrik.mapProject.model.building;

import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.map.terrain.Terrain;

public class NoBuilding extends Building {

    public NoBuilding(BuildingType type, int progress) {
        super(
                type,
                progress
        );
    }

    @Override
    public boolean processProduction(ManaEntity mana, Terrain terrain, MapCoordinates coordinates) {
        return false;
    }

}
