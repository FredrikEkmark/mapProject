package com.fredrik.mapProject.model.turnChange;

import com.fredrik.mapProject.config.GameConfig;
import com.fredrik.mapProject.model.battle.ArmyLocation;
import com.fredrik.mapProject.model.battle.Battle;
import com.fredrik.mapProject.model.eventLog.BattleLog;
import com.fredrik.mapProject.model.battle.BattleManager;
import com.fredrik.mapProject.model.eventLog.SecondaryEventLogEntry;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.building.Building;
import com.fredrik.mapProject.model.building.BuildingType;
import com.fredrik.mapProject.model.databaseEntity.*;
import com.fredrik.mapProject.model.event.Event;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;

import java.util.*;

public class TurnChange {

    private final GameSetupEntity gameSetup;

    private final GameMapManager gameMap;

    private List<EventEntity> eventEntityList;

    private final List<ManaEntity> manaList;

    private final List<EventLogEntity> eventLog;

    private boolean updated = false;

    public TurnChange(GameSetupEntity gameSetup, GameMapManager gameMap, List<EventEntity> eventEntityList, List<ManaEntity> manaList) {
        this.gameSetup = gameSetup;
        this.gameMap = gameMap;
        this.eventEntityList = eventEntityList;
        this.manaList = manaList;
        this.eventLog = new ArrayList<>();
    }

    public void update() {
        if (updated) {
            System.out.println("Turn already updated");
            return;
        }

        // new list with only the persistent events
        List<EventEntity> updatedEventEntityList = new ArrayList<>(eventEntityList.size());
        // String list of the output from event processing
        Map<String, List<String>> eventLogEntries = new HashMap<>();
        for (ManaEntity mana : manaList) {
            eventLogEntries.put(mana.getPlayerNr().name(), new ArrayList<>());
        }

        // PROCESSING ALL THE EVENTS
        processEventsEntityList(updatedEventEntityList, eventLogEntries);

        // PROCESS BATTLES
        processArmies(eventLogEntries);

        // PROCESSING PLAYER LIST
        processPlayers(eventLogEntries);

        eventEntityList = updatedEventEntityList;
        gameSetup.setTurn(gameSetup.getTurn() + 1); // ticks the turn up one
        updated = true;
        gameSetup.setUpdating(false);
        System.out.printf("""
                GameId: %s
                Turn has been updated
                """, gameSetup.getId());
    }

    // Processing functions

    private void processEventsEntityList(List<EventEntity> updatedEventEntityList, Map<String, List<String>> eventLogEntries) {

        // Sort list by EventType
        eventEntityList.sort(Comparator.comparingInt(eventEntity -> eventEntity.getEvent().getEventType().ordinal()));

        for (EventEntity eventEntity: eventEntityList) {

            Event event = eventEntity.getEvent();

            ManaEntity mana = manaList.stream()
                    .filter(m -> m.getPlayerNr().name().equals(eventEntity.getPlayerNr().name()))
                    .findFirst()
                    .orElse(null);

            // PROCESS EVENT
            boolean success = event.processEvent(mana, gameMap);
            // PROCESS EVENT

            String eventLogEntry = event.getEventLogEntry();
            eventLogEntries.get(event.getPlayerNr().name()).add(eventLogEntry);

            if (!success) {
                System.out.println("Event Not Processed");
            }

            if (event.isAggression()) {
                SecondaryEventLogEntry secondaryEventLogEntry = event.getSecondaryEventLogEntry();
                eventLogEntries.get(secondaryEventLogEntry.player().name()).add(secondaryEventLogEntry.eventLogEntry());
            }

            if (event.isPersistent()) {
                eventEntity.setCost(event.stringifyCost());
                eventEntity.setTurn(eventEntity.getTurn() + 1);
                updatedEventEntityList.add(eventEntity.clone());
            }
        }
    }

    private void processPlayers(Map<String, List<String>> eventLogEntries) {

        for (ManaEntity mana: manaList) {
            processPlayerMana(mana, eventLogEntries.get(mana.getPlayerNr().name()));

            StringBuilder eventLogText = new StringBuilder();
            int eventLogIndex = 0;

            for (String eventLogEntry: eventLogEntries.get(mana.getPlayerNr().name())) {
                if (eventLogEntry != null && eventLogText.length() + eventLogEntry.length() > 255) {
                    eventLog.add(new EventLogEntity(
                            eventLogText.toString(),
                            gameSetup.getId(),
                            mana.getPlayerNr(),
                            gameSetup.getTurn(),
                            eventLogIndex
                            ));

                    eventLogIndex++;
                    eventLogText = new StringBuilder();
                }

                eventLogText.append(eventLogEntry);
            }

            eventLog.add(new EventLogEntity(
                    eventLogText.toString(),
                    gameSetup.getId(),
                    mana.getPlayerNr(),
                    gameSetup.getTurn(),
                    eventLogIndex
            ));
        }
    }

