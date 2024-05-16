package com.fredrik.mapProject.model.battle;

import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record ArmyLocation(List<ArmyEntity> armies, MapCoordinates mapCoordinates) {

    public ArmyLocation(List<ArmyEntity> armies, MapCoordinates mapCoordinates) {
        this.armies = combineArmiesWithSameOwner(armies);
        this.mapCoordinates = mapCoordinates;
    }

    private List<ArmyEntity> combineArmiesWithSameOwner(List<ArmyEntity> armies) {
        Map<Player, List<ArmyEntity>> combinedArmies = new HashMap<>();

        for (ArmyEntity army : armies) {
            combinedArmies.computeIfAbsent(army.getOwner(), k -> new ArrayList<>()).add(army);
        }

        List<ArmyEntity> result = new ArrayList<>();


        return result;
    }

    public void addArmy(ArmyEntity army) {
        armies();
    }
}
