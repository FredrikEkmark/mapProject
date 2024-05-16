package com.fredrik.mapProject.model.battle;

import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;

public record BattleResult(ArmyEntity winningArmy, ArmyEntity defeatedArmy) {
}
