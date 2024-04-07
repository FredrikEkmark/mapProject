package com.fredrik.mapProject.gameRunDomain;

import com.fredrik.mapProject.gameRunDomain.model.EventLogEntity;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;

import java.util.List;

public class TurnChange {

    private GameSetupEntity gameSetup;

    private List<MapTileEntity> gameMap;

    private List<EventLogEntity> eventLogList;

    private boolean updated = false;

    public TurnChange(GameSetupEntity gameSetup, List<MapTileEntity> gameMap, List<EventLogEntity> eventLogList) {
        this.gameSetup = gameSetup;
        this.gameMap = gameMap;
        this.eventLogList = eventLogList;
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
        System.out.println("Turn has been updated");
    }

    public GameSetupEntity getGameSetup() {
        if (updated) {
            update();
        }
        return gameSetup;
    }

    public List<MapTileEntity> getGameMap() {
        if (updated) {
            update();
        }
        return gameMap;
    }

    public List<EventLogEntity> getEventLogList() {
        if (updated) {
            update();
        }
        return eventLogList;
    }
}
