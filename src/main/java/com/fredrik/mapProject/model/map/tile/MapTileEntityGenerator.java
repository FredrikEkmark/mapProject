package com.fredrik.mapProject.model.map.tile;

import com.fredrik.mapProject.model.player.Player;
import com.fredrik.mapProject.model.map.MapSizes;
import com.fredrik.mapProject.model.map.PerlinNoise.OpenSimplex;
import com.fredrik.mapProject.model.databaseEntity.MapTileEntity;
import com.fredrik.mapProject.model.id.MapTileId;


public class MapTileEntityGenerator {

    private final int seed;
    private final int width;
    private final int widthPart;
    private final int height;
    private final int heightPart;

    public MapTileEntityGenerator(int seed, int width, int height) {

        this.seed = seed;
        this.height = height;
        this.heightPart = height/10;
        this.width = width;
        this.widthPart = width/10;
    }

    public MapTileEntity generateTile(MapTileId mapTileId) {

        MapTileEntity tile = new MapTileEntity();
        tile.setMapTileId(mapTileId);
        tile.setTileOwner(Player.NONE);
        tile.setVisibility("00000000000000000000000000000000");
        tile.setBuildingJsonString("{\"type\": \"NONE\", \"progress\": 0}");

        int y = mapTileId.getCoordinates().getY();
        int x = mapTileId.getCoordinates().getX();

        double frequency1 = 1 / 36;
        double frequency2 = 1 / 120;
        double frequency3 = 1 / 360;
        double frequency4 = 1 / 24.0;
        double frequency5 = 1 / 3.0;

        double terrainNoiseValue = threeFrequencyNoise(y, x, frequency1, frequency2, frequency3);

        double difNoiseValue = twoFrequencyNoise(y, x, frequency4, frequency5);
        double precipitationNoiseValue = singleFrequencyNoise(y, x, frequency2) + (difNoiseValue * 0.15);

        int terrainValue = terrain(terrainNoiseValue, difNoiseValue , y, x);

        int temperatureValue = temperature(x, difNoiseValue);
        int precipitationValue = precipitation(precipitationNoiseValue, terrainValue);

        tile.setTileValue(terrainValue + temperatureValue + precipitationValue);
        return tile;
    }

    private Double threeFrequencyNoise(int x, int y, double frequency1, double frequency2, double frequency3) {
        double value1 = OpenSimplex.noise3_ImproveXY(seed, x * frequency1, y * frequency1, 0.0);
        double value2 = OpenSimplex.noise3_ImproveXY(seed, x * frequency2, y * frequency2, 0.0);
        double value3 = OpenSimplex.noise3_ImproveXY(seed, x * frequency3, y * frequency3, 0.0);

        return ((value1 + value2 + value3 + value3) / 4);
    }

    private double twoFrequencyNoise(int x, int y, double frequency1, double frequency2) {
        double value = OpenSimplex.noise3_ImproveXY(seed/2, x * frequency1, y * frequency1, 0.0);
        value += OpenSimplex.noise3_ImproveXY(seed/2, x * frequency2, y * frequency2, 0.0);

        return value;
    }

    private double singleFrequencyNoise(int x, int y, double frequency1) {
        return OpenSimplex.noise3_ImproveXY(seed/2, x * frequency1, y * frequency1, 0.0);
    }

    private int terrain(Double valuesCombined, double difValue, int x, int y) {


        return getTerrainResult(valuesCombined, difValue, x);
    }

    private int getTerrainResult(Double valuesCombined, double difValue, int x) {

        return terrainMapping(valuesCombined, difValue);
    }

    private int terrainMapping(Double terrainNoise, double difValue) {

        terrainNoise = terrainNoise * -1 + (difValue*0.05);

        if (terrainNoise < -0.03) {
            return 100;
        } else if (terrainNoise < 0) {
            if (difValue > 1.1) {
                return 300;}
            return 200;
        } else if (terrainNoise < 0.025) {
            if (difValue > -1.4) {
                return 300;}
            return 300;
        } else if (terrainNoise < 0.045) {
            return 400;
        } else {
            return 500;
        }
    }

