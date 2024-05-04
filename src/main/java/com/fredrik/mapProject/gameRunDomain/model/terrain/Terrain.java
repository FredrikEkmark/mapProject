package com.fredrik.mapProject.gameRunDomain.model.terrain;

public enum Terrain {

    DEEP_WATER(Precipitation.IRRELEVANT, Temperature.IRRELEVANT, Elevation.DEEP),
    COASTAL_WATER(Precipitation.IRRELEVANT, Temperature.IRRELEVANT, Elevation.SHALLOW),
    LOWLAND_TUNDRA(Precipitation.MODERATE, Temperature.SUBARCTIC, Elevation.LOWLANDS),
    HIGHLANDS_TUNDRA(Precipitation.MODERATE, Temperature.SUBARCTIC, Elevation.HIGHLANDS),
    COLD_DESERT(Precipitation.NONE, Temperature.SUBARCTIC, Elevation.LOWLANDS),
    COLD_DESERT_HILLS(Precipitation.NONE, Temperature.SUBARCTIC, Elevation.HIGHLANDS),
    TEMPERATE_LOWLANDS_PLAINS(Precipitation.LOW, Temperature.TEMPERATE, Elevation.LOWLANDS),
    TEMPERATE_FOREST(Precipitation.MODERATE, Temperature.TEMPERATE, Elevation.LOWLANDS),
    HIGHLAND_HILLS(Precipitation.LOW, Temperature.TEMPERATE, Elevation.HIGHLANDS),
    TEMPERATE_HIGHLAND_FOREST(Precipitation.MODERATE, Temperature.TEMPERATE, Elevation.HIGHLANDS),
    TEMPERATE_RAINFOREST(Precipitation.HIGH, Temperature.TEMPERATE, Elevation.LOWLANDS),
    TEMPERATE_HIGHLAND_RAINFOREST(Precipitation.HIGH, Temperature.TEMPERATE, Elevation.HIGHLANDS),
    HOT_DESERT(Precipitation.NONE, Temperature.SUBTROPICAL, Elevation.LOWLANDS),
    HOT_DESERT_HILLS(Precipitation.NONE, Temperature.SUBTROPICAL, Elevation.HIGHLANDS),
    HOT_STEPPE(Precipitation.MODERATE, Temperature.SUBTROPICAL, Elevation.LOWLANDS),
    HOT_STEPPE_HILLS(Precipitation.MODERATE, Temperature.SUBTROPICAL, Elevation.HIGHLANDS),
    TROPICAL_SAVANNA(Precipitation.MODERATE, Temperature.TROPICAL, Elevation.LOWLANDS),
    TROPICAL_SAVANNA_HILLS(Precipitation.MODERATE, Temperature.TROPICAL, Elevation.HIGHLANDS),
    TROPICAL_RAINFOREST(Precipitation.HIGH, Temperature.TROPICAL, Elevation.LOWLANDS),
    TROPICAL_RAINFOREST_HILLS(Precipitation.HIGH, Temperature.TROPICAL, Elevation.HIGHLANDS),
    GLACIER(Precipitation.IRRELEVANT, Temperature.ARCTIC, Elevation.LOWLANDS),
    GLACIAL_HEIGHTS(Precipitation.IRRELEVANT, Temperature.ARCTIC, Elevation.HIGHLANDS),
    MOUNTAINS(Precipitation.IRRELEVANT, Temperature.IRRELEVANT, Elevation.MOUNTAIN);

    private Precipitation precipitation;

    private Temperature temperature;

    private Elevation elevation;

    Terrain(Precipitation precipitation, Temperature temperature, Elevation elevation) {
        this.precipitation = precipitation;
        this.temperature = temperature;
        this.elevation = elevation;
    }

    public Precipitation getPrecipitation() {
        return precipitation;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public Elevation getElevation() {
        return elevation;
    }
}

