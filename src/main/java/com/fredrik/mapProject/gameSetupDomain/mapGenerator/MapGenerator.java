package com.fredrik.mapProject.gameSetupDomain.mapGenerator;

import com.fredrik.mapProject.gameSetupDomain.mapGenerator.tile.TileInterpretation;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileId;

import java.util.UUID;

public class MapGenerator
{
    public static MapTileEntity[][] generateMap(UUID gameId, int seed, int height, int width) {

        TileInterpretation tileInterpretation = new TileInterpretation(seed, width, height);

        MapTileEntity[][] map = new MapTileEntity[height][width];

        for (int y = 0; y < height; y++)
        {
            int oddOrEven = 0;
            if (y % 2 == 1) {
                oddOrEven = 1;
            }
            for (int x = 0; x < (width - oddOrEven); x++) {

                MapTileId mapTileId = new MapTileId(gameId, x, y);
                MapTileEntity tile = tileInterpretation.tileValue(mapTileId);
                map[y][x] = tile;
            }
        }
        return map;
    }
}
