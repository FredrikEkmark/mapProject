package com.fredrik.mapProject.model.map;

import com.fredrik.mapProject.config.GameConfig;

public enum MapSizes {

    SMALL(GameConfig.getMapSizeWidth("SMALL"),
            GameConfig.getMapSizeHeight("SMALL"),
            GameConfig.getMapSizeMaxPlayers("SMALL")),
    MEDIUM(GameConfig.getMapSizeWidth("MEDIUM"),
            GameConfig.getMapSizeHeight("MEDIUM"),
            GameConfig.getMapSizeMaxPlayers("MEDIUM")),
    LARGE(GameConfig.getMapSizeWidth("LARGE"),
            GameConfig.getMapSizeHeight("LARGE"),
            GameConfig.getMapSizeMaxPlayers("LARGE")),
    XLARGE(GameConfig.getMapSizeWidth("XLARGE"),
            GameConfig.getMapSizeHeight("XLARGE"),
            GameConfig.getMapSizeMaxPlayers("XLARGE"));

    private final int width;
    private final int height;
    private final int maxPlayers;

    MapSizes(int width, int height, int maxPlayers) {
        this.width = width;
        this.height = height;
        this.maxPlayers = maxPlayers;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxPlayers() {return maxPlayers;}
}
