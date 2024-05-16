package com.fredrik.mapProject.model.event.expand;

import com.fredrik.mapProject.model.event.Event;
import com.fredrik.mapProject.model.event.dto.NewChildEventDataDTO;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;

public class ExploreEvent extends Event {

    public ExploreEvent(NewChildEventDataDTO event) {
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
    public void parseFromEventData(String eventData) {

    }

    // Event processing functions
    @Override
    public boolean processEvent(ManaEntity mana, GameMapManager gameMap) {

        MapTileEntity mapTile = gameMap.getTileFromCoordinates(getPrimaryTileCoordinates());

        boolean tileIsVisible = mapTile.isVisible(getPlayerNr().number());
        if (tileIsVisible) {
            setEventLogEntry(String.format("Could not explore tile %d:%d because already visible;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        boolean tileAdjacentVisible = gameMap.isTileVisibleAdjacentToPlayer(getPrimaryTileCoordinates(), getPlayerNr());

        if (!tileAdjacentVisible) {
            setEventLogEntry(String.format("Could not explore tile %d:%d because no adjacent visible tile;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        boolean manpowerPaid = mana.withdrawManpower(getEventManaCost().getManpower());

        if (!manpowerPaid) {
            setEventLogEntry(String.format("Not enough manpower to explore tile %d:%d;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        // process the changes after a completion
        mapTile.setPlayerVisibility(true, getPlayerNr().number());

        gameMap.addTileToUpdatedTiles(mapTile);

        setEventLogEntry(String.format("Tile %d:%d explored and is now visible;",
                getPrimaryTileCoordinates().getX(),
                getPrimaryTileCoordinates().getY()));

        return true;
    }
}
