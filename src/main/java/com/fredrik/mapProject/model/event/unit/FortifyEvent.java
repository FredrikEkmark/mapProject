package com.fredrik.mapProject.model.event.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;
import com.fredrik.mapProject.model.event.Event;
import com.fredrik.mapProject.model.event.dto.NewChildEventDataDTO;
import com.fredrik.mapProject.model.map.GameMapManager;

import java.util.UUID;

public class FortifyEvent extends Event {

    private UUID armyId;

    public FortifyEvent(NewChildEventDataDTO event) {
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
    public String stringifyEventData() {
        return String.format("{\"armyId\": \"%s\"}", armyId);
    }

    @Override
    public void parseFromEventData(String eventData) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(eventData);
            JsonNode armyIdNode = rootNode.get("armyId");

            this.armyId = UnitEventUtility.parseArmyId(armyIdNode);

        } catch (RuntimeException | JsonProcessingException e) {
            System.out.println(e);
        }
    }

    @Override
    public boolean processEvent(ManaEntity mana, GameMapManager gameMap) {

        MapTileEntity mapTile = gameMap.getTileFromCoordinates(getPrimaryTileCoordinates());
        ArmyEntity army = gameMap.getArmyFromCoordinatesAndId(getPrimaryTileCoordinates(), armyId);

        boolean armyPresent = army != null;

        if (!armyPresent) {
            setEventLogEntry(String.format("Could not fortify tile %d:%d because the specified army was not present;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        boolean armyOwnedByPlayer = army.getOwner().equals(getPlayerNr());

        if (!armyOwnedByPlayer) {
            setEventLogEntry(String.format("Could not fortify tile %d:%d because the specified army is not owned by %s;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY(),
                    getPlayerNr().name()));
            return false;
        }

        boolean tileOwnedByPlayer = mapTile.getTileOwner().equals(getPlayerNr());

        if (!tileOwnedByPlayer) {
            setEventLogEntry(String.format("Could not fortify tile %d:%d because the tile is not owned by by %s;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY(),
                    getPlayerNr().name()));
            return false;
        }

        army.setFortified(true);

        setEventLogEntry(String.format("%s on tile %d:%d has been fortified;",
                army.getArmyName(),
                getPrimaryTileCoordinates().getX(),
                getPrimaryTileCoordinates().getY()));

        return true;
    }
}
