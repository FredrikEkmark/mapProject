package com.fredrik.mapProject.gamePlayDomain;

public enum Player {

    NONE(0, "white", "#ffffff" ),
    PLAYER_ONE(1, "red", "#ff0000"),
    PLAYER_TWO(2, "blue", "#0000ff"),
    PLAYER_THREE(3, "green", "#00ff00"),
    PLAYER_FOUR(4, "yellow", "#ffff00"),
    PLAYER_FIVE(5, "orange", "#ffa500"),
    PLAYER_SIX(6, "purple", "#800080"),
    PLAYER_SEVEN(7, "cyan", "#00ffff"),
    PLAYER_EIGHT(8, "magenta", "#ff00ff"),
    PLAYER_NINE(9, "lime", "#00ff00"),
    PLAYER_TEN(10, "teal", "#008080"),
    PLAYER_ELEVEN(11, "pink", "#ffc0cb"),
    PLAYER_TWELVE(12, "brown", "#a52a2a");

    private int number;
    private String color;
    private String colorHex;

    Player(int number, String color, String colorHex) {
        this.number = number;
        this.color = color;
        this.colorHex = colorHex;
    }

    public String getColor() {
        return color;
    }

    public String getColorHex() {
        return colorHex;
    }
    public int number() {
        return number;
    }
}
