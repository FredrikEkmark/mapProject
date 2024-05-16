package com.fredrik.mapProject.model.battle;

import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;

import java.util.List;

public record Battle(List<ArmyEntity> armies, MapTileEntity mapTile) {
}
