package com.fredrik.mapProject.gameRunDomain.model.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameRunDomain.model.GameMapManager;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;

import java.util.List;
import java.util.UUID;

public class ClaimTileEvent extends Event {

    private int manpowerCost;

    public ClaimTileEvent(UUID eventId,
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
    public void parseFromEventData(String eventData) {}

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

    @Override
    public String stringifyEventData() {return "{}";}

    @Override
    public String stringifyCost() {
        return String.format("{\"manpower\":%s}", manpowerCost) ;
    }

    @Override
    public boolean processEvent(ManaEntity mana, GameMapManager gameMap) {

        MapTileEntity mapTile = gameMap.getTileFromCoordinates(getPrimaryTileCoordinates());

        boolean tileIsVisible = mapTile.isVisible(getPlayerNr().number());

        List<MapTileEntity> playerTiles = gameMap.getTilesWithPlayer(getPlayerNr());

        boolean canClaimMoreTiles = ((mana.getPopulation() / 100) > playerTiles.size());

        System.out.println(mana.getPopulation() / 100);
        System.out.println(gameMap.getTilesWithPlayer(getPlayerNr()).size());

        if (!canClaimMoreTiles) {
            setEventLogEntry(String.format("Could not claim tile %d:%d because insufficient population;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }


        if (!tileIsVisible) {
            setEventLogEntry(String.format("Could not claim tile %d:%d because not visible;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        boolean tileIsUnOwned = mapTile.getTileOwner() == Player.NONE;

        if (!tileIsUnOwned) {
            setEventLogEntry(String.format("Could not claim tile %d:%d because already claimed by player %d;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY(),
                    gameMap.getTileFromCoordinates(getPrimaryTileCoordinates()).getTileOwner().number()));
            return false;
        }

        boolean tileIsAdjacentOwned = gameMap.isTileOwnerAdjacentToPlayer(mapTile.getMapTileId().getCoordinates(),
                getPlayerNr());

        if (!tileIsAdjacentOwned) {
            setEventLogEntry(String.format("Could not claim tile %d:%d because no adjacent owned tile;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        boolean manpowerPaid = mana.withdrawManpower(manpowerCost);

        if (!manpowerPaid) {
            setEventLogEntry(String.format("Not enough manpower to claim tile %d:%d;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        mapTile.setTileOwner(getPlayerNr());

        gameMap.addTileToUpdatedTiles(mapTile);
        playerTiles.add(mapTile);

        setEventLogEntry(String.format("Tile %d:%d claimed;",
                getPrimaryTileCoordinates().getX(),
                getPrimaryTileCoordinates().getY(),
                getPlayerNr().number()));

        return true;
    }

}
