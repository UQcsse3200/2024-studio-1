package com.csse3200.game.areas.generation;

import java.util.List;
import java.util.Map;

/**
 * Generates a static map, that ius useful for testing.
 */
public class TestMapGenerator implements MapGenerator {
    @Override
    public Map<String, String> getRoomKeys() {
        return Map.of("Boss", "1_0");
    }

    @Override
    public Map<String, List<String>> getPositions() {
        return Map.of(
                "0_0", List.of("", "0_1", "", ""),
                "0_1", List.of("", "", "0_0", ""),
                "1_0", List.of("0_0", "", "", "")
        );
    }

    @Override
    public Map<String, Map<String, Integer>> getRoomDetails() {
        return Map.of("0_0", Map.of(
                        "animal_index", 0,
                        "item_index", 0,
                        "room_type", 0),
                "0_1", Map.of(
                        "animal_index", 0,
                        "item_index", 0,
                        "room_type", 0
                ),
                "1_0", Map.of(
                        "animal_index", 0,
                        "item_index", 0,
                        "room_type", 1
                )
        );
    }

    @Override
    public void createMap() {

    }

    @Override
    public int getMapSize() {
        return 1;
    }

    @Override
    public String getMapSeed() {
        return "";
    }

    @Override
    public String getPlayerPosition() {
        return "0_0";
    }
}
