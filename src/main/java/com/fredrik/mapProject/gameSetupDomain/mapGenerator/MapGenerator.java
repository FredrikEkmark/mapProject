package com.fredrik.mapProject.gameSetupDomain.mapGenerator;

import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import com.fredrik.mapProject.gameSetupDomain.mapGenerator.tile.TileGenerator2;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileId;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MapGenerator
{

    private static Random random = new Random();
    public static MapTileEntity[][] generateMap(UUID gameId, int seed, int height, int width) {

        TileGenerator2 tileGenerator = new TileGenerator2(seed, width, height);

        MapTileEntity[][] map = new MapTileEntity[width][height];

        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {

                MapTileId mapTileId = new MapTileId(gameId, x, y);
                MapTileEntity tile = tileGenerator.tileValue(mapTileId);
                map[y][x] = tile; // switched them
            }
        }
        return map;
    }

    public static List<MapCoordinates> generateStartLocations(MapTileEntity[][] map) {

        int initialCapacity = map.length * map[0].length;

        List<MapCoordinates> possibleStartLocations = new ArrayList<>(initialCapacity);

        for (int i = 10; i < map.length - 10; i++) {
            for (int j = 10; j < map[0].length - 10; j++) {
                MapTileEntity tile = map[i][j];
                if (tile.getTileValue() > 299 && tile.getTileValue() < 499) {
                    possibleStartLocations.add(tile.getMapTileId().getCoordinates());
                }
            }
        }

        List<MapCoordinates> startLocations = new ArrayList<>(12);
        int loops = 0;

        while (startLocations.size() < 12) {
            loops++;

            MapCoordinates newStartLocation = possibleStartLocations.get(random.nextInt(0, possibleStartLocations.size()));

            boolean isTooClose = false;

            for (MapCoordinates startLocation : startLocations) {
                int dx = newStartLocation.getX() - startLocation.getX();
                int dy = newStartLocation.getY() - startLocation.getY();

                // Adjust dx for odd rows
                if (startLocation.getY() % 2 == 1) {
                    dx -= 0.5; // Offset for odd rows
                }

                // Calculate Euclidean distance between the two points
                double distance = Math.sqrt(dx * dx + dy * dy);

                // Check if distance is less than or equal to 2 tiles
                if (distance <= 2) {
                    isTooClose = true;
                    break; // No need to check other start locations if one is too close
                }
            }

            // Add the new start location if it's not too close to any existing start location
            if (!isTooClose) {
                startLocations.add(newStartLocation);
            }

            if (loops > 1000) {
                System.out.println("ToManyLOOPS");
                return null;
            }
        }

        return startLocations;
    }
}
