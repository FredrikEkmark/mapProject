package com.fredrik.mapProject.model.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.model.mana.EventManaCost;
import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.building.*;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;

import java.util.UUID;

public class BuildEvent extends Event {

    private EventManaCost eventManaCost;

    private BuildingType buildingType;

    public BuildEvent(UUID eventId,
                      Player playerNr,
                      int turn,
                      MapCoordinates primaryTileCoordinates,
                      EventType eventType,
                      String eventData,
                      String cost) {
        super(eventId, playerNr, turn, primaryTileCoordinates, eventType, true);
        parseFromEventData(eventData);
        parseFromCost(cost);
    }

    @Override
    public String stringifyEventData() {
        return String.format("{building: %s }", buildingType.name());
    }

    @Override
    public String stringifyCost() {
        StringBuilder stringBuilder = new StringBuilder("{");

        if (eventManaCost.getManpower() > 0) {
            stringBuilder.append("\"manpower\":").append(eventManaCost.getManpower()).append(",");
        }

        if (eventManaCost.getFood() > 0) {
            stringBuilder.append("\"food\":").append(eventManaCost.getFood()).append(",");
        }

        if (eventManaCost.getWood() > 0) {
            stringBuilder.append("\"wood\":").append(eventManaCost.getWood()).append(",");
        }

        if (eventManaCost.getLeather() > 0) {
            stringBuilder.append("\"leather\":").append(eventManaCost.getLeather()).append(",");
        }

        if (eventManaCost.getStone() > 0) {
            stringBuilder.append("\"stone\":").append(eventManaCost.getStone()).append(",");
        }

        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    @Override
    public void parseFromEventData(String eventData) {

        ObjectMapper objectMapper = new ObjectMapper();
        BuildingType type = BuildingType.NONE;

        try {
            JsonNode rootNode = objectMapper.readTree(eventData);

            JsonNode buildingNode = rootNode.get("building");

            if (buildingNode != null && buildingNode.isTextual()) {
                type = BuildingType.valueOf(buildingNode.asText());
            }
        } catch (RuntimeException | JsonProcessingException e) {
            System.out.println(e);
        }
        buildingType = type;
    }

    @Override
    public void parseFromCost(String cost) {
        ObjectMapper objectMapper = new ObjectMapper();
        EventManaCost eventManaCost = new EventManaCost();
        try {
            JsonNode rootNode = objectMapper.readTree(cost);

            JsonNode manpowerNode = rootNode.get("manpower");
            JsonNode woodNode = rootNode.get("wood");
            JsonNode foodNode = rootNode.get("food");
            JsonNode stoneNode = rootNode.get("stone");
            JsonNode leatherNode = rootNode.get("leather");

            if (manpowerNode != null && manpowerNode.isInt()) {
                eventManaCost.setManpower(manpowerNode.asInt());
            }

            if (woodNode != null && woodNode.isInt()) {
                eventManaCost.setWood(woodNode.asInt());
            }

            if (foodNode != null && foodNode.isInt()) {
                eventManaCost.setFood(foodNode.asInt());
            }

            if (stoneNode != null && stoneNode.isInt()) {
                eventManaCost.setStone(stoneNode.asInt());
            }

            if (leatherNode != null && leatherNode.isInt()) {
                eventManaCost.setLeather(leatherNode.asInt());
            }

            this.eventManaCost = eventManaCost;
        } catch (RuntimeException | JsonProcessingException e) {
            System.out.println(e);
        }
    }

    @Override
    public boolean processEvent(ManaEntity mana, GameMapManager gameMap) {

        MapTileEntity mapTile = gameMap.getTileFromCoordinates(getPrimaryTileCoordinates());

        boolean buildableElevation = buildingType.getBuildableElevation().contains(mapTile.getTerrain().getElevation());

        if (!buildableElevation) {
            setEventLogEntry(String.format("Could not build on tile %d:%d because not an acceptable terrain;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            setPersistent(false);
            return false;
        }

        boolean tileOwnedByPlayer = mapTile.getTileOwner() == getPlayerNr();

        if (!tileOwnedByPlayer) {
            setEventLogEntry(String.format("Could not build on tile %d:%d because not owned by player;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        Building alreadyExistingBuilding = mapTile.getBuilding();
        boolean buildingAlreadyComplete =  (alreadyExistingBuilding.isCompleted() && alreadyExistingBuilding.getType() != BuildingType.NONE);

        if (buildingAlreadyComplete) {
            setEventLogEntry(String.format("Could not build on tile %d:%d because building already complete;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        boolean eventBuildingIsSameAsExisting = (buildingType == alreadyExistingBuilding.getType());

        if (eventBuildingIsSameAsExisting) {
            int progressToComplete =
                    alreadyExistingBuilding.getType().getCompleteAtProgress() - alreadyExistingBuilding.getProgress();

            int manpowerPayableCost = Math.min(progressToComplete, eventManaCost.getManpower());
            boolean manpowerPaid = mana.withdrawManpower(manpowerPayableCost);

            if (manpowerPaid) {
                alreadyExistingBuilding.addProgress(manpowerPayableCost);
                mapTile.setBuilding(alreadyExistingBuilding);
                gameMap.addTileToUpdatedTiles(mapTile);
                if (alreadyExistingBuilding.isCompleted()) {
                    setPersistent(false);
                }
                setEventLogEntry(String.format("%d progress was added to %s on tile %d:%d, Progress is %d/%d;",
                        manpowerPayableCost,
                        alreadyExistingBuilding.getType().getBuilding(),
                        getPrimaryTileCoordinates().getX(),
                        getPrimaryTileCoordinates().getY(),
                        alreadyExistingBuilding.getProgress(),
                        alreadyExistingBuilding.getType().getCompleteAtProgress()));
                return true;
            }
        }

        boolean eventManaCostPayed = mana.payInFull(eventManaCost);

        if (!eventManaCostPayed) {
            setEventLogEntry(String.format("Not enough resources to build on tile %d:%d;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        Building newBuilding = getNewBuilding();

        mapTile.setBuilding(newBuilding);

        if (mapTile.getBuilding().isCompleted()) {
            setPersistent(false);
        }

        gameMap.addTileToUpdatedTiles(mapTile);



        setEventLogEntry(String.format("%d progress was added to %s on tile %d:%d, Progress is %d/%d;",
                eventManaCost.getManpower(),
                mapTile.getBuilding().getType().getBuilding(),
                getPrimaryTileCoordinates().getX(),
                getPrimaryTileCoordinates().getY(),
                mapTile.getBuilding().getProgress(),
                mapTile.getBuilding().getType().getCompleteAtProgress()));

        return true;
    }

    private Building getNewBuilding() {

        Building newBuilding;

        switch (buildingType) {
           case FARM -> newBuilding = new Farm(buildingType, eventManaCost.getManpower());
           case GRANARY -> newBuilding = new Granary(buildingType, eventManaCost.getManpower());
           case QUARRY -> newBuilding = new Quarry(buildingType, eventManaCost.getManpower());
           case LUMBER_CAMP -> newBuilding = new LumberCamp(buildingType, eventManaCost.getManpower());
           case CARPENTRY -> newBuilding = new Carpentry(buildingType, eventManaCost.getManpower());
           case RANCH -> newBuilding = new Ranch(buildingType, eventManaCost.getManpower());
           case LEATHER_WORKER -> newBuilding = new LeatherWorker(buildingType, eventManaCost.getManpower());
           case FISHERY -> newBuilding = new Fishery(buildingType, eventManaCost.getManpower());
           case VILLAGE -> newBuilding = new Village(buildingType, eventManaCost.getManpower());
           case TOWN -> newBuilding = new Town(buildingType, eventManaCost.getManpower());
           case CITY -> newBuilding = new City(buildingType, eventManaCost.getManpower());
           default -> newBuilding = new NoBuilding(BuildingType.NONE, 0);
       }
        return newBuilding;
    }
}
