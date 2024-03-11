package com.fredrik.mapProject.gameSetupDomain;

public enum MapSizes {

    SMALL(50, 25),
    MEDIUM(100, 50),
    LARGE(150, 75);

    private final int x;
    private final int y;

    MapSizes(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
