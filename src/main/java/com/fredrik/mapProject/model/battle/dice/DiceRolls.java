package com.fredrik.mapProject.model.battle.dice;

import java.util.Random;

public class DiceRolls {

    private static final Random random = new Random();

    public static int rollDice(Dice dice) {
        return random.nextInt(1, dice.getBounds());
    }
}
