package com.fredrik.mapProject.gameSetupDomain;

public enum MapSizes {

    SMALL(50, 25),
    MEDIUM(100, 50),
    LARGE(150, 75),
    XLARGE(400, 200);

    private final int width;
    private final int height;

    MapSizes(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
