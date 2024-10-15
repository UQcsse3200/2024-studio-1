package com.csse3200.game.areas.generation;

import java.util.List;
import java.util.Map;

/**
 * Generate the details for a Map.
 */
public interface MapGenerator {

    /**
     * Get a copy of all the room keys
     *
     * @return a copy of the room key map.
     */
    Map<String, String> getRoomKeys();

    /**
     * Gets the relative positions of all rooms in the map.
     *
     * @return A HashMap containing the relative positions of all rooms.
     */
    Map<String, List<String>> getPositions();

    /**
     * Gets the details of all rooms in the map.
     *
     * @return A HashMap containing the details of all rooms.
     */
    Map<String, Map<String, Integer>> getRoomDetails();

    /**
     * Creates the map by generating and connecting rooms.
     */
    void createMap();

    /**
     * Gets the size of the map.
     *
     * @return The size of the map.
     */
    int getMapSize();

    /**
     * Gets the seed used for random number generation.
     *
     * @return The seed as a string.
     */
    String getMapSeed();

    /**
     * Get the player spawn position.
     *
     * @return the room key of the room to spawn in.
     */
    String getPlayerPosition();
}
