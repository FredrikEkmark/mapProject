package com.fredrik.mapProject.gameRunDomain.model.event;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameRunDomain.model.GameMapManager;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
        JsonObject jsonObject = JsonParser.parseString(cost).getAsJsonObject();
        this.manpowerCost = jsonObject.get("manpower").getAsInt();
    }

    @Override
    public String stringifyEventData() {return "{}";}

    @Override
    public boolean processEvent(ManaEntity mana, GameMapManager gameMap) {

        MapTileEntity mapTile = gameMap.getTileFromCoordinates(getPrimaryTileCoordinates());

        boolean tileIsVisible = mapTile.isVisible(getPlayerNr().number());

        if (!tileIsVisible) {
            System.out.println("Tile is visible");
            return false;
        }

        boolean tileIsUnOwned = mapTile.getTileOwner() == Player.NONE;

        if (!tileIsUnOwned) {
            System.out.println("Tile is owned");
            return false;
        }

        boolean tileIsAdjacentOwned = gameMap.isTileOwnerAdjacentToPlayer(mapTile.getMapTileId().getCoordinates(),
                getPlayerNr());

        if (!tileIsAdjacentOwned) {
            System.out.println("Tile is not adjacent");
            return false;
        }

        boolean manpowerPaid = mana.withdrawManpower(manpowerCost);

        if (!manpowerPaid) {
            System.out.println("Manpower Not paid");
            return false;
        }

        mapTile.setTileOwner(getPlayerNr());

        gameMap.addTileToUpdatedTiles(mapTile);

        return true;
    }

}
