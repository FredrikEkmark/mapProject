package com.fredrik.mapProject.model.databaseEntity;

import com.fredrik.mapProject.model.event.*;
import com.fredrik.mapProject.model.event.build.BuildEvent;
import com.fredrik.mapProject.model.event.build.DemolishEvent;
import com.fredrik.mapProject.model.event.dto.NewChildEventDataDTO;
import com.fredrik.mapProject.model.event.expand.ClaimTileEvent;
import com.fredrik.mapProject.model.event.expand.ExploreEvent;
import com.fredrik.mapProject.model.event.unit.*;
import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
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

        NewChildEventDataDTO newChildEventData = new NewChildEventDataDTO(
                this.eventId,
                this.playerNr,
                this.turn,
                this.primaryTileCoordinates,
                this.eventType,
                this.eventData,
                this.cost
        );

        switch (this.eventType) {
            case EXPLORE_EVENT -> {
                return new ExploreEvent(newChildEventData);
            }
            case BUILD_EVENT -> {
                return new BuildEvent(newChildEventData);
            }
            case CLAIM_TILE_EVENT -> {
                return new ClaimTileEvent(newChildEventData);
            }
            case DEMOLISH_EVENT -> {
                return new DemolishEvent(newChildEventData);
            }
            case CONQUER_EVENT -> {
                return new ConquerEvent(newChildEventData);
            }
            case MOVE_EVENT -> {
                return new MoveEvent(newChildEventData);
            }
            case SPLIT_ARMY_EVENT -> {
                return new SplitArmyEvent(newChildEventData);
            }
            case FORTIFY_EVENT -> {
                return new FortifyEvent(newChildEventData);
            }
            case DISMISS_EVENT ->  {
                return new DismissEvent(newChildEventData);
            }
            case RECRUIT_EVENT -> {
                return new RecruitEvent(newChildEventData);
            }
            case RAID_EVENT -> {
                return new RaidEvent(newChildEventData);
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
