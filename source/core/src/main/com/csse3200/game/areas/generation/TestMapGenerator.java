package com.csse3200.game.areas.generation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates a static map, that ius useful for testing.
 */
public class TestMapGenerator implements MapGenerator {
    @Override
    public Map<String, String> getRoomKeys() {
        // TODO add a boss room to the test environment.
        return new HashMap<>();
    }

    @Override
    public Map<String, List<String>> getPositions() {
        return Map.of(
                "0_0", List.of("", "0_1", "", ""),
                "0_1", List.of("", "", "0_0", "")
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
