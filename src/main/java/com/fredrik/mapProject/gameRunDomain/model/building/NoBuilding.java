package com.fredrik.mapProject.gameRunDomain.model.building;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameRunDomain.Terrain;

public class NoBuilding extends Building {

    public NoBuilding(BuildingType type, int progress) {
        super(type, progress);
    }

    @Override
    public boolean processProduction(ManaEntity mana, Terrain terrain, MapCoordinates coordinates) {
        return false;
    }

    @Override
    protected double terrainModifier(Terrain terrain) {
        return 1;
    }
}
