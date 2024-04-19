package com.fredrik.mapProject.gameRunDomain;

import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gameRunDomain.model.building.Building;
import com.fredrik.mapProject.gameRunDomain.model.event.Event;
import com.fredrik.mapProject.gameRunDomain.model.entity.EventEntity;
import com.fredrik.mapProject.gameRunDomain.model.entity.EventLogEntity;
import com.fredrik.mapProject.gameRunDomain.model.GameMapManager;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;

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
                eventEntity.setTurn(eventEntity.getTurn() + 1);
                updatedEventEntityList.add(eventEntity);
            }
        }
    }

    private void processPlayers(Map<String, List<String>> eventLogEntries) {

        for (ManaEntity mana: manaList) {
            processPlayerMana(mana, eventLogEntries.get(mana.getPlayerNr().name()));
        }
    }

    private void processPlayerMana(ManaEntity mana, List<String> playerEventLogEntries) {

        List<MapTileEntity> playerMap = gameMap.getTilesWithPlayer(mana.getPlayerNr());
        // use the unused manpower from last turn for food production
        System.out.println("Manpower: " + mana.getManpower());
        int excessManpowerFromTurn = mana.withdrawAllManpower();
        mana.depositFood((int) Math.floor((double) excessManpowerFromTurn / 20));

        // reset the manpower for next turn
        mana.depositManpower(mana.getPopulation());
        System.out.println("Population: " + mana.getPopulation());
        System.out.println("Manpower: " + mana.getManpower());

        mana.setPopulationMax(0);
        mana.setProtectedFood(0);
        int tilePopulationMaxBonus = 100;

        // Run all the players tiles for building processProduction and modifiers
        for (MapTileEntity tile: playerMap) {

            Building building = tile.getBuilding();
            mana.raisePopulationMax(tilePopulationMaxBonus);

            if (building.isCompleted()) {
                boolean buildingProcessed = building.processProduction(mana, tile.getTerrain());
            }
        }

        // calculate Luxury resources impact on populationMax
        calculateLuxuryResourceEffect(mana, playerEventLogEntries);

        // calculate new Population
        calculatePopulationChange(mana, playerEventLogEntries);

        // calculate food spoilage
        int spoiledFood = mana.foodSpoilage();

        System.out.println("Population: " + mana.getPopulation());
        System.out.println("PopulationMax: " + mana.getPopulationMax());
        System.out.println("Manpower: " + mana.getManpower());
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

        System.out.println("Population: " + mana.getPopulation());

        final int CONSUMPTION = mana.getPopulation()/100;
        System.out.println("Consumption: " + CONSUMPTION
        );
        boolean populationFoodPayed = mana.withdrawFood(CONSUMPTION);
        System.out.println(populationFoodPayed);

        if (populationFoodPayed) {
            // this can result in a lowering if population is over populationMax
            int possiblePopulationIncrease = mana.getPopulationMax() - mana.getPopulation();
            int populationIncrease = (int) (possiblePopulationIncrease * 0.1) + (int) (mana.getPopulation() * 0.05);
            System.out.println("Possible Population increase: " + possiblePopulationIncrease);
            System.out.println("Population increase: " + populationIncrease);
            mana.raisePopulation(populationIncrease);
        } else {
            int populationDecrease = mana.getPopulation() - (mana.withdrawAllFood() * 100);
            System.out.println("Population decrease: " + populationDecrease);
            mana.lowerPopulation(populationDecrease);
        }
        System.out.println("Population: " + mana.getPopulation());
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
