package com.fredrik.mapProject.model.event.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fredrik.mapProject.model.map.coordinates.MapCoordinates;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class UnitEventUtility {

    static UUID parseArmyId(JsonNode armyIdNode) {

        if (armyIdNode.isTextual()) {
            String uuidString = armyIdNode.asText();
            return UUID.fromString(uuidString);
        }
        return null;
    }

    static MapCoordinates parseDestinationCoordinate(JsonNode xNode, JsonNode yNode) {

        if (yNode.isInt() && xNode.isInt()) {
            return new MapCoordinates(xNode.asInt(), yNode.asInt());
        }
        return null;
    }

    static List<UUID> parseRegimentIDs(JsonNode regimentsNode) {
        List<UUID> regimentIDs = new ArrayList<>();
        if (regimentsNode.isArray()) {
            ArrayNode regimentsArray = (ArrayNode) regimentsNode;
            regimentIDs = new ArrayList<>(regimentsArray.size());
            for (JsonNode regiment : regimentsArray) {
                String uuidString = regiment.asText();
                UUID uuid = UUID.fromString(uuidString);
                regimentIDs.add(uuid);
            }
        }
        return regimentIDs;
    }
}
