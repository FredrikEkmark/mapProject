package com.fredrik.mapProject.gameRunDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;

import java.util.UUID;

public class BuildEvent extends Event {
    public BuildEvent(UUID eventId, Player playerNr, int turn, MapCoordinates primaryTileCoordinates, EventType eventType, String eventData) {
        super(eventId, playerNr, turn, primaryTileCoordinates, eventType);
        parseFromEventData(eventData);
    }

    private void parseFromEventData(String eventData) {
        // toDo add logic to distribute the eventData to the correct class variables
    }

    @Override
    public String parseToEventData() {
        // toDo add logic to return the correct jsonData
        return null;
    }
}
