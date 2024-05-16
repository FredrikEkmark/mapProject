package com.fredrik.mapProject.model.event.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.databaseEntity.RegimentEntity;
import com.fredrik.mapProject.model.event.Event;
import com.fredrik.mapProject.model.event.dto.NewChildEventDataDTO;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SplitArmyEvent extends Event {

    private UUID armyId;

    private MapCoordinates destinationCoordinates;

    private List<UUID> regimentIDs;

    public SplitArmyEvent(NewChildEventDataDTO event) {
        super(event.getEventId(),
                event.getPlayerNr(),
                event.getTurn(),
                event.getPrimaryTileCoordinates(),
                event.getEventType(),
                false,
                true,
                event.getCost());
        parseFromEventData(event.getEventData());
        parseFromCost(event.getCost());
    }
    @Override
    public String stringifyEventData() {
        String regimentIdsString = regimentIDs.stream()
                .map(uuid -> "\"" + uuid.toString() + "\"")
                .collect(Collectors.joining(","));

        return String.format("{\"destination\":{\"x\":%d,\"y\":%d}, \"regiments\":[%s],\"armyId\": \"%s\"}",
                destinationCoordinates.getX(),
                destinationCoordinates.getY(),
                regimentIdsString,
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
            JsonNode regimentsNode = rootNode.get("regiments");
            JsonNode armyIdNode = rootNode.get("armyId");

            this.armyId = UnitEventUtility.parseArmyId(armyIdNode);
            this.destinationCoordinates = UnitEventUtility.parseDestinationCoordinate(xNode, yNode);
            this.regimentIDs = UnitEventUtility.parseRegimentIDs(regimentsNode);

        } catch (RuntimeException | JsonProcessingException e) {
            System.out.println(e);
        }
    }

    @Override
    public boolean processEvent(ManaEntity mana, GameMapManager gameMap) {

        ArmyEntity army = gameMap.getArmyFromCoordinatesAndId(getPrimaryTileCoordinates(), armyId);

        boolean armyPresent = army != null;

        if (!armyPresent) {
            setEventLogEntry(String.format("Could not split army from tile %d:%d to tile %d:%d because the specified army was not present;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY(),
                    destinationCoordinates.getX(),
                    destinationCoordinates.getY()));
            return false;
        }

        boolean armyOwnedByPlayer = army.getOwner().equals(getPlayerNr());

        if (!armyOwnedByPlayer) {
            setEventLogEntry(String.format("Could not split army from tile %d:%d to tile %d:%d because army was not owned by player;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY(),
                    destinationCoordinates.getX(),
                    destinationCoordinates.getY()));
            return false;
        }

        Iterator<RegimentEntity> iterator = army.getRegiments().iterator();
        List<RegimentEntity> remainingRegiments = new ArrayList<>();
        List<RegimentEntity> splitRegiments = new ArrayList<>();

        while (iterator.hasNext()) {
            RegimentEntity regiment = iterator.next();
            if (regimentIDs.contains(regiment.getRegimentId())) {
                splitRegiments.add(regiment);
            } else {
                remainingRegiments.add(regiment);
            }
        }

        ArmyEntity splitArmy = new ArmyEntity(gameMap.getGameId(),
                getPlayerNr(),
                gameMap.getPlayerNextArmyNumber(getPlayerNr()),
                getPrimaryTileCoordinates());

        boolean armyMoved = gameMap.moveArmy(splitArmy, destinationCoordinates);

        if (!armyMoved) {
            setEventLogEntry(String.format("Could not split army from tile %d:%d to tile %d:%d because it was not a valid move;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY(),
                    destinationCoordinates.getX(),
                    destinationCoordinates.getY()));
            return false;
        }

        army.setRegiments(remainingRegiments);

        setEventLogEntry(String.format("%s on tile %d:%d has split part of it to tile %d:%d;",
                army.getArmyName(),
                getPrimaryTileCoordinates().getX(),
                getPrimaryTileCoordinates().getY(),
                destinationCoordinates.getX(),
                destinationCoordinates.getY()));

        return false;
    }
}
