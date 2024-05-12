package com.fredrik.mapProject.model.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.building.*;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;

import java.util.UUID;

public class BuildEvent extends Event {

    private BuildingType buildingType;

    public BuildEvent(UUID eventId,
                      Player playerNr,
                      int turn,
                      MapCoordinates primaryTileCoordinates,
                      EventType eventType,
                      String eventData,
                      String cost) {
        super(eventId, playerNr, turn, primaryTileCoordinates, eventType, true, cost);
        parseFromEventData(eventData);
        parseFromCost(cost);
    }

    @Override
    public String stringifyEventData() {
        return String.format("{building: %s }", buildingType.name());
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

            int manpowerPayableCost = Math.min(progressToComplete, getEventManaCost().getManpower());
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

        boolean eventManaCostPayed = mana.payInFull(getEventManaCost());

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
                getEventManaCost().getManpower(),
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
           case FARM -> newBuilding = new Farm(buildingType, getEventManaCost().getManpower());
           case GRANARY -> newBuilding = new Granary(buildingType, getEventManaCost().getManpower());
           case QUARRY -> newBuilding = new Quarry(buildingType, getEventManaCost().getManpower());
           case LUMBER_CAMP -> newBuilding = new LumberCamp(buildingType, getEventManaCost().getManpower());
           case CARPENTRY -> newBuilding = new Carpentry(buildingType, getEventManaCost().getManpower());
           case RANCH -> newBuilding = new Ranch(buildingType, getEventManaCost().getManpower());
           case LEATHER_WORKER -> newBuilding = new LeatherWorker(buildingType, getEventManaCost().getManpower());
           case FISHERY -> newBuilding = new Fishery(buildingType, getEventManaCost().getManpower());
           case VILLAGE -> newBuilding = new Village(buildingType, getEventManaCost().getManpower());
           case TOWN -> newBuilding = new Town(buildingType, getEventManaCost().getManpower());
           case CITY -> newBuilding = new City(buildingType, getEventManaCost().getManpower());
           default -> newBuilding = new NoBuilding(BuildingType.NONE, 0);
       }
        return newBuilding;
    }
}
