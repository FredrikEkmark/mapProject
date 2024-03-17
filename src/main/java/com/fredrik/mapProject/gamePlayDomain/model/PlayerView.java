package com.fredrik.mapProject.gamePlayDomain.model;

import com.fredrik.mapProject.gameSetupDomain.MapSizes;

import java.util.List;

public class PlayerView {

    private final String playerName;
    private final MapSizes mapSize;
    private MapTile[][] map;

    public PlayerView(String playerName, MapSizes mapSize, List<MapTile> tileList) {
        this.playerName = playerName;
        this.mapSize = mapSize;
        this.map = map;
    }
}
