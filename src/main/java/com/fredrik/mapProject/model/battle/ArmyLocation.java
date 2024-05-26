package com.fredrik.mapProject.model.battle;

import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ArmyLocation(List<ArmyEntity> armies, MapCoordinates mapCoordinates) {
}
