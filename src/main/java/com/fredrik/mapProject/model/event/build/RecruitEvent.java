package com.fredrik.mapProject.model.event.build;

import com.fredrik.mapProject.model.building.BuildingType;
import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.databaseEntity.ManaEntity;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;
import com.fredrik.mapProject.model.databaseEntity.RegimentEntity;
import com.fredrik.mapProject.model.event.Event;
import com.fredrik.mapProject.model.event.dto.NewChildEventDataDTO;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.unit.UnitType;

import java.util.List;

public class RecruitEvent extends Event {

    public RecruitEvent(NewChildEventDataDTO event) {
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
        return "{}";
    }

    @Override
    public void parseFromEventData(String eventData) {}

    @Override
    public boolean processEvent(ManaEntity mana, GameMapManager gameMap) {

        MapTileEntity mapTile = gameMap.getTileFromCoordinates(getPrimaryTileCoordinates());

        boolean tileOwnedByPlayer = mapTile.getTileOwner() == getPlayerNr();

        if (!tileOwnedByPlayer) {
            setEventLogEntry(String.format("Could not recruit on tile %d:%d because not owned by player;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        boolean recruitBuildingPresent =  mapTile.getBuilding().getType() == BuildingType.BARRACK ||
                mapTile.getBuilding().getType() == BuildingType.ARCHERY_RANGE ||
                mapTile.getBuilding().getType() == BuildingType.STABLE;

        if (!recruitBuildingPresent) {
            setEventLogEntry(String.format("Could not recruit on tile %d:%d because no recruitment building present;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        boolean recruitBuildingIsComplete =  mapTile.getBuilding().isCompleted();

        if (!recruitBuildingIsComplete) {
            setEventLogEntry(String.format("Could not recruit on tile %d:%d because recruitment building is not functioning;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        List<ArmyEntity> armyList = gameMap.getArmiesFromCoordinates(getPrimaryTileCoordinates());


        boolean armyPresent = armyList.size() > 0;
        boolean hostileArmy;

        if (armyPresent) {
            boolean multipleArmies = armyList.size() > 1;
            hostileArmy = !(armyList.get(0).equals(getPlayerNr())) || multipleArmies ;
        } else {hostileArmy = false;}

        if (hostileArmy) {
            setEventLogEntry(String.format("Could not recruit on tile %d:%d because occupied by hostile army;",
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
        }

        UnitType unitType;
        switch (mapTile.getBuilding().getType()) {
            case BARRACK -> unitType = UnitType.INFANTRY;
            case ARCHERY_RANGE -> unitType = UnitType.ARCHERS;
            case STABLE -> unitType = UnitType.CAVALRY;
            default -> {setEventLogEntry("ERROR RECRUITMENT BUILDING NOT FOUND");
                return false;
            }
        }

        boolean eventManaCostPayed = mana.payInFull(getEventManaCost());

        if (!eventManaCostPayed) {
            setEventLogEntry(String.format("Not enough resources to recruit %s on tile %d:%d;",
                    unitType.getUnitName(),
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY()));
            return false;
        }

        ArmyEntity army;

        if (armyPresent) {
            army = armyList.get(0);
            setEventLogEntry(String.format("Recruited 1 %s on tile %d:%d to %s ;",
                    unitType.getUnitName(),
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY(),
                    army.getArmyName()));
        } else {
            army = new ArmyEntity(gameMap.getGameId(),
                    getPlayerNr(),
                    gameMap.getPlayerNextArmyNumber(getPlayerNr()),
                    getPrimaryTileCoordinates());

            setEventLogEntry(String.format("Recruited 1 %s on tile %d:%d and formed %s ;",
                    unitType.getUnitName(),
                    getPrimaryTileCoordinates().getX(),
                    getPrimaryTileCoordinates().getY(),
                    army.getArmyName()));
            gameMap.addArmy(army);
        }

        RegimentEntity regiment = new RegimentEntity(
                army.getArmyId(),
                unitType, army.getOwner(),
                army.regimentsSize(),
                0);// todo add equipment modifier

        army.getRegiments().add(regiment);

        return true;
    }
}
