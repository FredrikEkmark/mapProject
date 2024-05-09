package com.fredrik.mapProject.gameRunDomain.model.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameRunDomain.model.GameMapManager;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;

import java.util.UUID;

public class ExploreEvent extends Event {

    private int manpowerCost;

    public ExploreEvent(UUID eventId,
                        Player playerNr,
                        int turn,
                        MapCoordinates primaryTileCoordinates,
                        EventType eventType,
                        String eventData,
                        String cost) {
        super(eventId, playerNr, turn, primaryTileCoordinates, eventType, false);
        parseFromEventData(eventData);
        parseFromCost(cost);
    }


    @Override
    public String stringifyEventData() {return "{}";}

    @Override
    public String stringifyCost() {
        return String.format("{\"manpower\":%s}", manpowerCost) ;
    }

    @Override
    public void parseFromEventData(String eventData) {

    }

    @Override
    public void parseFromCost(String cost) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(cost);

            JsonNode manpowerNode = rootNode.get("manpower");

            if (manpowerNode != null && manpowerNode.isInt()) {
                manpowerCost = manpowerNode.asInt();
            }
        } catch (RuntimeException | JsonProcessingException e) {
            System.out.println(e);
        }
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

        boolean manpowerPaid = mana.withdrawManpower(manpowerCost);

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
