package com.fredrik.mapProject.gameSetupDomain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gameRunDomain.model.terrain.Terrain;
import com.fredrik.mapProject.gameRunDomain.model.building.*;
import jakarta.persistence.*;

@Entity
@Table(name = "game_map_tile")
public class MapTileEntity {

    @EmbeddedId
    private MapTileId mapTileId;

    @Column(nullable = false)
    private int tileValue;

    @Column(nullable = false, length = 24)
    @Enumerated(EnumType.STRING)
    private Player tileOwner;

    @Column(nullable = false, length = 32)
    private String visibility;

    @Column(nullable = false, length = 128)
    private String building;

    @Column(nullable = false, length = 128)
    private String unit;

    public MapTileEntity() {
    }

    public MapTileId getMapTileId() {
        return mapTileId;
    }

    public void setMapTileId(MapTileId maptileId) {
        this.mapTileId = maptileId;
    }

    public int getTileValue() {
        return tileValue;
    }

    public void setTileValue(int tileValue) {
        this.tileValue = tileValue;
    }

    public Player getTileOwner() {
        return tileOwner;
    }

    public void setTileOwner(Player tileOwner) {
        this.tileOwner = tileOwner;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public boolean isVisible(int playerNumber) {
        // Ensure playerNumber is within valid range
        if (playerNumber < 1 || playerNumber > visibility.length()) {
            throw new IllegalArgumentException("Invalid player number");
        }

        // Get the character at the corresponding position in the visibility string
        char visibilityChar = visibility.charAt(playerNumber - 1);

        // Check if the character is '1' (visible) or '0' (not visible)
        return visibilityChar == '1';
    }

    // Method to set visibility for a specific player
    public void setPlayerVisibility(boolean visible, int playerNumber) {
        // Ensure playerNumber is within valid range
        if (playerNumber < 1 || playerNumber > visibility.length()) {
            throw new IllegalArgumentException("Invalid player number");
        }

        // Get the current visibility bit for the player
        char currentVisibilityChar = visibility.charAt(playerNumber - 1);

        // Update the visibility bit if it's different from the desired visibility
        if ((currentVisibilityChar == '1' && !visible) || (currentVisibilityChar == '0' && visible)) {
            StringBuilder builder = new StringBuilder(visibility);
            builder.setCharAt(playerNumber - 1, visible ? '1' : '0');
            this.visibility = builder.toString();
        }
    }

    public String getBuildingJsonString() {
        return building;
    }

    public Building getBuilding() {

        ObjectMapper objectMapper = new ObjectMapper();

        BuildingType type = BuildingType.NONE;
        int progress = 0;

        try {
            JsonNode rootNode = objectMapper.readTree(building);

            JsonNode typeNode = rootNode.get("type");
            JsonNode progressNode = rootNode.get("progress");

            if (typeNode != null) {
                type = BuildingType.valueOf(typeNode.asText());
            }

            if (progressNode != null && progressNode.isInt()) {
                progress = progressNode.asInt();
            }
        } catch (RuntimeException | JsonProcessingException e) {
            System.out.println(e);
        }

        return switch (type) {
            case NONE -> new NoBuilding(BuildingType.NONE, progress);
            case FARM -> new Farm(type, progress);
            case GRANARY -> new Granary(type, progress);
            case QUARRY -> new Quarry(type, progress);
            case LUMBER_CAMP -> new LumberCamp(type, progress);
            case CARPENTRY -> new Carpentry(type, progress);
            case RANCH -> new Ranch(type, progress);
            case LEATHER_WORKER -> new LeatherWorker(type, progress);
            case FISHERY -> new Fishery(type, progress);
            case VILLAGE -> new Village(type, progress);
            case TOWN -> new Town(type, progress);
            case CITY -> new City(type, progress);
        };
    }

    public void setBuildingJsonString(String building) {
        this.building = building;
    }

    public void setBuilding(Building building) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            BuildingJson buildingJson = new BuildingJson();
            buildingJson.setType(building.getType());
            buildingJson.setProgress(building.getProgress());

            this.building = objectMapper.writeValueAsString(buildingJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Terrain getTerrain() {
        switch (tileValue) {
            case 111, 112, 113, 211, 212, 213, 311, 312, 313 -> {return Terrain.GLACIER;}  // Glacier
            case 123,  133, 143, 153 -> {return Terrain.DEEP_WATER;}  // Deep Water
            case 223, 233, 243, 253 -> {return Terrain.COASTAL_WATER;}  // Coastal Water
            case 322, 323 -> {return Terrain.LOWLAND_TUNDRA;} // Lowland Tundra
            case 422, 423 -> {return Terrain.HIGHLANDS_TUNDRA;} // Highlands Tundra
            case 321 -> {return Terrain.COLD_DESERT;} // Cold Desert
            case 421 -> {return Terrain.COLD_DESERT_HILLS;} // Cold Desert Hills
            case 331 -> {return Terrain.TEMPERATE_LOWLANDS_PLAINS;}  // Temperate Lowlands Plains
            case 332 -> {return Terrain.TEMPERATE_FOREST;}  // Temperate Forest
            case 431 -> {return Terrain.HIGHLAND_HILLS;}  // Highland Hills
            case 432 -> {return Terrain.TEMPERATE_HIGHLAND_FOREST;}  // Temperate Highland Forest
            case 333 -> {return Terrain.TEMPERATE_RAINFOREST;}  // Temperate Rainforest
            case 433 -> {return Terrain.TEMPERATE_HIGHLAND_RAINFOREST;}  // Temperate Highland Rainforest
            case 341 -> {return Terrain.HOT_DESERT;} // Hot Desert
            case 441 -> {return Terrain.HOT_DESERT_HILLS;} // Hot Desert Hills
            case 342 -> {return Terrain.HOT_STEPPE;} // Hot Steppe
            case 442 -> {return Terrain.HOT_STEPPE_HILLS;} // Hot Steppe hills
            case 351, 352, 343 -> {return Terrain.TROPICAL_SAVANNA;} // Tropical Savanna
            case 451, 452, 443 -> {return Terrain.TROPICAL_SAVANNA_HILLS;} // Tropical Savanna Hills
            case 353 -> {return Terrain.TROPICAL_RAINFOREST;} // Tropical Rainforest
            case 453 -> {return Terrain.TROPICAL_RAINFOREST_HILLS;} // Tropical Rainforest Hills
            case 411, 412, 413 -> {return Terrain.GLACIAL_HEIGHTS;} // Glacial Heights
            case 511, 521, 531, 541, 551 -> {return Terrain.MOUNTAINS;} // Frozen Mountains
            default -> {return null;}
        }
    }
}
