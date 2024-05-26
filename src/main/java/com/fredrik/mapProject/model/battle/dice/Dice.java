package com.fredrik.mapProject.model.battle.dice;

public enum Dice {

    D20(20),
    D12(12),
    D6(6),
    D4(4);

    private int bounds;

    Dice(int bounds) {
        this.bounds = bounds + 1;
    }

    public int getBounds() {
        return bounds;
    }
}
