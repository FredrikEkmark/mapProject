package com.fredrik.mapProject.gameRunDomain;

import com.fredrik.mapProject.gameRunDomain.model.EventEntity;
import com.fredrik.mapProject.gameRunDomain.model.EventLogEntity;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;

import java.util.ArrayList;
import java.util.List;

public class TurnChange {

    private GameSetupEntity gameSetup;

    private List<MapTileEntity> gameMap;

    private List<EventEntity> eventList;

    private List<EventLogEntity> eventLog;


    private boolean updated = false;

    public TurnChange(GameSetupEntity gameSetup, List<MapTileEntity> gameMap, List<EventEntity> eventList) {
        this.gameSetup = gameSetup;
        this.gameMap = gameMap;
        this.eventList = eventList;
        this.eventLog = new ArrayList<EventLogEntity>();
    }

    public void update() {
        if (updated) {
            System.out.println("Turn already updated");
            return;
        }

        // toDo write update logic

        gameSetup.setTurn(gameSetup.getTurn() + 1); // ticks the turn up one
        updated = true;
        gameSetup.setUpdating(false);
        System.out.printf("""
                GameId: %s
                Turn has been updated
                """, gameSetup.getId()); // toDo write full log for turn update, number of tiles changed, eventLogs remaining...
    }

    public GameSetupEntity getGameSetup() {
        if (!updated) {
            update();
        }
        return gameSetup;
    }

    public List<MapTileEntity> getGameMap() {
        if (!updated) {
            update();
        }
        return gameMap;
    }

    public List<EventEntity> getEventList() {
        if (!updated) {
            update();
        }
        return eventList;
    }

    public List<EventLogEntity> getEventLog() {
        if (!updated) {
            update();
        }
        return eventLog;
    }
}
