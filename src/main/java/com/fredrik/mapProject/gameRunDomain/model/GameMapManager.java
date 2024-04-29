package com.fredrik.mapProject.gameRunDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.GamePlayerEntity;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gamePlayDomain.model.PlayerStartPositionDTO;
import com.fredrik.mapProject.gameRunDomain.model.building.BuildingType;
import com.fredrik.mapProject.gameSetupDomain.MapSizes;
import com.fredrik.mapProject.gameSetupDomain.mapGenerator.tile.TileGenerator;
import com.fredrik.mapProject.gameSetupDomain.model.GameSetupEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileId;

import java.util.*;

public class GameMapManager {

    private Map<MapCoordinates, MapTileEntity> gameMap;
    private Map<String, List<MapTileEntity>> playerMap;

    private GameSetupEntity gameSetup;

    private TileGenerator tileGenerator;

    private List<MapTileEntity> updatedTiles;

    public GameMapManager(List<MapTileEntity> mapTileEntityList, GameSetupEntity game) {
        this.gameMap = new HashMap<>();
        this.playerMap = new HashMap<>();
        this.gameSetup = game;
        this.tileGenerator = new TileGenerator(game.getSeed(), game.getMapSize().getWidth(), game.getMapSize().getHeight());
        populateMap(mapTileEntityList);
        this.updatedTiles = new ArrayList<>(playerMap.size() * 100);
    }

    private void populateMap(List<MapTileEntity> mapTileEntityList) {
        for (MapTileEntity tile : mapTileEntityList) {
            gameMap.put(tile.getMapTileId().getCoordinates(), tile);
            Player player = tile.getTileOwner();
            if (player != Player.NONE) {
                playerMap.computeIfAbsent(player.name(), k -> new ArrayList<>()).add(tile);
            }
        }
    }

    public List<MapTileEntity> getGameMap() {
        return new ArrayList<>(gameMap.values());
    }

    public List<MapTileEntity> getTilesWithPlayer(Player player) {
        return playerMap.getOrDefault(player.name(), Collections.emptyList());
    }

    public List<MapTileEntity> getUpdatedTiles() {
        return updatedTiles;
    }

    public void addTileToUpdatedTiles(MapTileEntity tile) {
        updatedTiles.add(tile);
    }

    // Utility functions

    public MapTileEntity getTileFromCoordinates(MapCoordinates mapCoordinates) {
        MapTileEntity tile = gameMap.get(mapCoordinates);

        if (tile == null) {
            tile = tileGenerator.generateTile(new MapTileId(gameSetup.getId(), mapCoordinates.getX(), mapCoordinates.getY()));
        }
        return tile;
    }


    // Check Tile is Adjacent to functions

    public boolean isTileVisibleAdjacentToPlayer(MapCoordinates mapCoordinates, Player player) {

        List<MapCoordinates> adjacentTileCoordinates = getAdjacentTileCoordinates(mapCoordinates, 0);

        for (MapCoordinates coordinates: adjacentTileCoordinates) {
            MapTileEntity tile = getTileFromCoordinates(coordinates);
            if (tile.isVisible(player.number())) {
                return true;
            }
        }
        return false;
    }

    public boolean isTileOwnerAdjacentToPlayer(MapCoordinates mapCoordinates, Player player) {

        System.out.println(player);

        List<MapCoordinates> adjacentTileCoordinates = getAdjacentTileCoordinates(mapCoordinates, 0);

        System.out.println("Prime X:" + mapCoordinates.getX() + " Y:" + mapCoordinates.getY());
        for (MapCoordinates t: adjacentTileCoordinates) {
            System.out.println("X:" + t.getX() + " Y:" + t.getY());
        }

        for (MapCoordinates coordinates: adjacentTileCoordinates) {
            MapTileEntity tile = getTileFromCoordinates(coordinates);
            if (tile.getTileOwner().name() == player.name()) {
                return true;
            }
        }
        return false;
    }

