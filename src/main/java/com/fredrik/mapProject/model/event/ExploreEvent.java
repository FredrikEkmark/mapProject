package com.fredrik.mapProject.model.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;

import java.util.UUID;

public class ExploreEvent extends Event {

    public ExploreEvent(UUID eventId,
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
