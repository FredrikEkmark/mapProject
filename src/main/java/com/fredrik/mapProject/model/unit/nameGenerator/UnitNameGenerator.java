package com.fredrik.mapProject.model.unit.nameGenerator;

import java.util.Random;

public class UnitNameGenerator {

    private static final Random random = new Random();
    private static final String[] ROMAN_NUMERALS = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};
    private static final int[] DECIMAL_VALUES = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
    private static final String[] NAME_IDENTIFIERS = {
            "Wolf",
            "Hawk",
            "Lion",
            "Tiger",
            "Eagle",
            "Bear",
            "Dragon",
            "Phoenix",
            "Raven",
            "Griffin",
            "Panther",
            "Thunder",
            "Storm",
            "Viper",
            "Falcon",
            "Spartan",
            "Viking",
            "Goliath",
            "Titan",
            "Pegasus",
            "Jaguar",
            "Centurion",
            "Bison",
            "Cobra",
            "Hydra",
            "Scorpion",
            "Ram",
            "Sentinel",
            "Wolverine",
            "Olympian",
            "Atlas",
            "Mercury",
            "Venus",
            "Mars",
            "Jupiter",
            "Saturn",
            "Uranus",
            "Neptune",
            "Pluto"
    };


    public static String getGeneratedArmyName(int iteration) {

        String armyNumber = intToRoman(iteration) + addOrdinal(iteration);

        return String.format("%s Army", armyNumber);
    }

    public static String getGeneratedRegimentName(int iteration) {

        String regimentNumber = intToRoman(iteration) + addOrdinal(iteration);
        String nameIdentifier = getNameIdentifier();

        return String.format("%s Regiment %s", regimentNumber, nameIdentifier);
    }

    private static String getNameIdentifier() {
        return NAME_IDENTIFIERS[random.nextInt(0, NAME_IDENTIFIERS.length)];
    }


    private static String intToRoman(int num) {
        if (num <= 0 || num >= 4000) {
            throw new IllegalArgumentException("Input should be in the range from 1 to 3999");
        }

        StringBuilder stringBuilder = new StringBuilder();
        int index = DECIMAL_VALUES.length - 1; // Start from the largest decimal value

        while (num > 0) {
            if (num >= DECIMAL_VALUES[index]) {
                stringBuilder.append(ROMAN_NUMERALS[index]);
                num -= DECIMAL_VALUES[index];
            } else {
                index--; // Move to a smaller decimal value
            }
        }

        return stringBuilder.toString();
    }

    private static String addOrdinal(int num) {
        switch (num) {
            case 1, 11 -> {
                return "ˢᵗ";
            }
            case 2, 12 -> {
                return "ⁿᵈ";
            }
            case 3, 13 -> {
                return "ʳᵈ";
            }
            default -> {
                return "ᵗʰ";
            }
        }
    }
}
