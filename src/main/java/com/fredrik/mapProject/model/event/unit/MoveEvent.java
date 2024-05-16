package com.fredrik.mapProject.model.event.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.event.Event;
import com.fredrik.mapProject.model.event.dto.NewChildEventDataDTO;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;

import java.util.*;

public class MoveEvent extends Event {

    private UUID armyId;

    private MapCoordinates destinationCoordinates;

    public MoveEvent(NewChildEventDataDTO event) {
        super(event.getEventId(),
                event.getPlayerNr(),
                event.getTurn(),
                event.getPrimaryTileCoordinates(),
                event.getEventType(),
                true,
                true,
                event.getCost());
        parseFromEventData(event.getEventData());
        parseFromCost(event.getCost());
    }

    @Override
    public String stringifyEventData() {
        return String.format("{\"destination\":{\"x\":%d,\"y\":%d},\"armyId\": \"%s\"}",
                destinationCoordinates.getX(),
                destinationCoordinates.getY(),
                armyId);
    }

    @Override
    public void parseFromEventData(String eventData) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(eventData);
            JsonNode destinationNode = rootNode.get("destination");
            JsonNode xNode = destinationNode.get("x");
            JsonNode yNode = destinationNode.get("y");
            JsonNode armyIdNode = rootNode.get("armyId");

            this.armyId = UnitEventUtility.parseArmyId(armyIdNode);
            this.destinationCoordinates = UnitEventUtility.parseDestinationCoordinate(xNode, yNode);

        } catch (RuntimeException | JsonProcessingException e) {
            System.out.println(e);
        }
    }

    @Override
    public boolean processEvent(ManaEntity mana, GameMapManager gameMap) {

        ArmyEntity army = gameMap.getArmyFromCoordinatesAndId(getPrimaryTileCoordinates(), armyId);

        boolean armyPresent = army != null;

        if (!armyPresent) {
            setEventLogEntry(String.format("Could not move from tile %d:%d to tile %d:%d because the specified army was not present;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY(),
                    destinationCoordinates.getX(),
                    destinationCoordinates.getY()));
            return false;
        }

        boolean armyOwnedByPlayer = army.getOwner().equals(getPlayerNr());

        if (!armyOwnedByPlayer) {
            setEventLogEntry(String.format("Could not move from tile %d:%d to tile %d:%d because army was not owned by player;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY(),
                    destinationCoordinates.getX(),
                    destinationCoordinates.getY()));
            return false;
        }

        boolean armyMoved = gameMap.moveArmy(army, destinationCoordinates);

        if (!armyMoved) {
            setEventLogEntry(String.format("Could not move from tile %d:%d to tile %d:%d because it was not a valid move;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY(),
                    destinationCoordinates.getX(),
                    destinationCoordinates.getY()));
            return false;
        }

        setEventLogEntry(String.format("%s on tile %d:%d has move to tile %d:%d;",
                army.getArmyName(),
                getPrimaryTileCoordinates().getX(),
                getPrimaryTileCoordinates().getY(),
                destinationCoordinates.getX(),
                destinationCoordinates.getY()));

        return true;
    }
}
