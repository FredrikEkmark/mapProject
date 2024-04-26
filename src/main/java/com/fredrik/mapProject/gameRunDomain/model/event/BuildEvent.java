package com.fredrik.mapProject.gameRunDomain.model.event;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameRunDomain.model.GameMapManager;
import com.fredrik.mapProject.gameRunDomain.model.building.*;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.UUID;

public class BuildEvent extends Event {

    private int manpowerCost;

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
        // toDo add logic to return the correct jsonData
        return "{}";
    }

    @Override
    public void parseFromEventData(String eventData) {
        JsonObject jsonObject;
        BuildingType type = BuildingType.NONE;

        try {
            jsonObject = JsonParser.parseString(eventData).getAsJsonObject();
            type = BuildingType.valueOf(jsonObject.get("building").getAsString());
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
        buildingType = type;
    }

    @Override
    public void parseFromCost(String cost) {
        JsonObject jsonObject = JsonParser.parseString(cost).getAsJsonObject();
        this.manpowerCost = jsonObject.get("manpower").getAsInt();
    }

    @Override
    public boolean processEvent(ManaEntity mana, GameMapManager gameMap) {

        MapTileEntity mapTile = gameMap.getTileFromCoordinates(getPrimaryTileCoordinates());

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

        boolean manpowerPaid;

        if (eventBuildingIsSameAsExisting) {
            int progressToComplete =
                    alreadyExistingBuilding.getType().getCompleteAtProgress() - alreadyExistingBuilding.getProgress();

            int manpowerPayableCost = Math.min(progressToComplete, manpowerCost);
            manpowerPaid = mana.withdrawManpower(manpowerPayableCost);

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

        manpowerPaid = mana.withdrawManpower(manpowerCost);

        if (!manpowerPaid) {
            setEventLogEntry(String.format("Not enough manpower to build on tile %d:%d;",
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
                manpowerCost,
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
           case FARM -> newBuilding = new Farm(buildingType, manpowerCost);
           case GRANARY -> newBuilding = new Granary(buildingType, manpowerCost);
           case QUARRY -> newBuilding = new Quarry(buildingType, manpowerCost);
           case LUMBER_CAMP -> newBuilding = new LumberCamp(buildingType, manpowerCost);
           case CARPENTRY -> newBuilding = new Carpentry(buildingType, manpowerCost);
           case RANCH -> newBuilding = new Ranch(buildingType, manpowerCost);
           case LEATHER_WORKER -> newBuilding = new LeatherWorker(buildingType, manpowerCost);
           case FISHERY -> newBuilding = new Fishery(buildingType, manpowerCost);
           case VILLAGE -> newBuilding = new Village(buildingType, manpowerCost);
           case TOWN -> newBuilding = new Town(buildingType, manpowerCost);
           case CITY -> newBuilding = new City(buildingType, manpowerCost);
           default -> newBuilding = new NoBuilding(BuildingType.NONE, manpowerCost);
       }
        return newBuilding;
    }
}
