package com.fredrik.mapProject.model.event.build;

import com.fredrik.mapProject.model.building.Building;
import com.fredrik.mapProject.model.building.BuildingType;
import com.fredrik.mapProject.model.building.NoBuilding;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;
import com.fredrik.mapProject.model.event.Event;
import com.fredrik.mapProject.model.event.dto.NewChildEventDataDTO;
import com.fredrik.mapProject.model.map.GameMapManager;

public class DemolishEvent extends Event {

    public DemolishEvent(NewChildEventDataDTO event) {
        super(event.getEventId(),
                event.getPlayerNr(),
                event.getTurn(),
                event.getPrimaryTileCoordinates(),
                event.getEventType(),
                false,
                false,
                event.getCost());
        parseFromEventData(event.getEventData());
        parseFromCost(event.getCost());
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