    private int temperature(int latitude, double difValue) {

        int adjustedLatitude = adjustLatitude(latitude);

        int maxHeight = MapSizes.XLARGE.getHeight();

        int maxHeightPart = maxHeight/10;

        int value;
        int latitudeLine = -1;

        if (adjustedLatitude < maxHeightPart/4) {
            latitudeLine = maxHeightPart/2;
            value = 10;
        } else if (adjustedLatitude > (maxHeightPart/4)*39) {
            latitudeLine = (maxHeightPart/4)*39;
            value = 10;
        } else if (adjustedLatitude < maxHeightPart*2 ) {
            latitudeLine = (maxHeightPart*2);
            value = 20;
        } else if (adjustedLatitude > maxHeightPart*8) {
            latitudeLine = (maxHeightPart*8);
            value = 20;
        } else if (adjustedLatitude < maxHeightPart*3.5) {
            latitudeLine = (int) (maxHeightPart*3.5);
            value = 30;
        } else if (adjustedLatitude > maxHeightPart*6.5) {
            latitudeLine = (int) (maxHeightPart*6.5);
            value = 30;
        } else if (adjustedLatitude < maxHeightPart*4.5) {
            latitudeLine = (int) (maxHeightPart*4.5);
            value = 40;
        } else if (adjustedLatitude > maxHeightPart*5.5) {
            latitudeLine = (int) (maxHeightPart*5.5);
            value = 40;
        } else {
            value = 50;
        }

        double difPos;

        if (latitudeLine == -1) {
            difPos = 100.0;
        } else if (adjustedLatitude < maxHeight/2) {
            difPos = (latitudeLine - adjustedLatitude);
        } else {
            difPos = (adjustedLatitude - latitudeLine);
        }

        difPos = (difPos) * 0.1;

        if (difValue + difPos < 0 )
            value += 10;

        return value;
    }

    private int adjustLatitude(int latitude) {

        int maxHeight = MapSizes.XLARGE.getHeight();

        int heightDif = maxHeight - height;

        if (heightDif == 0)
            return latitude;

        int adjustedLatitude = latitude + (seed % heightDif);

        return adjustedLatitude;
    }

    public int precipitation(double precipitationNoise, int terrain) {
        if (terrain == 500) {
            return 1;
        } else if (terrain == 400) {
            if (precipitationNoise > 0.12) {
                return 1;
            } else if (precipitationNoise > 0.09) {
                return 2;
            } else {
                return 3;
            }
        } else if (terrain == 300) {
            if (precipitationNoise > 0.03) {
                return 1;
            } else if (precipitationNoise > 0.007) {
                return 2;
            } else {
                return 3;
            }
        } else {
            return 3;
        }
    }

    public static int TEMP_PAINTER(int value) {
        switch (value) {
            case 111, 112, 113, 211, 212, 213, 311, 312, 313 -> {return 0xc2d7f2;}  // Glacier
            case 123,  133, 143, 153 -> {return 0x1433a6;}  // Deep Water
            case 223, 233, 243, 253 -> {return 0x3c5cfa;}  // Coastal Water
            case 322, 323 -> {return 0x10944b;} // Lowland Tundra
            case 422, 423 -> {return 0x53916f;} // Highlands Tundra
            case 321 -> {return 0x768c91;} // Cold Desert
            case 421 -> {return 0x3c474a;} // Cold Desert Hills
            case 331 -> {return 0x18d628;}  // Temperate Lowlands Plains
            case 332 -> {return 0x10941b;}  // Temperate Forest
            case 431 -> {return 0x6ed477;}  // Highland Hills
            case 432 -> {return 0x498f4f;}  // Temperate Highland Forest
            case 333 -> {return 0x084d0e;}  // Temperate Rainforest
            case 433 -> {return 0x2a452c;}  // Temperate Highland Rainforest
            case 341 -> {return 0xe84827;} // Hot Desert
            case 441 -> {return 0x9c2e17;} // Hot Desert Hills
            case 342 -> {return 0x459410;} // Hot Steppe
            case 442 -> {return 0x69914e;} // Hot Steppe hills
            case 351, 352, 343 -> {return 0x739410;} // Tropical Savanna
            case 451, 452, 443 -> {return 0x7e8f4c;} // Tropical Savanna Hills
            case 353 -> {return 0x3f5209;} // Tropical Rainforest
            case 453 -> {return 0x464f2b;} // Tropical Rainforest Hills
            case 411, 412, 413 -> {return 0x9dc6fc;} // Glacial Heights
            case 511, 521, 531 -> {return 0xebf0f7;} // Frozen Mountains
            case 541, 551 -> {return 0x260f02;}  // Mountains
        }
        System.out.println("Error not valid value: " + value);
        return 0x010101;
    }
}
