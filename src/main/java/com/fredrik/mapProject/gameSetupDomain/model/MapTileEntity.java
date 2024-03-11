package com.fredrik.mapProject.gameSetupDomain.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "map_tile")
public class MapTileEntity {

    @EmbeddedId
    private MapTileId mapTileId;

    private int tileValue;

    public MapTileEntity() {
    }

    public MapTileId getMaptileId() {
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
}
