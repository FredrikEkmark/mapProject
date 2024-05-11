package com.fredrik.mapProject.model.turnChange;

import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.building.Building;
import com.fredrik.mapProject.model.building.BuildingType;
import com.fredrik.mapProject.model.databaseEntity.*;
import com.fredrik.mapProject.model.event.Event;

import java.util.*;

public class TurnChange {

    private GameSetupEntity gameSetup;

    private GameMapManager gameMap;

    private List<EventEntity> eventEntityList;

    private List<ManaEntity> manaList;

    private List<EventLogEntity> eventLog;

    private boolean updated = false;

    public TurnChange(GameSetupEntity gameSetup, GameMapManager gameMap, List<EventEntity> eventEntityList, List<ManaEntity> manaList) {
        this.gameSetup = gameSetup;
        this.gameMap = gameMap;
        this.eventEntityList = eventEntityList;
        this.manaList = manaList;
        this.eventLog = new ArrayList<EventLogEntity>();
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

        // PROCESSING MANA LIST
        processPlayers(eventLogEntries);

        eventEntityList = updatedEventEntityList;
        gameSetup.setTurn(gameSetup.getTurn() + 1); // ticks the turn up one
        updated = true;
        gameSetup.setUpdating(false);
        System.out.printf("""
                GameId: %s
                Turn has been updated
                """, gameSetup.getId()); // toDo write full log for turn update, number of tiles changed, eventLogs remaining...
    }

    // Processing functions

    private void processEventsEntityList(List<EventEntity> updatedEventEntityList, Map<String, List<String>> eventLogEntries) {

        // Sort list by EventType
        eventEntityList.sort(Comparator.comparingInt(eventEntity -> eventEntity.getEvent().getEventType().ordinal()));

        for (EventEntity eventEntity: eventEntityList) {
            System.out.printf("EventId: %s, EventType: %s, Cost: %s, EventData: %s, Turn: %s\n", eventEntity.getEventId(), eventEntity.getEventType(), eventEntity.getCost(), eventEntity.getEventData(), eventEntity.getTurn());

            Event event = eventEntity.getEvent();

            ManaEntity mana = manaList.stream()
                    .filter(m -> m.getPlayerNr().name() == eventEntity.getPlayerNr().name())
                    .findFirst()
                    .orElse(null);

            // PROCESS EVENT
            boolean success = event.processEvent(mana, gameMap);
            // PROCESS EVENT

            String eventLogEntry = event.getEventLogEntry(); // toDo write logic in this function
            eventLogEntries.get(event.getPlayerNr().name()).add(eventLogEntry);

            if (!success) {
                System.out.println("Event Not Processed");
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

            StringBuilder eventLogText = new StringBuilder("");
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
                    eventLogText = new StringBuilder("");
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

        // use the unused manpower from last turn for food production
        int excessManpowerFromTurn = mana.withdrawAllManpower();

        // reset the manpower for next turn
        mana.depositManpower(mana.getPopulation());

        mana.setPopulationMax(0);
        mana.setProtectedFood(0);
        int tilePopulationMaxBonus = 100;

        // Run all the players tiles for building processProduction and modifiers
        for (MapTileEntity tile: playerMap) {

            Building building = tile.getBuilding();
            mana.raisePopulationMax(tilePopulationMaxBonus);

            if (building.isCompleted() && building.getType() != BuildingType.NONE) {
                boolean buildingProcessed = building.processProduction(mana, tile.getTerrain(), tile.getMapTileId().getCoordinates());

                if (!buildingProcessed) {
                    int buildingDamage = 20;
                    building.damage(buildingDamage);
                }
                String buildingLogEntry = building.getEventLogEntry();
                playerEventLogEntries.add(buildingLogEntry);
            }
        }

        // calculate Luxury resources impact on populationMax
        calculateLuxuryResourceEffect(mana, playerEventLogEntries);

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

    public void calculateLuxuryResourceEffect(ManaEntity mana, List<String> eventLogEntries) {

        final int CONSUMPTION = mana.getPopulation()/100;

        int furniturePopulationMaxBonus = 100;
        boolean populationFurniturePayed = mana.withdrawFurniture(CONSUMPTION);

        int furnitureBonusPopulationMaxIncrease;
        if (populationFurniturePayed) {
            furnitureBonusPopulationMaxIncrease = furniturePopulationMaxBonus * CONSUMPTION;
        } else {
            furnitureBonusPopulationMaxIncrease = furniturePopulationMaxBonus * mana.withdrawAllFurniture();
        }
        mana.raisePopulationMax(furnitureBonusPopulationMaxIncrease);

        int simpleClothesPopulationMaxBonus = 100;
        boolean populationSimpleClothesPayed = mana.withdrawSimpleClothes(CONSUMPTION);

        int simpleClothesBonusPopulationMaxIncrease;
        if (populationSimpleClothesPayed) {
            simpleClothesBonusPopulationMaxIncrease = simpleClothesPopulationMaxBonus * CONSUMPTION;
        } else {
            simpleClothesBonusPopulationMaxIncrease = simpleClothesPopulationMaxBonus * mana.withdrawAllSimpleClothes();
        }
        mana.raisePopulationMax(simpleClothesBonusPopulationMaxIncrease);
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
}
