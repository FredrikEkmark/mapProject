package com.fredrik.mapProject.model.unit;

import com.fredrik.mapProject.model.battle.dice.Dice;
import com.fredrik.mapProject.model.databaseEntity.RegimentEntity;

public class Infantry extends Unit {
    private int moralDamageBonus = 10;
    public Infantry(RegimentEntity regiment, boolean isFortified) {
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
        Dice attackingDice = Dice.D12;
        if (opposingUnit.isFortified()) {
            attackingDice = Dice.D6;
        }
        if (opposingUnit.getUnitType() == UnitType.INFANTRY) {
            opposingDamage = opposingUnit.rollForAttackDamage(Dice.D12);
            thisUnitsDamage = rollForAttackDamage(attackingDice);
        } else if (opposingUnit.getUnitType() == UnitType.ARCHERS) {
            thisUnitsDamage = rollForAttackDamage(attackingDice);
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
