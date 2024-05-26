package com.fredrik.mapProject.model.turnChange;

import com.fredrik.mapProject.config.Roles;
import com.fredrik.mapProject.model.building.BuildingType;
import com.fredrik.mapProject.model.building.Village;
import com.fredrik.mapProject.model.databaseEntity.*;
import com.fredrik.mapProject.model.id.MapTileId;
import com.fredrik.mapProject.model.map.GameMapManager;
import com.fredrik.mapProject.model.map.MapSizes;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.map.tile.MapTileEntityGenerator;
import com.fredrik.mapProject.model.player.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class TurnChangeTest {

    @Test
    void populationIncreaseTest() {
        // Given
        List<EventEntity> eventEntityList = new ArrayList<>();
        TurnChange turnChange = seedTurnChange(eventEntityList);
        turnChange.getManaList().get(0).depositFood(5);

        // When
        turnChange.update();
        int population = turnChange.getManaList().get(0).getPopulation();

        // Then
        Assertions.assertEquals(510, population);
    }

    private UserEntity seedUser() {
        return new UserEntity(
                "Fredrik",
                "1234",
                "fredrik@gmail.com",
                Roles.HOST
        );
    }

    private GameSetupEntity seedGameSetup(UserEntity user) {
        return new GameSetupEntity(
                user,
                "111111111111111111111111:00",
                MapSizes.SMALL,
                99999
        );
    }

    private GamePlayerEntity seedGamePlayer(GameSetupEntity gameSetup, UserEntity user, Player player) {

        MapCoordinates startCoordinates = new MapCoordinates(12, 25);

        return new GamePlayerEntity(gameSetup.getId(), user.getId(), startCoordinates, player);
    }

    private ManaEntity seedMana(GamePlayerEntity gamePlayer) {
        return new ManaEntity(gamePlayer.getManaId(), gamePlayer.getPlayerGameId().getUserId(), gamePlayer.getPlayerNr());
    }

    private GameMapManager seedMap(GameSetupEntity gameSetup, Player player) {
        MapTileEntityGenerator mapTileEntityGenerator = new MapTileEntityGenerator(gameSetup.getSeed(),gameSetup.getMapSize().getWidth(), gameSetup.getMapSize().getHeight());
        List<MapTileEntity> mapTileEntityList = new ArrayList<>(7);
        List<ArmyEntity> armyEntityList = new ArrayList<>();

        List<MapCoordinates> mapCoordinates = new ArrayList<>(7);
        MapCoordinates startCoordinates = new MapCoordinates(12, 25);

        mapCoordinates.add(startCoordinates);
        mapCoordinates.add(new MapCoordinates(11, 24));
        mapCoordinates.add(new MapCoordinates(11, 25));
        mapCoordinates.add(new MapCoordinates(12, 24));
        mapCoordinates.add(new MapCoordinates(12, 26));
        mapCoordinates.add(new MapCoordinates(13, 24));
        mapCoordinates.add(new MapCoordinates(13, 25));


        for (MapCoordinates coordinates : mapCoordinates) {
            MapTileEntity tile = mapTileEntityGenerator.generateTile(new MapTileId(gameSetup.getId(), coordinates.getX(), coordinates.getY()));
            mapTileEntityList.add(tile);
        }

        MapTileEntity startTile = mapTileEntityList.get(0);
        startTile.setTileOwner(player);
        startTile.setBuilding(new Village(BuildingType.VILLAGE, 800));

        return  new GameMapManager(mapTileEntityList, gameSetup, armyEntityList);
    }

    private TurnChange seedTurnChange(List<EventEntity> eventEntityList) {
        UserEntity user = seedUser();
        GameSetupEntity gameSetup = seedGameSetup(user);
        GamePlayerEntity gamePlayer = seedGamePlayer(gameSetup, user, Player.PLAYER_ONE);
        GameMapManager gameMapManager = seedMap(gameSetup, gamePlayer.getPlayerNr());
        List<ManaEntity> manaEntityList = new ArrayList<>(1);
        manaEntityList.add(seedMana(gamePlayer));

        return new TurnChange(gameSetup, gameMapManager, eventEntityList , manaEntityList);
    }

}
