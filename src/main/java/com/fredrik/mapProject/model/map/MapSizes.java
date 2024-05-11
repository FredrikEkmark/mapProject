package com.fredrik.mapProject.model.map;

public enum MapSizes {

    SMALL(50, 25, 4),
    MEDIUM(100, 51, 6),
    LARGE(200, 101, 8),
    XLARGE(400, 201, 12);

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
