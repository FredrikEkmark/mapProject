package com.fredrik.mapProject.gameRunDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;

public class ClaimTileEvent extends Event {
    public ClaimTileEvent(EventId eventId, Player playerNr, int turn, EventType eventType, String eventData) {
        super(eventId, playerNr, turn, eventType);
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
