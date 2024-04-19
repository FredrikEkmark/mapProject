package com.fredrik.mapProject.gameRunDomain.model.event;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.ManaEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameRunDomain.model.GameMapManager;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    public void parseFromEventData(String eventData) {

    }

    @Override
    public void parseFromCost(String cost) {
        JsonObject jsonObject = JsonParser.parseString(cost).getAsJsonObject();
        this.manpowerCost = jsonObject.get("manpower").getAsInt();
    }

    // Event processing functions
    @Override
    public boolean processEvent(ManaEntity mana, GameMapManager gameMap) {

        MapTileEntity mapTile = gameMap.getTileFromCoordinates(getPrimaryTileCoordinates());

        boolean tileIsVisible = mapTile.isVisible(getPlayerNr().number());
        if (tileIsVisible) {
            return false;
        }

        boolean tileAdjacentVisible = gameMap.isTileVisibleAdjacentToPlayer(getPrimaryTileCoordinates(), getPlayerNr());

        if (!tileAdjacentVisible) {
            return false;
        }

        boolean manpowerPaid = mana.withdrawManpower(manpowerCost);

        if (!manpowerPaid) {
            return false;
        }

        // process the changes after a completion
        mapTile.setPlayerVisibility(true, getPlayerNr().number());

        gameMap.addTileToUpdatedTiles(mapTile);

        return true;
    }
}