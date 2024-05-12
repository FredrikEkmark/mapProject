package com.fredrik.mapProject.model.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.model.mana.EventManaCost;
import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.map.GameMapManager;

import java.util.UUID;

public abstract class Event {

    private UUID eventId;

    private Player playerNr;

    private int turn;

    private MapCoordinates primaryTileCoordinates;

    private final EventType eventType;

    private boolean persistent;

    private String eventLogEntry;

    private EventManaCost eventManaCost;

    public Event(UUID eventId,
                 Player playerNr,
                 int turn,
                 MapCoordinates primaryTileCoordinates,
                 EventType eventType,
                 boolean persistent,
                 String cost) {
        this.eventId = eventId;
        this.playerNr = playerNr;
        this.turn = turn;
        this.primaryTileCoordinates = primaryTileCoordinates;
        this.eventType = eventType;
        this.persistent = persistent;
        this.eventLogEntry = "";
        parseFromCost(cost);
    }

    // Setup functions for child classes

    public abstract String stringifyEventData();

    public abstract void parseFromEventData(String eventData);

    public void parseFromCost(String cost) {
        ObjectMapper objectMapper = new ObjectMapper();
        EventManaCost eventManaCost = new EventManaCost();
        try {
            JsonNode rootNode = objectMapper.readTree(cost);

            JsonNode manpowerNode = rootNode.get("manpower");
            JsonNode woodNode = rootNode.get("wood");
            JsonNode foodNode = rootNode.get("food");
            JsonNode stoneNode = rootNode.get("stone");
            JsonNode leatherNode = rootNode.get("leather");
            JsonNode furnitureNode = rootNode.get("furniture");
            JsonNode simpleClothesNode = rootNode.get("simpleClothes");

            if (manpowerNode != null && manpowerNode.isInt()) {
                eventManaCost.setManpower(manpowerNode.asInt());
            }

            if (woodNode != null && woodNode.isInt()) {
                eventManaCost.setWood(woodNode.asInt());
            }

            if (foodNode != null && foodNode.isInt()) {
                eventManaCost.setFood(foodNode.asInt());
            }

            if (stoneNode != null && stoneNode.isInt()) {
                eventManaCost.setStone(stoneNode.asInt());
            }

            if (leatherNode != null && leatherNode.isInt()) {
                eventManaCost.setLeather(leatherNode.asInt());
            }

            if (furnitureNode != null && furnitureNode.isInt()) {
                eventManaCost.setFurniture(furnitureNode.asInt());
            }

            if (simpleClothesNode != null && simpleClothesNode.isInt()) {
                eventManaCost.setSimpleClothes(simpleClothesNode.asInt());
            }

            this.eventManaCost = eventManaCost;
        } catch (RuntimeException | JsonProcessingException e) {
            System.out.println(e);
        }
    }

    public String stringifyCost() {
        StringBuilder stringBuilder = new StringBuilder("{");

        if (eventManaCost.getManpower() > 0) {
            stringBuilder.append("\"manpower\":").append(eventManaCost.getManpower()).append(",");
        }

        if (eventManaCost.getFood() > 0) {
            stringBuilder.append("\"food\":").append(eventManaCost.getFood()).append(",");
        }

        if (eventManaCost.getWood() > 0) {
            stringBuilder.append("\"wood\":").append(eventManaCost.getWood()).append(",");
        }

        if (eventManaCost.getLeather() > 0) {
            stringBuilder.append("\"leather\":").append(eventManaCost.getLeather()).append(",");
        }

        if (eventManaCost.getStone() > 0) {
            stringBuilder.append("\"stone\":").append(eventManaCost.getStone()).append(",");
        }

        if (stringBuilder.charAt(stringBuilder.length() - 1) == ',') {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    // Event Processing functions

    public abstract boolean processEvent(ManaEntity mana, GameMapManager gameMap);


    // Getters and Setters

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public Player getPlayerNr() {
        return playerNr;
    }

    public void setPlayerNr(Player playerNr) {
        this.playerNr = playerNr;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public EventType getEventType() {
        return eventType;
    }

    public MapCoordinates getPrimaryTileCoordinates() {
        return primaryTileCoordinates;
    }

    public void setPrimaryTileCoordinates(MapCoordinates primaryTileCoordinates) {
        this.primaryTileCoordinates = primaryTileCoordinates;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public String getEventLogEntry() {
        return eventLogEntry;
    }

    public void setEventLogEntry(String eventLogEntry) {
        this.eventLogEntry = eventLogEntry;
    }

    public EventManaCost getEventManaCost() {
        return eventManaCost;
    }

    public void setEventManaCost(EventManaCost eventManaCost) {
        this.eventManaCost = eventManaCost;
    }
}