    protected List<MapCoordinates> getAdjacentTileCoordinates(MapCoordinates mapCoordinates, int tileDistance) {

        boolean isEven = mapCoordinates.getX() % 2 == 0;
        List<MapCoordinates> adjacentMapCoordinates = new ArrayList<>();
        if (isEven) {
            for (int i = 0; i < tileDistance + 1; i++) {
                int topRow = 2 + i;
                for (int j = 0; j < topRow; j++) {
                    int xOffset = -1 - i;
                    int yOffset = (int) (- 1 - Math.floor(i / 2) + j);
                    adjacentMapCoordinates.add(new MapCoordinates(mapCoordinates.getX() + xOffset,
                            mapCoordinates.getY() + yOffset));
                }
                for (int j = 0; j < topRow-1; j++) {
                    int xOffset = j - i;
                    int  jValue = (int) (i % 2 == 0 ? Math.floor( j / 2) : Math.ceil( j / 2));
                    int yOffset = (int) (1 + Math.floor(i / 2) + jValue);
                    adjacentMapCoordinates.add(new MapCoordinates(mapCoordinates.getX() + xOffset,
                            mapCoordinates.getY() + yOffset));
                }
                for (int j = 0; j < topRow-1; j++) {
                    int xOffset = j + 1;
                    int yOffset = (int) (i - Math.floor(j / 2));
                    adjacentMapCoordinates.add(new MapCoordinates(mapCoordinates.getX() + xOffset,
                            mapCoordinates.getY() + yOffset));
                }
                for (int j = 0; j < topRow-1; j++) {
                    int xOffset = i + 1;
                    int yOffset = (int) (- 1 + Math.ceil(i / 2) - j);
                    adjacentMapCoordinates.add(new MapCoordinates(mapCoordinates.getX() + xOffset,
                            mapCoordinates.getY() + yOffset));
                }
                for (int j = 0; j < topRow-1; j++) {
                    int xOffset = i - j;
                    int jValue = (int) (i % 2 == 0 ? Math.ceil( j / 2) : Math.floor( j / 2));
                    int yOffset = (int) (-1 - Math.ceil(i / 2) - jValue);
                    adjacentMapCoordinates.add(new MapCoordinates(mapCoordinates.getX() + xOffset,
                            mapCoordinates.getY() + yOffset));
                }
                for (int j = 0; j < topRow - 2; j++) {
                    int xOffset = - 1 - j;
                    int yOffset = (int) (- 1  - i + Math.ceil(j / 2));
                    adjacentMapCoordinates.add(new MapCoordinates(mapCoordinates.getX() + xOffset,
                            mapCoordinates.getY() + yOffset));
                }
            }
        } else {
            for (int i = 0; i < tileDistance + 1; i++) {
                int topRow = 2 + i;
                for (int j = 0; j < topRow; j++) {
                    int xOffset = -1 - i;
                    int yOffset = (int) (- Math.ceil(i / 2) + j);
                    adjacentMapCoordinates.add(new MapCoordinates(mapCoordinates.getX() + xOffset,
                            mapCoordinates.getY() + yOffset));
                }
                for (int j = 0; j < topRow-1; j++) {
                    int xOffset = j - i;
                    int jxValue = (int) (i % 2 == 0 ? Math.ceil( j / 2) : Math.floor( j / 2));
                    int yOffset = (int) (1 + Math.ceil(i / 2) + jxValue);
                    adjacentMapCoordinates.add(new MapCoordinates(mapCoordinates.getX() + xOffset,
                            mapCoordinates.getY() + yOffset));
                }
                for (int j = 0; j < topRow-1; j++) {
                    int xOffset = j + 1;
                    int yOffset = (int) (1 + i - Math.ceil(j / 2));
                    adjacentMapCoordinates.add(new MapCoordinates(mapCoordinates.getX() + xOffset,
                            mapCoordinates.getY() + yOffset));
                }
                for (int j = 0; j < topRow-1; j++) {
                    int xOffset = i + 1;
                    int yOffset = (int) (Math.floor(i / 2) - j);
                    adjacentMapCoordinates.add(new MapCoordinates(mapCoordinates.getX() + xOffset,
                            mapCoordinates.getY() + yOffset));
                }
                for (int j = 0; j < topRow-1; j++) {
                    int xOffset = i - j;
                    int jxValue = (int) (i % 2 == 0 ? Math.floor( j / 2) : Math.ceil( j / 2));
                    int yOffset = (int) (-1 - Math.floor(i / 2) - jxValue);
                    adjacentMapCoordinates.add(new MapCoordinates(mapCoordinates.getX() + xOffset,
                            mapCoordinates.getY() + yOffset));
                }
                for (int j = 0; j < topRow - 2; j++) {
                    int xOffset = - 1 - j;
                    int yOffset = (int) (- i + Math.floor(j / 2));
                    adjacentMapCoordinates.add(new MapCoordinates(mapCoordinates.getX() + xOffset,
                            mapCoordinates.getY() + yOffset));
                }
            }
        }
        return adjacentMapCoordinates;
    }

