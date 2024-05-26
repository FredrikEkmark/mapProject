package com.fredrik.mapProject.config;

import com.fredrik.mapProject.model.building.BuildingType;
import com.fredrik.mapProject.model.mana.EventManaCost;
import com.fredrik.mapProject.model.mana.StorableManaTypes;
import com.fredrik.mapProject.model.map.MapSizes;
import com.fredrik.mapProject.model.map.terrain.Elevation;
import com.fredrik.mapProject.model.map.terrain.Precipitation;
import com.fredrik.mapProject.model.map.terrain.Temperature;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public class GameConfig {
    private static final String CONFIG_FILE_PATH = "src/main/resources/game-config.properties";

    private final static Properties properties;

    static {
        properties = new Properties();
        try (InputStream input = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(input);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static String getGameConfigStringify() {
        StringBuilder stringBuilder = new StringBuilder("{");

        stringBuilder.append("\"buildings\": {");

        for (BuildingType buildingType: BuildingType.values()) {
            stringBuilder.append("\"" + buildingType.toString().toLowerCase() + "\":{");
            stringBuilder.append("\"manpowerUpkeep\":" + getBuildingManpowerUpkeep(buildingType.toString()) + ",");
            stringBuilder.append("\"completeAtProgress\":" + getBuildingCompleteAtProgress(buildingType.toString()) +",");
            stringBuilder.append("\"populationMaxBonus\":" + getBuildingPopulationMaxBonus(buildingType.toString()) +",");
            stringBuilder.append("\"buildableElevation\":" +
                    convertElevationArrayToString(getBuildingBuildableElevations(buildingType.toString())) +",");
            stringBuilder.append("\"input\":{");
            for (StorableManaTypes storableManaTypes : StorableManaTypes.values()) {
                stringBuilder.append("\"" + storableManaTypes.toString().toLowerCase() + "\":" +
                        getBuildingBaseInput(buildingType, storableManaTypes) + ",");
            }
            stringBuilder.append("},\"output\":{");
            for (StorableManaTypes storableManaTypes : StorableManaTypes.values()) {
                stringBuilder.append("\"" + storableManaTypes.toString().toLowerCase() + "\":" +
                        getBuildingBaseOutput(buildingType, storableManaTypes) + ",");
            }
            stringBuilder.append("},\"buildCost\":{");
            for (StorableManaTypes storableManaTypes : StorableManaTypes.values()) {
                stringBuilder.append("\"" + storableManaTypes.toString().toLowerCase() + "\":" +
                        getBuildingBuildCost(buildingType, storableManaTypes) + ",");
            }
            stringBuilder.append("},");
        }
        stringBuilder.append("},\"mapSize\":{");
        for (MapSizes mapSize: MapSizes.values()) {
            stringBuilder.append("\"width\":" + getMapSizeWidth(mapSize.toString()) + ",");
            stringBuilder.append("\"height\":" + getMapSizeHeight(mapSize.toString()) + ",");
            stringBuilder.append("\"maxPlayers\":" + getMapSizeMaxPlayers(mapSize.toString()) + ",");
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    private static String convertElevationArrayToString(Elevation[] elevations) {
        StringBuilder stringBuilder = new StringBuilder("[");

        for (int i = 0; i < elevations.length; i++) {
            stringBuilder.append(elevations[i].toString());
            if (i < elevations.length - 1) {
                stringBuilder.append(",");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    // MAP SIZE FUNCTIONS

    public static int getMapSizeWidth(String mapSize) {
        return Integer.parseInt(properties.getProperty(
                "MAP_SIZE_" + mapSize + "_WIDTH"
        ));
    }

    public static int getMapSizeHeight(String mapSize) {
        return Integer.parseInt(properties.getProperty(
                "MAP_SIZE_" + mapSize + "_HEIGHT"
        ));
    }

    public static int getMapSizeMaxPlayers(String mapSize) {
        return Integer.parseInt(properties.getProperty(
                "MAP_SIZE_" + mapSize + "_MAX_PLAYERS"
        ));
    }

    // BUILDING FUNCTIONS

    public static int getBaseDisuseDamage() {
        return Integer.parseInt(properties.getProperty(
                "BUILDING_BASE_DISUSE_DAMAGE", "0"
        ));
    }

    public  static EventManaCost getBuildingNonManpowerCost(BuildingType buildingType) {
        EventManaCost eventManaCost = new EventManaCost();

        eventManaCost.setFood(Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_BASE_BUILD_COST_FOOD", "0")));
        eventManaCost.setStone(Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_BASE_BUILD_COST_STONE", "0")));
        eventManaCost.setWood(Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_BASE_BUILD_COST_WOOD", "0")));
        eventManaCost.setLeather(Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_BASE_BUILD_COST_LEATHER", "0")));
        eventManaCost.setIron(Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_BASE_BUILD_COST_IRON", "0")));
        eventManaCost.setFurniture(Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_BASE_BUILD_COST_FURNITURE", "0")));
        eventManaCost.setSimpleClothes(Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_BASE_BUILD_COST_SIMPLE_CLOTHES", "0")));
        eventManaCost.setHorses(Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_BASE_BUILD_COST_HORSES", "0")));

        return eventManaCost;
    }

    public static int getBuildingBuildCost(BuildingType buildingType, StorableManaTypes storableManaTypes) {
        return Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_BASE_BUILD_COST_" + storableManaTypes.toString(), "0"
        ));
    }

    public static int getBuildingBaseInput(BuildingType buildingType, StorableManaTypes storableManaTypes) {
        return Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_BASE_INPUT_" + storableManaTypes.toString(), "0"
        ));
    }

    public static int getBuildingBaseOutput(BuildingType buildingType, StorableManaTypes storableManaTypes) {
        return Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_BASE_OUTPUT_" + storableManaTypes.toString(), "0"
        ));
    }

    public static int getBuildingManpowerUpkeep(String buildingType) {
        return Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType + "_MANPOWER_UPKEEP", "0"
        ));
    }

    public static int getBuildingCompleteAtProgress(String buildingType) {
        return Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType + "_COMPLETE_AT_PROGRESS", "0"
        ));
    }

    public static int getBuildingPopulationMaxBonus(String buildingType) {
        return Integer.parseInt(properties.getProperty(
                "BUILDING_" + buildingType + "_POPULATION_MAX_BONUS", "0"
        ));
    }

    public static Elevation[] getBuildingBuildableElevations(String buildingType) {
        String propertyValue = properties.getProperty("BUILDING_" + buildingType + "_BUILDABLE_ELEVATION");

        if (propertyValue != null) {
            Elevation[] elevations = Arrays.stream(propertyValue.split(","))
                    .map(String::trim)
                    .map(Elevation::valueOf)
                    .toArray(Elevation[]::new);
            return elevations;
        }
        return new Elevation[0];
    }

    public static double getBuildingElevationModifier(BuildingType buildingType, Elevation elevation) {

        return Double.parseDouble(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_ELEVATION_MODIFIER_ " +  elevation, "0"
        ));
    }

    public static double getBuildingTemperatureModifier(BuildingType buildingType, Temperature temperature) {

        return Double.parseDouble(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_TEMPERATURE_MODIFIER_" + temperature, "0"
        ));
    }

    public static double getBuildingPrecipitationModifier(BuildingType buildingType, Precipitation precipitation) {

        return Double.parseDouble(properties.getProperty(
                "BUILDING_" + buildingType.toString() + "_PRECIPITATION_MODIFIER_" +  precipitation, "0"
        ));
    }

    // UNIT FUNCTIONS

    public static int getBaseMoral() {
        return Integer.parseInt(properties.getProperty(
                "UNIT_BASE_MORAL", "0"
        ));
    }

    public static int getMoralMaximum() {
        return Integer.parseInt(properties.getProperty(
                "UNIT_MORAL_MAXIMUM", "0"
        ));
    }

    public static int getMoralMinimum() {
        return Integer.parseInt(properties.getProperty(
                "UNIT_MORAL_MINIMUM", "0"
        ));
    }

    public static int getBaseMoralRecovery() {
        return Integer.parseInt(properties.getProperty(
                "UNIT_BASE_MORAL_RECOVERY", "0"
        ));
    }

    public static int getBaseRecruitment() {
        return Integer.parseInt(properties.getProperty(
                "UNIT_BASE_RECRUITMENT_RATE", "0"
        ));
    }

    public static int getUnitBaseMovement(String unitType) {
        return Integer.parseInt(properties.getProperty(
                "UNIT_" + unitType + "_BASE_MOVEMENT", "0"
        ));
    }

    public static int getUnitRegimentAmount(String unitType) {
        return Integer.parseInt(properties.getProperty(
                "UNIT_" + unitType + "_REGIMENT_AMOUNT", "0"
        ));
    }

    // EVENT FUNCTIONS

    public static int conquerEventBuildingDamage() {
        return Integer.parseInt(properties.getProperty(
                "CONQUER_EVENT_BUILDING_DAMAGE", "0"
        ));
    }

    public static int raidEventBuildingDamage() {
        return Integer.parseInt(properties.getProperty(
                "RAID_EVENT_BUILDING_DAMAGE", "0"
        ));
    }
}
