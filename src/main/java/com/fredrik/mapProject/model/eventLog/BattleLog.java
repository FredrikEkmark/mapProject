package com.fredrik.mapProject.model.eventLog;

import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.player.Player;

public record BattleLog(Player player, String logEntry, int loses) {
}