    public PlayerStartPositionDTO createNewStartPosition() {

        List<Player> allPlayerNumbers = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(Player.values(), 0, gameSetup.getMapSize().getMaxPlayers())));

        Player player = Player.NONE;

        for (Player playerFromList: allPlayerNumbers) {
            if (playerMap.get(playerFromList.name()) == null) {
                player = playerFromList;
                break;
            }
        }

        if (player == Player.NONE) {
            return new PlayerStartPositionDTO(player , new MapCoordinates() , new ArrayList<>(7));
        }

        Random random = new Random();

        int xBounds = gameSetup.getMapSize().getHeight();
        int yBounds = gameSetup.getMapSize().getWidth();

        System.out.println(xBounds);
        System.out.println(yBounds);

        MapCoordinates startPosition = new MapCoordinates();

        int[] xOffsets = {-1, -1, 0, 0, 1, 1};
        int[] yOffsets = {0, 1, -1, 1, 0, 1};

        boolean notValidStart = true;

        MapTileEntity startTile;

        while (notValidStart) {
            System.out.println("ran While Loop");
            startPosition.setX(random.nextInt(0, xBounds));
            startPosition.setY(random.nextInt(20, yBounds - 20));
            System.out.println(startPosition.getX() + " " + startPosition.getY());

            if (startPosition.getX() % 2 == 0) {
                yOffsets[1] = -1;
                yOffsets[5] = -1;
            } else {
                yOffsets[1] = 1;
                yOffsets[5] = 1;
            }

            startTile = getTileFromCoordinates(startPosition);

            if (startTile.getTileOwner() == Player.NONE) {
                if (startTile.getTileValue() >= 300 && startTile.getTileValue() < 500 ) {
                    notValidStart = false;
                    for (int i = 0; i < xOffsets.length; i++) {
                        MapTileEntity tile = getTileFromCoordinates(new MapCoordinates(
                                startPosition.getX() + xOffsets[i],
                                startPosition.getY() + yOffsets[i]));
                        if (tile.getTileOwner() != Player.NONE) {
                            notValidStart = true;
                        }
                    }
                }
            }
        }

        startTile = getTileFromCoordinates(startPosition);

        startTile.setPlayerVisibility(true, player.number());
        startTile.setTileOwner(player);
        startTile.setBuildingJsonString("{\"type\": \"VILLAGE\", \"progress\": " + BuildingType.VILLAGE.getCompleteAtProgress() + "}");

        List<MapTileEntity> startTiles = new ArrayList<>(7);

        startTiles.add(startTile);

        for (int i = 0; i < xOffsets.length; i++) {
            MapTileEntity tile = getTileFromCoordinates(new MapCoordinates(
                    startPosition.getX() + xOffsets[i],
                    startPosition.getY() + yOffsets[i]));
            tile.setPlayerVisibility(true, player.number());
            startTiles.add(tile);
        }

        return new PlayerStartPositionDTO(player , startPosition , startTiles);
    }
}
