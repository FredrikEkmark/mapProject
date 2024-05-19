package com.fredrik.mapProject.model.map;

import com.fredrik.mapProject.model.battle.ArmyLocation;
import com.fredrik.mapProject.model.building.BuildingType;
import com.fredrik.mapProject.model.databaseEntity.ArmyEntity;
import com.fredrik.mapProject.model.databaseEntity.RegimentEntity;
import com.fredrik.mapProject.model.map.coordinates.PathCoordinates;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;
import com.fredrik.mapProject.model.map.terrain.Elevation;
import com.fredrik.mapProject.model.map.tile.MapTileEntityGenerator;
import com.fredrik.mapProject.model.player.PlayerStartPositionDTO;
import com.fredrik.mapProject.model.databaseEntity.GameSetupEntity;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;
import com.fredrik.mapProject.model.id.MapTileId;
import com.fredrik.mapProject.model.player.Player;

import java.util.*;
import java.util.stream.Collectors;

public class GameMapManager {

    private final Map<MapCoordinates, MapTileEntity> gameMap;
    private final Map<String, List<MapTileEntity>> playerMap;

    private final GameSetupEntity gameSetup;

    private final MapTileEntityGenerator mapTileEntityGenerator;

    private final List<MapTileEntity> updatedTiles;

    private final  Map<MapCoordinates, List<ArmyEntity>> armyMap;

    private final List<ArmyEntity> removedArmies;
    private final List<RegimentEntity> removedRegiments;

    private final Map<String,List<ArmyEntity>> playerArmyMap;


    public GameMapManager(List<MapTileEntity> mapTileEntityList, GameSetupEntity game, List<ArmyEntity> armyList) {
        this.gameMap = new HashMap<>();
        this.playerMap = new HashMap<>();
        this.playerArmyMap = new HashMap<>();
        this.armyMap = new HashMap<>();
        this.removedArmies = new ArrayList<>();
        this.removedRegiments = new ArrayList<>();
        this.gameSetup = game;
        this.mapTileEntityGenerator = new MapTileEntityGenerator(game.getSeed(), game.getMapSize().getWidth(), game.getMapSize().getHeight());
        populateMap(mapTileEntityList);
        distributeArmies(armyList);
        this.updatedTiles = new ArrayList<>(playerMap.size() * 100);
    }

    public UUID getGameId() {
        return gameSetup.getId();
    }

    private void populateMap(List<MapTileEntity> armyList) {
        for (MapTileEntity tile : armyList) {
            gameMap.put(tile.getMapTileId().getCoordinates(), tile);
            if (tile.getTileOwner() != Player.NONE) {
                playerMap.computeIfAbsent(tile.getTileOwner().name(), k -> new ArrayList<>()).add(tile);
            }
        }
    }

