package com.fredrik.mapProject.gameSetupDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
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

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
