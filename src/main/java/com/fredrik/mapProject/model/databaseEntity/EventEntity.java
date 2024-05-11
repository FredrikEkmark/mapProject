package com.fredrik.mapProject.model.databaseEntity;

import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.map.MapCoordinates;
import com.fredrik.mapProject.model.event.EventType;
import com.fredrik.mapProject.model.event.BuildEvent;
import com.fredrik.mapProject.model.event.ClaimTileEvent;
import com.fredrik.mapProject.model.event.Event;
import com.fredrik.mapProject.model.event.ExploreEvent;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "event_data")
public class EventEntity implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID eventId;

    @Column(nullable = false)
    private UUID gameId;

    @Column(nullable = false, length = 24)
    @Enumerated(EnumType.STRING)
    private Player playerNr;

    @Column(nullable = false)
    private int turn;

    @Embedded
    @Column(nullable = false)
    private MapCoordinates primaryTileCoordinates;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private EventType eventType;

    @Column(nullable = false)
    private String eventData;

    @Column(nullable = false)
    private String cost;

    public EventEntity() {
    }

    public EventEntity(UUID gameId, Player playerNr, int turn, MapCoordinates primaryTileCoordinates, EventType eventType, String eventData, String cost) {
        this.eventId = UUID.randomUUID();
        this.gameId = gameId;
        this.playerNr = playerNr;
        this.turn = turn;
        this.primaryTileCoordinates = primaryTileCoordinates;
        this.eventType = eventType;
        this.eventData = eventData;
        this.cost = cost;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
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

    public MapCoordinates getPrimaryTileCoordinates() {
        return primaryTileCoordinates;
    }

    public void setPrimaryTileCoordinates(MapCoordinates primaryTileCoordinates) {
        this.primaryTileCoordinates = primaryTileCoordinates;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Event getEvent() {

        switch (this.eventType) {
            case EXPLORE_EVENT -> {
                return new ExploreEvent(this.eventId,
                        this.playerNr,
                        this.turn,
                        this.primaryTileCoordinates,
                        this.eventType,
                        this.eventData,
                        this.cost);
            }
            case BUILD_EVENT -> {
                return new BuildEvent(this.eventId,
                        this.playerNr,
                        this.turn,
                        this.primaryTileCoordinates,
                        this.eventType,
                        this.eventData,
                        this.cost);
            }
            case CLAIM_TILE_EVENT -> {
                return new ClaimTileEvent(this.eventId,
                        this.playerNr,
                        this.turn,
                        this.primaryTileCoordinates,
                        this.eventType,
                        this.eventData,
                        this.cost);
            }
            default -> {
                System.out.println("ERROR, NO EVENT TYPE FOUND");
                return null;
            }
        }
    }

    public void setEvent(Event event) {
        this.eventData = event.stringifyEventData();
        this.eventId = event.getEventId();
        this.eventType = event.getEventType();
        this.turn = event.getTurn();
        this.primaryTileCoordinates = event.getPrimaryTileCoordinates();
        this.playerNr = event.getPlayerNr();
    }

    @Override
    public EventEntity clone() {
        try {
            return (EventEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            // Handle the exception appropriately
            return null;
        }
    }
}