    private void distributeArmies(List<ArmyEntity> armyList) {
        for (ArmyEntity army : armyList) {
           armyMap.computeIfAbsent(army.getArmyCoordinates(), k -> new ArrayList<>()).add(army);
           playerArmyMap.computeIfAbsent(army.getOwner().name(), k -> new ArrayList<>()).add(army);
        }
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

    public List<ArmyEntity> getAllArmies() {
        return armyMap.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<ArmyEntity> getPlayerArmies(Player playerNr) {
        return playerArmyMap.getOrDefault(playerNr.name(), Collections.emptyList());
    }

    public List<ArmyEntity> getRemovedArmies() {
        return removedArmies;
    }


    public void removeArmy(ArmyEntity army) {
        playerArmyMap.get(army.getOwner().name()).remove(army);
        armyMap.remove(army.getArmyCoordinates());
        removedArmies.add(army);
        removedRegiments.addAll(army.getRegiments());
    }

    public void addArmy(ArmyEntity army) {
        playerArmyMap.computeIfAbsent(army.getOwner().name(), k -> new ArrayList<>()).add(army);
        armyMap.computeIfAbsent(army.getArmyCoordinates(), k -> new ArrayList<>()).add(army);
    }

    public List<RegimentEntity> getRemovedRegiments() {
        return removedRegiments;
    }

    public void removeRegiments(List<RegimentEntity> regiments) {
        removedRegiments.addAll(regiments);
    }

    // Utility functions

    public MapTileEntity getTileFromCoordinates(MapCoordinates mapCoordinates) {
        MapTileEntity tile = gameMap.get(mapCoordinates);

        if (tile == null) {
            tile = mapTileEntityGenerator.generateTile(new MapTileId(gameSetup.getId(), mapCoordinates.getX(), mapCoordinates.getY()));
            gameMap.put(tile.getMapTileId().getCoordinates(), tile);
        }
        return tile;
    }

    public ArmyEntity getArmyFromCoordinatesAndId(MapCoordinates coordinates, UUID armyId) {
        List<ArmyEntity> armyList = armyMap.get(coordinates);
        for (ArmyEntity army: armyList) {
            if (army.getArmyId().equals(armyId)) {
                return army;
            }
        }
        return null;
    }

    public List<ArmyEntity> getArmiesFromCoordinates(MapCoordinates coordinates) {
        return armyMap.getOrDefault(coordinates, new ArrayList<>());
    }

     public Map<MapCoordinates, ArmyLocation> getArmyLocationList() {

         Map<MapCoordinates, ArmyLocation> armyLocationMap = new HashMap<>();

         for (Map.Entry<MapCoordinates, List<ArmyEntity>> entry : armyMap.entrySet()) {
             MapCoordinates coordinates = entry.getKey();
             List<ArmyEntity> armiesList = entry.getValue();

             if (!armiesList.isEmpty()) {
                 armyLocationMap.put(coordinates, new ArmyLocation(armiesList, coordinates));
             }
         }
         return armyLocationMap;
    }



    // Army Movement


    public boolean moveArmy(ArmyEntity army, MapCoordinates destinationCoordinates) {
        int movement = army.getArmyMovement();
        MapCoordinates startCoordinates = army.getArmyCoordinates();
        boolean possibleMove = possibleArmyMove(true,
                army.getOwner(),
                movement,
                startCoordinates,
                destinationCoordinates);
        if (!possibleMove) {
            return false;
        }

        List<ArmyEntity> startPossitionList = armyMap.get(startCoordinates);
        if (startPossitionList != null) {
            startPossitionList.remove(army);
            if (startPossitionList.isEmpty()) {
                armyMap.remove(startCoordinates);
            }
        }

        List<ArmyEntity> armiesOnLocation = armyMap.computeIfAbsent(destinationCoordinates, k -> new ArrayList<>());

        for (ArmyEntity armyOnLocation : armiesOnLocation) {
            if (armyOnLocation.getOwner().equals(army.getOwner())) {
                armyOnLocation.addRegiments(army.getRegiments());
                army.getRegiments().clear();
                removeArmy(army);
            }
        }

        if (!army.getRegiments().isEmpty()) {
            armiesOnLocation.add(army);
            army.setArmyCoordinates(destinationCoordinates);
            army.setFortified(false);
        }

        return true;
    }

    private boolean possibleArmyMove(boolean isLandBased,
                                           Player player,
                                           int movement,
                                           MapCoordinates startCoordinates,
                                           MapCoordinates endCoordinates) {
        List<Elevation> traversableElevations = getTraversableElevations(isLandBased);

        Queue<PathCoordinates> queue = new LinkedList<>();
        queue.add(new PathCoordinates(startCoordinates));

        Set<MapCoordinates> visited = new HashSet<>();
        visited.add(startCoordinates);

        while (!queue.isEmpty()) {

            PathCoordinates currentCoordinates = queue.poll();

            if (currentCoordinates.getCoordinates().equals(endCoordinates)) {
                return true;
            }

            if (currentCoordinates.getPathIteration() < movement) {

                List<MapTileEntity> adjacentTiles = getAdjacentTraversableTilesFromCoordinates(
                        currentCoordinates.getCoordinates(), traversableElevations, player);

                for (MapTileEntity tile : adjacentTiles) {

                    MapCoordinates adjacentCoordinates = tile.getMapTileId().getCoordinates();

                    if (!visited.contains(adjacentCoordinates)) {

                        queue.add(new PathCoordinates(adjacentCoordinates, currentCoordinates.getNextPathIteration()));
                        visited.add(adjacentCoordinates);
                    }
                }
            }
        }
        return false;
    }

    private List<Elevation> getTraversableElevations(boolean isLandBased) {
        List<Elevation> traversableElevations = new ArrayList<>();
        if (isLandBased) {
            traversableElevations.add(Elevation.LOWLANDS);
            traversableElevations.add(Elevation.HIGHLANDS);
        } else {
            traversableElevations.add(Elevation.SHALLOW);
            traversableElevations.add(Elevation.DEEP);
        }
        return traversableElevations;
    }


    // Check Tile is Adjacent to functions

    public List<MapTileEntity> getPossibleRetreatLocations(MapCoordinates battleCoordinates, boolean isLandBased) {
        List<Elevation> traversableElevations = getTraversableElevations(isLandBased);
        List<MapCoordinates> adjacentCoordinatesList = getAdjacentTileCoordinates(battleCoordinates, 0);
        List<MapTileEntity> possibleRetreatLocations = new ArrayList<>();
        for (MapCoordinates mapCoordinates: adjacentCoordinatesList) {
            MapTileEntity tile = getTileFromCoordinates(mapCoordinates);
            if (traversableElevations.contains(tile.getTerrain().getElevation())) {
                possibleRetreatLocations.add(tile);
            }
        }
        return possibleRetreatLocations;
    }

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

        List<MapCoordinates> adjacentTileCoordinates = getAdjacentTileCoordinates(mapCoordinates, 0);

        for (MapCoordinates coordinates: adjacentTileCoordinates) {
            MapTileEntity tile = getTileFromCoordinates(coordinates);
            if (tile.getTileOwner().name().equals(player.name())) {
                return true;
            }
        }
        return false;
    }

    public List<MapTileEntity> getAdjacentTraversableTilesFromCoordinates(MapCoordinates startCoordinates, List<Elevation> traversableElevations, Player player) {

        List<MapCoordinates> adjacentCoordinates = getAdjacentTileCoordinates(startCoordinates, 0);
        List<MapTileEntity> adjacentTiles = new ArrayList<>(6);

        for (MapCoordinates coordinates: adjacentCoordinates) {
            MapTileEntity tile = getTileFromCoordinates(coordinates);
            if (traversableElevations.contains(tile.getTerrain().getElevation()) && tile.isVisible(player.number())) {
                adjacentTiles.add(tile);
            }
        }
        return adjacentTiles;
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

        MapCoordinates startPosition = new MapCoordinates();

        boolean notValidStart = true;

        MapTileEntity startTile;

        int[] xOffsets = {-1, -1, 0, 0, 1, 1};
        int[] yOffsets = {0, 1, -1, 1, 0, 1};

        while (notValidStart) {
            startPosition.setX(random.nextInt(6, xBounds - 7));
            startPosition.setY(random.nextInt(0, yBounds));

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

    public int getPlayerNextArmyNumber(Player player) {
        List<ArmyEntity> armyEntityList = playerArmyMap.get(player.name());

        if (armyEntityList == null) {
            return 1;
        }

        for (int i = 0; i < armyEntityList.size(); i++) {
            if (armyEntityList.get(i).getArmyNumber() != i + 1) {
                return i + 1;
            }
        }

        return armyEntityList.size() + 1;
    }
}
