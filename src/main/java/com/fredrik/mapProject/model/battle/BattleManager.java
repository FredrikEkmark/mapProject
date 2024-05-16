package com.fredrik.mapProject.model.battle;

import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;
import com.fredrik.mapProject.model.eventLog.BattleLog;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;

import java.util.*;

public class BattleManager {

    private static final Random random = new Random();

    public BattleManager() {
    }

    public static List<BattleLog> processBattle(Battle battle,
                                                GameMapManager gameMap,
                                                Map<MapCoordinates,
                                                ArmyLocation> armyLocationMap)
    {

        MapCoordinates battleCoordinates = battle.mapTile().getMapTileId().getCoordinates();

        List<BattleLog> battleLogList = new ArrayList<>(battle.armies().size());
        List<ArmyEntity> retreatingArmies = new ArrayList<>();

        // Battle
        Queue<ArmyEntity> armyQueue = new LinkedList<>(battle.armies());

        if (armyQueue.size() < 2) {
            return battleLogList;
        }

        ArmyEntity defendingArmy = armyQueue.poll();

        while (!armyQueue.isEmpty()) {

            ArmyEntity attackingArmy = armyQueue.poll();

            BattleResult battleResult = battleArmies(defendingArmy, attackingArmy);

            if (!armyQueue.isEmpty()) {

                battleLogList.add(new BattleLog(battleResult.winningArmy().getOwner(), String.format(
                        "%s defeated %s on tile %d:%d;",
                        battleResult.winningArmy().getArmyName(),
                        battleResult.defeatedArmy().getArmyName(),
                        battleCoordinates.getX(),
                        battleCoordinates.getY()
                )));

                battleLogList.add(new BattleLog(battleResult.defeatedArmy().getOwner(), String.format(
                        "%s was defeated by %s on tile %d:%d;",
                        battleResult.defeatedArmy().getArmyName(),
                        battleResult.winningArmy().getArmyName(),
                        battleCoordinates.getX(),
                        battleCoordinates.getY()
                )));
            } else {
                battleLogList.add(new BattleLog(battleResult.defeatedArmy().getOwner(), String.format(
                        "%s was defeated by %s on tile %d:%d;",
                        battleResult.defeatedArmy().getArmyName(),
                        battleResult.winningArmy().getArmyName(),
                        battleCoordinates.getX(),
                        battleCoordinates.getY()
                )));
                retreatingArmies.add(battleResult.defeatedArmy());
                defendingArmy = battleResult.winningArmy();
            }
        }

        // Retreat
        List<MapTileEntity> possibleRetreatLocations = gameMap.getPossibleRetreatLocations(battleCoordinates, true);


        for (ArmyEntity army: retreatingArmies) {
            retreatArmy(army, gameMap, possibleRetreatLocations, armyLocationMap, battleLogList);
        }

        return battleLogList;
    }

    private static BattleResult battleArmies(ArmyEntity defendingArmy, ArmyEntity attackingArmy) {

        // todo write actual logic here

        return new BattleResult(defendingArmy, attackingArmy);
    }

    private static void retreatArmy(ArmyEntity army,
                                    GameMapManager gameMap,
                                    List<MapTileEntity> possibleRetreatLocations,
                                    Map<MapCoordinates, ArmyLocation> armyLocationMap, List<BattleLog> battleLogList) {

        List<MapTileEntity> armyRetreatLocations = new ArrayList<>();

        for (MapTileEntity possibleRetreatLocation : possibleRetreatLocations) {
            ArmyLocation armyOnLocation = armyLocationMap.get(possibleRetreatLocation.getMapTileId().getCoordinates());

            if (armyOnLocation.armies().isEmpty()) {
                armyRetreatLocations.add(possibleRetreatLocation);
            } else if (armyOnLocation.armies().size() == 1 &&
                        armyOnLocation.armies().get(0).getOwner().equals(army.getOwner())) {
                armyRetreatLocations.add(possibleRetreatLocation);
            }
        }

        if (armyRetreatLocations.isEmpty()) {
            battleLogList.add(new BattleLog(army.getOwner(), String.format(
                    "%s was dissolved since it had no avenue of retreat;",
                    army.getArmyName()
            )));
            gameMap.removeArmy(army);
        }

        MapTileEntity retreatLocation = armyRetreatLocations.get(random.nextInt(0, armyRetreatLocations.size()));
        ArmyLocation armyOnRetreatLocation = armyLocationMap.get(retreatLocation.getMapTileId().getCoordinates());

        if (armyOnRetreatLocation.armies().isEmpty()) {
            battleLogList.add(new BattleLog(army.getOwner(), String.format(
                    "%s retreated to tile %d:%d;",
                    army.getArmyName(),
                    retreatLocation.getMapTileId().getCoordinates().getX(),
                    retreatLocation.getMapTileId().getCoordinates().getY()
            )));
            gameMap.moveArmy(army, retreatLocation.getMapTileId().getCoordinates());
        } else if (armyOnRetreatLocation.armies().size() == 1 && armyOnRetreatLocation.armies().get(0).getOwner().equals(army.getOwner())) {
            battleLogList.add(new BattleLog(army.getOwner(), String.format(
                    "%s retreated to tile %d:%d and was absorbed by %s;",
                    army.getArmyName(),
                    retreatLocation.getMapTileId().getCoordinates().getX(),
                    retreatLocation.getMapTileId().getCoordinates().getY(),
                    armyOnRetreatLocation.armies().get(0).getArmyName()
            )));
            armyOnRetreatLocation.armies().get(0).getRegiments().addAll(army.getRegiments());
            gameMap.removeArmy(army);
        }
    }
}
