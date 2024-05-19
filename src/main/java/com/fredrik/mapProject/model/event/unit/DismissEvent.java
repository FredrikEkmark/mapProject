package com.fredrik.mapProject.model.event.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;
import com.fredrik.mapProject.model.databaseEntity.RegimentEntity;
import com.fredrik.mapProject.model.event.Event;
import com.fredrik.mapProject.model.event.dto.NewChildEventDataDTO;
import com.fredrik.mapProject.model.map.GameMapManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DismissEvent extends Event {

    private UUID armyId;

    private List<UUID> regimentIDs;

    public DismissEvent(NewChildEventDataDTO event) {
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
        String regimentIdsString = regimentIDs.stream()
                .map(uuid -> "\"" + uuid.toString() + "\"")
                .collect(Collectors.joining(","));

        return String.format("{\"regiments\":[%s],\"armyId\": \"%s\"}",
                regimentIdsString,
                armyId);
    }

    @Override
    public void parseFromEventData(String eventData) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(eventData);
            JsonNode regimentsNode = rootNode.get("regimentIDs");
            JsonNode armyIdNode = rootNode.get("armyId");

            this.armyId = UnitEventUtility.parseArmyId(armyIdNode);
            this.regimentIDs = UnitEventUtility.parseRegimentIDs(regimentsNode);

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
            setEventLogEntry(String.format("Could not dismiss Regiments on tile %d:%d because the specified army was not present;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        boolean armyOwnedByPlayer = army.getOwner().equals(getPlayerNr());

        if (!armyOwnedByPlayer) {
            setEventLogEntry(String.format("Could not dismiss Regiments on tile %d:%d because army was not owned by player;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        boolean tileOwnedByPlayer = mapTile.getTileOwner().equals(getPlayerNr());

        if (!tileOwnedByPlayer) {
            setEventLogEntry(String.format("Could not dismiss Regiments on tile %d:%d because the tile is not owned by the player;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        List<RegimentEntity> remainingRegiments = new ArrayList<>();
        List<RegimentEntity> dismissedRegiments = new ArrayList<>();

        for (RegimentEntity regiment: army.getRegiments()) {
            if (regimentIDs.contains(regiment.getRegimentId())) {
                dismissedRegiments.add(regiment);
            } else {
                remainingRegiments.add(regiment);
            }
        }

        if (remainingRegiments.size() == 0) {
            gameMap.removeArmy(army);
            setEventLogEntry(String.format("%s on tile %d:%d dismissed;",
                    army.getArmyName(),
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
        } else {
            army.setRegiments(remainingRegiments);
            gameMap.removeRegiments(dismissedRegiments);
            setEventLogEntry(String.format("%d regiments on tile %d:%d dismissed;",
                    dismissedRegiments.size(),
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
        }

        return true;
    }
}
