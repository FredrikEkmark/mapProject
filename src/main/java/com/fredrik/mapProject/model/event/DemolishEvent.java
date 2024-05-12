package com.fredrik.mapProject.model.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.model.building.Building;
import com.fredrik.mapProject.model.building.BuildingType;
import com.fredrik.mapProject.model.building.NoBuilding;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.player.Player;

import java.util.UUID;

public class DemolishEvent extends Event {

    public DemolishEvent(UUID eventId,
                      Player playerNr,
                      int turn,
                      MapCoordinates primaryTileCoordinates,
                      EventType eventType,
                      String eventData,
                      String cost) {
        super(eventId, playerNr, turn, primaryTileCoordinates, eventType, false, cost);
        parseFromEventData(eventData);
        parseFromCost(cost);
    }

    @Override
    public String stringifyEventData() {return "{}";}

    @Override
    public void parseFromEventData(String eventData) {}

    @Override
    public boolean processEvent(ManaEntity mana, GameMapManager gameMap) {

        MapTileEntity mapTile = gameMap.getTileFromCoordinates(getPrimaryTileCoordinates());

        boolean tileOwnedByPlayer = mapTile.getTileOwner() == getPlayerNr();

        if (!tileOwnedByPlayer) {
            setEventLogEntry(String.format("Could not demolish building on tile %d:%d because not owned by player;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        Building alreadyExistingBuilding = mapTile.getBuilding();
        boolean buildingComplete =  (alreadyExistingBuilding.isCompleted() && alreadyExistingBuilding.getType() != BuildingType.NONE);


        if (!buildingComplete) {
            setEventLogEntry(String.format("Could not demolish building on tile %d:%d because building is damaged;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        boolean manpowerPaid = mana.withdrawManpower(getEventManaCost().getManpower());

        if (!manpowerPaid) {
            setEventLogEntry(String.format("Not enough manpower to demolish building on tile %d:%d;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        Building noBuilding = new NoBuilding(BuildingType.NONE, 0);

        mapTile.setBuilding(noBuilding);

        gameMap.addTileToUpdatedTiles(mapTile);

        setEventLogEntry(String.format("Building on tile %d:%d demolished;",
                getPrimaryTileCoordinates().getX(),
                getPrimaryTileCoordinates().getY()));

        return true;
    }
}
