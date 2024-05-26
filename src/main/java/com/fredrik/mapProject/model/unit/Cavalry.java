package com.fredrik.mapProject.model.unit;

import com.fredrik.mapProject.model.battle.dice.Dice;
import com.fredrik.mapProject.model.databaseEntity.RegimentEntity;

public class Cavalry extends Unit {

    private int moralDamageBonus = 15;
    public Cavalry(RegimentEntity regiment, boolean isFortified) {
        super(regiment.getRegimentId(),
                regiment.getUnitType(),
                regiment.getUnitAmount(),
                regiment.getMoral(),
                regiment.getEquipmentModifier(),
                isFortified);
    }

    @Override
    public void battle(Unit opposingUnit) {

        int opposingDamage = 0;
        int thisUnitsDamage = 0;
        if (opposingUnit.getUnitType() == UnitType.CAVALRY) {
            opposingDamage = opposingUnit.rollForAttackDamage(Dice.D6);
            thisUnitsDamage = rollForAttackDamage(Dice.D6);
        } else if (opposingUnit.getUnitType() == UnitType.ARCHERS) {
            Dice dice = Dice.D20;
            if (opposingUnit.isFortified()) {
                dice = Dice.D6;
            }
            thisUnitsDamage = rollForAttackDamage(dice);
        }

        opposingUnit.incapacitateUnits(thisUnitsDamage);
        incapacitateUnits(opposingDamage);

        if (opposingDamage > thisUnitsDamage) {
            reduceMoral(opposingDamage + moralDamageBonus);
            opposingUnit.reduceMoral(thisUnitsDamage);
        } else {
            reduceMoral(opposingDamage);
            opposingUnit.reduceMoral(thisUnitsDamage + moralDamageBonus);
        }
    }
}