    private void processPlayerMana(ManaEntity mana, List<String> playerEventLogEntries) {

        List<MapTileEntity> playerMap = gameMap.getTilesWithPlayer(mana.getPlayerNr());
        List<ArmyEntity> playerArmies = gameMap.getPlayerArmies(mana.getPlayerNr());

        // use the unused manpower from last turn for food production
        int excessManpowerFromTurn = mana.withdrawAllManpower();

        // reset the manpower for next turn
        mana.depositManpower(mana.getPopulation());

        mana.setPopulationMax(0);
        mana.setProtectedFood(0);
        int tilePopulationMaxBonus = 100;

        // Run all player armies for upkeep cost
        List<RegimentEntity> removedRegiments = new ArrayList<>();
        for (ArmyEntity army: playerArmies) {
            for (RegimentEntity regiment: army.getRegiments()) {
                boolean manpowerUpkeepPayed = mana.withdrawManpower(regiment.getUnitAmount());
                if (!manpowerUpkeepPayed) {
                    removedRegiments.add(regiment);
                } else {
                    regiment.recoverUnits();
                }
            }
        }
        gameMap.removeRegiments(removedRegiments);

        // Run all the players tiles for building processProduction and modifiers
        for (MapTileEntity tile: playerMap) {

            Building building = tile.getBuilding();
            mana.raisePopulationMax(tilePopulationMaxBonus);

            if (building.isCompleted() && building.getType() != BuildingType.NONE) {
                boolean buildingProcessed = building.processProduction(mana, tile.getTerrain(), tile.getMapTileId().getCoordinates());

                if (!buildingProcessed) {
                    int buildingDamage = GameConfig.getBaseDisuseDamage();
                    building.damage(buildingDamage);
                }
                String buildingLogEntry = building.getEventLogEntry();
                playerEventLogEntries.add(buildingLogEntry);
            }
        }

        // calculate new Population
        calculatePopulationChange(mana, playerEventLogEntries);

        // calculate food spoilage
        int foodBeforeSpoilage = mana.getFood();
        int spoiledFood = mana.foodSpoilage();
        playerEventLogEntries.add(String.format(
                "Out of %s Food, %s was stored properly and %s Food was lost to spoilage;",
                foodBeforeSpoilage,
                mana.getProtectedFood(),
                spoiledFood));
    }

    public void calculatePopulationChange(ManaEntity mana, List<String> eventLogEntries) {

        final int CONSUMPTION = (int) Math.ceil((double) mana.getPopulation() / 100);
        boolean populationFoodPayed = mana.withdrawFood(CONSUMPTION);

        eventLogEntries.add(String.format(
                "Population: %d, Food consumption: %d, Enough food for population: %b;",
                mana.getPopulation(), CONSUMPTION, populationFoodPayed
        ));

        if (populationFoodPayed) {
            // this can result in a lowering if population is over populationMax
            int possiblePopulationIncrease = mana.getPopulationMax() - mana.getPopulation();
            int populationIncrease = (int) Math.min(possiblePopulationIncrease * 0.1, mana.getPopulation() * 0.1);
            mana.raisePopulation(populationIncrease);
            int populationIncreaseFoodCost = (int) (double) (populationIncrease / 10);
            mana.withdrawFood(populationIncreaseFoodCost);

            eventLogEntries.add(String.format(
                    "Possible Population increase: %d, Population increase: %d, Population Increase Food Cost: %d;",
                    possiblePopulationIncrease, populationIncrease, populationIncreaseFoodCost
            ));
        } else {
            int populationDecrease = mana.getPopulation() - (mana.withdrawAllFood() * 100);
            mana.lowerPopulation(populationDecrease);

            eventLogEntries.add(String.format(
                    "Population decrease: %d;",
                    populationDecrease
            ));
        }
    }

    private void processArmies(Map<String, List<String>> eventLogEntries) {

        Map<MapCoordinates, ArmyLocation> armyLocationMap = gameMap.getArmyLocationList();
        Queue<Battle> battleQueue = new LinkedList<>();

        for (ArmyLocation armyLocation: armyLocationMap.values()) {

            if (armyLocation.armies().size() > 1) {
                battleQueue.add(
                        new Battle(
                                armyLocation.armies(),
                                gameMap.getTileFromCoordinates(armyLocation.mapCoordinates())));
            }
        }

        while (!battleQueue.isEmpty()) {

            Battle battle = battleQueue.poll();

            List<BattleLog> battleLogList = BattleManager.processBattle(battle, gameMap, armyLocationMap);

            for (BattleLog battleLog: battleLogList) {
                eventLogEntries
                        .get(battleLog.player().name())
                        .add(battleLog.logEntry());

                getPlayerMana(battleLog.player().name()).lowerPopulation(battleLog.loses());
            }
        }
    }

    // Getters

    public GameSetupEntity getGameSetup() {
        if (!updated) {
            update();
        }
        return gameSetup;
    }

    public GameMapManager getGameMap() {
        if (!updated) {
            update();
        }
        return gameMap;
    }

    public List<EventEntity> getEventEntityList() {
        if (!updated) {
            update();
        }
        return eventEntityList;
    }

    public List<EventLogEntity> getEventLog() {
        if (!updated) {
            update();
        }
        return eventLog;
    }

    public List<ManaEntity> getManaList() {
        if (!updated) {
            update();
        }
        return manaList;
    }

    private ManaEntity getPlayerMana(String player) {
        for (ManaEntity mana: manaList) {
            if (mana.getPlayerNr().name().equals(player)
            ) {
                return mana;
            }
        }
        return null;
    }
}
