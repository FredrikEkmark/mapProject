package com.fredrik.mapProject.model.event.expand;

import com.fredrik.mapProject.model.event.Event;
import com.fredrik.mapProject.model.event.dto.NewChildEventDataDTO;
import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;

import java.util.List;

public class ClaimTileEvent extends Event {

    public ClaimTileEvent(NewChildEventDataDTO event) {
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
    public void parseFromEventData(String eventData) {}

    @Override
    public String stringifyEventData() {return "{}";}

    @Override
    public boolean processEvent(ManaEntity mana, GameMapManager gameMap) {

        MapTileEntity mapTile = gameMap.getTileFromCoordinates(getPrimaryTileCoordinates());

        boolean tileIsVisible = mapTile.isVisible(getPlayerNr().number());

        List<MapTileEntity> playerTiles = gameMap.getTilesWithPlayer(getPlayerNr());

        boolean canClaimMoreTiles = ((mana.getPopulation() / 100) > playerTiles.size());

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

        boolean manpowerPaid = mana.withdrawManpower(getEventManaCost().getManpower());

        if (!manpowerPaid) {
            setEventLogEntry(String.format("Not enough manpower to claim tile %d:%d;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        mapTile.setTileOwner(getPlayerNr());

        gameMap.addTileToUpdatedTiles(mapTile);
        playerTiles.add(mapTile);

        setEventLogEntry(String.format("Tile %d:%d claimed by player %d;",
                getPrimaryTileCoordinates().getX(),
                getPrimaryTileCoordinates().getY(),
                getPlayerNr().number()));

        return true;
    }

}
