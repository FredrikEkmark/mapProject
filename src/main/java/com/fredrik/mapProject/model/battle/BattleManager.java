package com.fredrik.mapProject.model.battle;

import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;
import com.fredrik.mapProject.model.databaseEntity.RegimentEntity;
import com.fredrik.mapProject.model.eventLog.BattleLog;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.unit.*;

import java.util.*;
import java.util.stream.Collectors;

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

            List<RegimentEntity> remainingRegiments = new ArrayList<>();
            List<RegimentEntity> removedRegiments = new ArrayList<>();
            for (RegimentEntity regiment:battleResult.defeatedArmy().getRegiments()) {
                if (regiment.getUnitAmount() < 10) {
                    removedRegiments.add(regiment);
                } else {
                    remainingRegiments.add(regiment);
                }
            }
            battleResult.defeatedArmy().setRegiments(remainingRegiments);
            gameMap.removeRegiments(removedRegiments);

            if (remainingRegiments.isEmpty()) {
                System.out.println("remove army");
                gameMap.removeArmy(battleResult.defeatedArmy());
            }

            removedRegiments.clear();
            remainingRegiments.clear();

            for (RegimentEntity regiment:battleResult.winningArmy().getRegiments()) {
                if (regiment.getUnitAmount() < 10) {
                    removedRegiments.add(regiment);
                } else {
                    remainingRegiments.add(regiment);
                }
            }
            gameMap.removeRegiments(removedRegiments);
            battleResult.winningArmy().setRegiments(remainingRegiments);

            if (remainingRegiments.isEmpty()) {
                gameMap.removeArmy(battleResult.winningArmy());
            }

            if (!armyQueue.isEmpty()) {
                defendingArmy = battleResult.winningArmy();
            }

            battleLogList.add(new BattleLog(battleResult.defeatedArmy().getOwner(), String.format(
                    "%s was defeated by %s on tile %d:%d;",
                    battleResult.defeatedArmy().getArmyName(),
                    battleResult.winningArmy().getArmyName(),
                    battleCoordinates.getX(),
                    battleCoordinates.getY()),
                    battleResult.defeatedArmiesLoses()));

            battleLogList.add(new BattleLog(battleResult.winningArmy().getOwner(), String.format(
                    "%s defeated %s on tile %d:%d;",
                    battleResult.winningArmy().getArmyName(),
                    battleResult.defeatedArmy().getArmyName(),
                    battleCoordinates.getX(),
                    battleCoordinates.getY()),
                    battleResult.winningArmiesLoses()));

            retreatingArmies.add(battleResult.defeatedArmy());
        }

        // Retreat
        List<MapTileEntity> possibleRetreatLocations = gameMap.getPossibleRetreatLocations(battleCoordinates, true);


        for (ArmyEntity army: retreatingArmies) {
            retreatArmy(army, gameMap, possibleRetreatLocations, armyLocationMap, battleLogList);
        }

        return battleLogList;
    }

    private static BattleResult battleArmies(ArmyEntity defendingArmy, ArmyEntity attackingArmy) {

        Map<UnitType, List<Unit>> stagedDefendingArmy = stageArmy(defendingArmy);
        Map<UnitType, List<Unit>> stagedAttackingArmy = stageArmy(attackingArmy);

        List<Unit> attackingUnitsOutOfBattle = new ArrayList<>(attackingArmy.regimentsSize());
        List<Unit> defendingUnitsOutOfBattle = new ArrayList<>(defendingArmy.regimentsSize());

        boolean defendingArmyRetreating = false;
        boolean attackingArmyRetreating = false;

        while (!defendingArmyRetreating && !attackingArmyRetreating) {
            List<Unit> attackingInfantry = stagedAttackingArmy.get(UnitType.INFANTRY);
            List<Unit> defendingInfantry = stagedDefendingArmy.get(UnitType.INFANTRY);
            List<Unit> attackingArchers = stagedAttackingArmy.get(UnitType.ARCHERS);
            List<Unit> defendingArchers = stagedDefendingArmy.get(UnitType.ARCHERS);
            List<Unit> attackingCavalries = stagedAttackingArmy.get(UnitType.CAVALRY);
            List<Unit> defendingCavalries = stagedDefendingArmy.get(UnitType.CAVALRY);

            for (int i = 0; i < 10; i++) {

                if (i == 0) {
                    cavalryActions(attackingCavalries, defendingCavalries, attackingArchers, defendingArchers, Flank.LEFT);
                } else if (i == 9) {
                    cavalryActions(attackingCavalries, defendingCavalries, attackingArchers, defendingArchers, Flank.RIGHT);
                }

                if (i > 0 && i < 9) {
                    infantryActions(i,
                            attackingInfantry,
                            defendingInfantry,
                            attackingArchers,
                            defendingArchers);
                }

                archersAction(i,
                        attackingInfantry,
                        defendingInfantry,
                        attackingArchers,
                        defendingArchers,
                        attackingCavalries,
                        defendingCavalries);
            }


            attackingArmyRetreating = updateArmyAndCheckForRetreat(attackingUnitsOutOfBattle, attackingInfantry, attackingArchers, attackingCavalries);
            defendingArmyRetreating = updateArmyAndCheckForRetreat(defendingUnitsOutOfBattle, defendingInfantry, defendingArchers, defendingCavalries);
        }

        int defendingArmyLoses = reArrangeArmyAndCountLoses(defendingArmyRetreating, defendingArmy, defendingUnitsOutOfBattle, stagedDefendingArmy);
        int attackingArmyLoses = reArrangeArmyAndCountLoses(attackingArmyRetreating, attackingArmy, attackingUnitsOutOfBattle, stagedAttackingArmy);

        if (attackingArmyRetreating) {
            return new BattleResult(defendingArmy, attackingArmy, defendingArmyLoses, attackingArmyLoses);
        }
        return new BattleResult(attackingArmy, defendingArmy, attackingArmyLoses, defendingArmyLoses);
    }

    private static int reArrangeArmyAndCountLoses(boolean lostBattle, ArmyEntity army, List<Unit> unitsOutOfBattle, Map<UnitType, List<Unit>> stagedArmy) {
        List<Unit> infantry = stagedArmy.get(UnitType.INFANTRY);
        List<Unit> archers = stagedArmy.get(UnitType.ARCHERS);
        List<Unit> cavalries = stagedArmy.get(UnitType.CAVALRY);

        unitsOutOfBattle.addAll(infantry);
        unitsOutOfBattle.addAll(archers);
        unitsOutOfBattle.addAll(cavalries);

        Map<UUID, RegimentEntity> regimentEntityMap = army.getRegiments().stream()
                .collect(Collectors.toMap(RegimentEntity::getRegimentId, regimentEntity -> regimentEntity));

        int moralBonus = 1;
        if (lostBattle) {
            moralBonus = -1;
        }

        int loses = 0;
        List<RegimentEntity> removedRegiments = new ArrayList<>();
        for (Unit unit : unitsOutOfBattle) {
            RegimentEntity regiment = regimentEntityMap.get(unit.getId());
            regiment.setUnitAmount(Math.max(0, unit.getActiveUnitAmount()));
            regiment.adjustMoral(moralBonus);
            loses += unit.getIncapacitatedUnits();
        }
        return loses;
    }

    private static boolean updateArmyAndCheckForRetreat(List<Unit> outOfBattle,
                                                        List<Unit> infantry,
                                                        List<Unit> archers,
                                                        List<Unit> cavalries) {
        List<Unit> allUnits = new ArrayList<>(infantry.size() + archers.size() + cavalries.size());
        allUnits.addAll(infantry);
        allUnits.addAll(archers);
        allUnits.addAll(cavalries);

        for (Unit unit : allUnits) {
            boolean unitOutOfBattle = false;
            if (unit.getActiveUnitAmount() < 10) {
                unitOutOfBattle = true;
            }
            if (unit.getUnitMoral() < 10) {
                unitOutOfBattle = true;
            }
            if (unitOutOfBattle) {
                outOfBattle.add(unit);
                switch (unit.getUnitType()) {
                    case INFANTRY -> infantry.remove(unit);
                    case ARCHERS -> archers.remove(unit);
                    case CAVALRY -> cavalries.remove(unit);
                }
            }
        }

        return infantry.isEmpty();
    }


    private static void archersAction(int iteration,
                                      List<Unit> attackingInfantry,
                                      List<Unit> defendingInfantry,
                                      List<Unit> attackingArchers,
                                      List<Unit> defendingArchers,
                                      List<Unit> attackingCavalries,
                                      List<Unit> defendingCavalries) {

        archerFireSalvo(iteration, attackingArchers, defendingInfantry, defendingArchers, defendingCavalries);

        archerFireSalvo(iteration, defendingArchers, attackingInfantry, attackingArchers, attackingCavalries);
    }

    private static void archerFireSalvo(int iteration, List<Unit> attackingArchers, List<Unit> defendingInfantry, List<Unit> defendingArchers, List<Unit> defendingCavalries) {
        if (attackingArchers.size() > iteration) {
            Unit attackingArcher = attackingArchers.get(iteration);

            if (defendingArchers.size() > iteration) {
                attackingArcher.battle(defendingArchers.get(iteration));
            }

            if (iteration == 0) {
                if (!defendingCavalries.isEmpty()) {
                    attackingArcher.battle(defendingCavalries.get(0));
                }
                if (defendingInfantry.size() > iteration + 1) {
                    attackingArcher.battle(defendingInfantry.get(iteration + 1));
                }
            } else if (iteration == 9) {
                if (defendingCavalries.size() > 1) {
                    attackingArcher.battle(defendingCavalries.get(1));
                }
                if (defendingInfantry.size() > iteration - 1) {
                    attackingArcher.battle(defendingInfantry.get(iteration - 1));
                }
            } else {
                if (defendingInfantry.size() > iteration + 1) {
                    attackingArcher.battle(defendingInfantry.get(iteration + 1));
                }
                if (defendingInfantry.size() > iteration) {
                    attackingArcher.battle(defendingInfantry.get(iteration));
                }
                if (defendingInfantry.size() > iteration - 1) {
                    attackingArcher.battle(defendingInfantry.get(iteration - 1));
                }
            }
        }
    }

    private static void infantryActions(int iteration,
                                        List<Unit> attackingInfantry,
                                        List<Unit> defendingInfantry,
                                        List<Unit> attackingArchers,
                                        List<Unit> defendingArchers) {
        int i = iteration - 1;

        if (attackingInfantry.size() > i) {
            Unit infantry = attackingInfantry.get(i);
            if (defendingInfantry.size() > i) {
                infantry.battle(defendingInfantry.get(i));
            } else if (defendingArchers.size() > i) {
                infantry.battle(defendingArchers.get(i));
            }
        } else if (defendingInfantry.size() > i) {
            Unit infantry = defendingInfantry.get(i);
            if (attackingArchers.size() > i) {
                infantry.battle(attackingArchers.get(i));
            }
        }
    }

    private static void cavalryActions(List<Unit> attackingCavalries,
                                       List<Unit> defendingCavalries,
                                       List<Unit> attackingArchers,
                                       List<Unit> defendingArchers,
                                       Flank flank) {

        if (flank == Flank.LEFT) {
            if (!attackingCavalries.isEmpty()) {
                if (defendingCavalries.isEmpty()) {
                    if (!defendingArchers.isEmpty()) {
                        attackingCavalries.get(0).battle(defendingArchers.get(0));
                    }
                } else {
                    attackingCavalries.get(0).battle(defendingCavalries.get(0));
                }
            } else if (!defendingCavalries.isEmpty()) {
                if (!attackingArchers.isEmpty()) {
                    defendingCavalries.get(0).battle(attackingArchers.get(0));
                }
            }
        } else if (flank == Flank.RIGHT) {
            if (attackingCavalries.size() > 1) {
                if (defendingCavalries.size() > 1) {
                    attackingCavalries.get(1).battle(defendingCavalries.get(1));
                } else {
                    if (defendingArchers.size() >= 10) {
                        attackingCavalries.get(1).battle(defendingArchers.get(9));
                    } else if (!defendingArchers.isEmpty()) {
                        attackingCavalries.get(1).battle(defendingArchers.getLast());
                    }
                }
            } else if (defendingCavalries.size() > 1) {
                if (attackingArchers.size() >= 10) {
                    defendingCavalries.get(1).battle(attackingArchers.get(9));
                } else if (!attackingArchers.isEmpty()) {
                    defendingCavalries.get(1).battle(attackingArchers.getLast());
                }
            }
        }
    }

    private static Map<UnitType, List<Unit>> stageArmy(ArmyEntity army) {

        HashMap stagedArmy = new HashMap<>();

        List<Infantry> infantry = new LinkedList<>();
        List<Archers> archers = new LinkedList<>();
        List<Cavalry> cavalry = new LinkedList<>();

        for (RegimentEntity regiment: army.getRegiments()) {
            switch (regiment.getUnitType()) {
                case INFANTRY -> infantry.add(new Infantry(regiment, army.isFortified()));
                case ARCHERS -> archers.add(new Archers(regiment, army.isFortified()));
                case CAVALRY -> cavalry.add(new Cavalry(regiment, army.isFortified()));
            }
        }

        stagedArmy.put(UnitType.INFANTRY, infantry);
        stagedArmy.put(UnitType.ARCHERS, archers);
        stagedArmy.put(UnitType.CAVALRY, cavalry);

        return stagedArmy;
    }

    private static void retreatArmy(ArmyEntity army,
                                    GameMapManager gameMap,
                                    List<MapTileEntity> possibleRetreatLocations,
                                    Map<MapCoordinates, ArmyLocation> armyLocationMap, List<BattleLog> battleLogList) {


        List<MapTileEntity> armyRetreatLocations = new ArrayList<>();

        for (MapTileEntity possibleRetreatLocation : possibleRetreatLocations) {
            ArmyLocation armyOnLocation = armyLocationMap.get(possibleRetreatLocation.getMapTileId().getCoordinates());

            if (armyOnLocation == null) {
                armyRetreatLocations.add(possibleRetreatLocation);
            } else if (armyOnLocation.armies().size() == 1 &&
                        armyOnLocation.armies().get(0).getOwner().equals(army.getOwner())) {
                armyRetreatLocations.add(possibleRetreatLocation);
            }
        }

        if (armyRetreatLocations.isEmpty()) {
            battleLogList.add(new BattleLog(army.getOwner(), String.format(
                    "%s was dissolved since it had no avenue of retreat;",
                    army.getArmyName()),
                    0)); // toDo consider if this should be a penalty to but damage from battle is already applied
            gameMap.removeArmy(army);
        }

        MapTileEntity retreatLocation = armyRetreatLocations.get(random.nextInt(0, armyRetreatLocations.size()));
        ArmyLocation armyOnRetreatLocation = armyLocationMap.get(retreatLocation.getMapTileId().getCoordinates());

        if (armyOnRetreatLocation == null) {
            battleLogList.add(new BattleLog(army.getOwner(), String.format(
                    "%s retreated to tile %d:%d;",
                    army.getArmyName(),
                    retreatLocation.getMapTileId().getCoordinates().getX(),
                    retreatLocation.getMapTileId().getCoordinates().getY()),
                    0)); // toDo consider if this should be a penalty to but damage from battle is already applied
            gameMap.moveArmy(army, retreatLocation.getMapTileId().getCoordinates());
        } else if (armyOnRetreatLocation.armies().size() == 1 && armyOnRetreatLocation.armies().get(0).getOwner().equals(army.getOwner())) {
            battleLogList.add(new BattleLog(army.getOwner(), String.format(
                    "%s retreated to tile %d:%d and was absorbed by %s;",
                    army.getArmyName(),
                    retreatLocation.getMapTileId().getCoordinates().getX(),
                    retreatLocation.getMapTileId().getCoordinates().getY(),
                    armyOnRetreatLocation.armies().get(0).getArmyName()),
                    0)); // toDo consider if this should be a penalty to but damage from battle is already applied
            System.out.println(armyOnRetreatLocation.mapCoordinates().getX() + ":" + armyOnRetreatLocation.mapCoordinates().getX() );
            armyOnRetreatLocation.armies().get(0).getRegiments().addAll(army.getRegiments());
            gameMap.removeArmy(army);
        }
    }
}
