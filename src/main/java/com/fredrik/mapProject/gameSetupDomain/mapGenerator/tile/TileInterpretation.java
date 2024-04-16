package com.fredrik.mapProject.gameSetupDomain.mapGenerator.tile;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gameSetupDomain.mapGenerator.PerlinNoise.OpenSimplex;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileEntity;
import com.fredrik.mapProject.gameSetupDomain.model.MapTileId;


public class TileInterpretation {

    private final int seed;
    private final int width;
    private final int widthPart;
    private final int height;
    private final int heightPart;
    private final int waterPath;

    public TileInterpretation(int seed, int width, int height) {

        this.seed = seed;
        this.height = height;
        this.heightPart = height/10;
        this.width = width;
        this.widthPart = width/10;
        this.waterPath = spinSeed(seed);
    }

    public MapTileEntity tileValue(MapTileId mapTileId) {

        MapTileEntity tile = new MapTileEntity();
        tile.setMapTileId(mapTileId);
        tile.setTileOwner(Player.NONE);
        tile.setVisibility("00000000000000000000000000000000");
        tile.setBuilding("{\"type\": \"NONE\", \"progress\": 0}");

        int y = mapTileId.getCoordinates().getY();
        int x = mapTileId.getCoordinates().getX();

        double frequency1 = 1 / 36.0;
        double frequency2 = 1 / 120.0;
        double frequency3 = 1 / 340.0;
        double frequency4 = 1 / 24.0;
        double frequency5 = 1 / 6.0;

        double terrainNoiseValue = threeFrequencyNoise(y, x, frequency1, frequency2, frequency3);
        terrainNoiseValue = waterPath( x, terrainNoiseValue);

        double difNoiseValue = twoFrequencyNoise(y, x, frequency4, frequency5);
        double precipitationNoiseValue = singleFrequencyNoise(y, x, frequency2) + (difNoiseValue * 0.05);

        int terrainValue = terrain(terrainNoiseValue, difNoiseValue , y, x);

        int temperatureValue = temperature(x, (terrainNoiseValue + difNoiseValue - 0.8)/3);
        int precipitationValue = precipitation(precipitationNoiseValue, terrainValue, difNoiseValue, terrainNoiseValue);

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

        if (y < 100) {
            Double gradiant100 = y * 0.01;
            double gradiant0 = 1 - gradiant100;
            valuesCombined = ((valuesCombined * gradiant100) + (0.01 * gradiant0)/2);
        } else if (y > height - 100) {
            int gradiant = height - y;
            Double gradiant100 = gradiant * 0.01;
            double gradiant0 = 1 - gradiant100;
            valuesCombined = ((valuesCombined * gradiant100) + (0.01 * gradiant0)/2);
        }

        return getTerrainResult(valuesCombined, difValue, x);
    }

    private int getTerrainResult(Double valuesCombined, double difValue, int x) {
        int terrainResult;

        if (x <= widthPart) {
            float test = ((1.f/widthPart)* x);
            double test2 = 1 - test;
            Double value = ((valuesCombined * test) + (test2 * 1/2));
            terrainResult =  terrainMapping(value, difValue);
        } else if (x >= width - widthPart) {
            float test = ((1.f/widthPart) * (width - x));
            double test2 = 1 - test;
            Double value = ((valuesCombined * test) + (test2 * 1/2)) ;
            terrainResult = terrainMapping(value, difValue);
        } else {
            terrainResult = terrainMapping(valuesCombined, difValue);
        }
        return terrainResult;
    }

    private double waterPath(int y, double value) {

        if (waterPath <= y + 100 && waterPath >= y - 100) {
            int gradiant = y - waterPath;
            if (gradiant < 0) {
                gradiant = gradiant * -1;
            }
            double gradiant100 = gradiant * 0.01;
            double gradiant0 = 1 - gradiant100;
            if (value * gradiant100 < 0.5 * gradiant0) {
                value = ((value * gradiant100) + (0.3 * gradiant0)/2);
            }
        }
        return value;
    }

    private int terrainMapping(Double terrain, double difValue) {

        terrain = terrain * -1 + (difValue*0.05);

        if (terrain < -0.1) {
            return 100;
        } else if (terrain < 0) {
            if (difValue > 1.1) {
                return 300;}
            return 200;
        } else if (terrain < 0.15) {
            if (difValue > -1.4) {
                return 300;}
            return 300;
        } else if (terrain < 0.4) {
            return 400;
        } else {
            return 500;
        }
    }

    private int temperature(int latitude, double difValue) {

        int value;
        int latitudeLine = -1;

        if (latitude < heightPart/2) {
            latitudeLine = heightPart/2;
            value = 10;
        } else if (latitude > (heightPart/2)*19) {
            latitudeLine = (heightPart/2)*19;
            value = 10;
        } else if (latitude < heightPart*2 ) {
            latitudeLine = (heightPart*2);
            value = 20;
        } else if (latitude > heightPart*8) {
            latitudeLine = (heightPart*8);
            value = 20;
        } else if (latitude < heightPart*3.5) {
            latitudeLine = (int) (heightPart*3.5);
            value = 30;
        } else if (latitude > heightPart*6.5) {
            latitudeLine = (int) (heightPart*6.5);
            value = 30; //
        } else if (latitude < heightPart*4.5) {
            latitudeLine = (int) (heightPart*4.5);
            value = 40;
        } else if (latitude > heightPart*5.5) {
            latitudeLine = (int) (heightPart*5.5);
            value = 40;
        } else {
            value = 50;
        }

        double difPos;

        if (latitudeLine == -1) {
            difPos = 100.0;
        } else if (latitude < height/2) {
            difPos = (latitudeLine - latitude);
        } else {
            difPos = (latitude - latitudeLine);
        }

        difPos = (difPos) * 0.1;

        if (difValue + difPos < 0 )
            value += 10;

        return value;
    }

    public int precipitation(double perNoise, int terrain, double difValue, double terrainNoiseValue) {
        if (terrain == 500) {
            return 1;
        } else if (terrain < 201) {
            return 3;
        }

        terrainNoiseValue = (terrainNoiseValue * -1) + (difValue * 0.03);
        perNoise = perNoise + (difValue * 0.2);

        if (terrainNoiseValue > 0.25) {
            if (perNoise > -0.2) {
                return 1;
            } else if (perNoise > -0.6) {
                return 2;
            } else {
                return 3;
            }
        } else {
            if (perNoise > 0.7) {
                return 1;
            } else if (perNoise > 0) {
                return 2;
            } else {
                return 3;
            }
        }
    }

    private int spinSeed(int latitude) {

        while (latitude > height - heightPart) {
            latitude -= (height - heightPart*2);
        }

        if (latitude < heightPart) {
            latitude += (height - heightPart*2);
        }

        return latitude;
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
