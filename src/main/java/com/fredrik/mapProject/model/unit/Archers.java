package com.fredrik.mapProject.model.unit;

import com.fredrik.mapProject.model.battle.dice.Dice;
import com.fredrik.mapProject.model.databaseEntity.RegimentEntity;

public class Archers extends Unit{
    private int moralDamageBonus = 5;

    public Archers(RegimentEntity regiment, boolean isFortified) {
        super(regiment.getRegimentId(),
                regiment.getUnitType(),
                regiment.getUnitAmount(),
                regiment.getMoral(),
                regiment.getEquipmentModifier(),
                isFortified);
    }

    @Override
    public void battle(Unit opposingUnit) {

        Dice dice = Dice.D6;

        if (opposingUnit.isFortified()) {
            dice = Dice.D4;
        }

        int damage = rollForAttackDamage(dice);

        opposingUnit.reduceMoral(damage + moralDamageBonus);
        opposingUnit.incapacitateUnits(damage);
    }
}
