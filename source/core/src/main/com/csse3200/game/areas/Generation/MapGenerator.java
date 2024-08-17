package com.csse3200.game.areas.generation;
import java.util.HashMap;
import java.util.Map;
import com.csse3200.game.areas.generation.Room;

import com.csse3200.game.areas.RandomNumberGenerator;




public class MapGenerator {

    private int mapSize;
    private RandomNumberGenerator rng;
    private Room startingRoom;
    private HashMap<String, Room> relativePosition; // Key-value dictionary for coordinates

    /**
     * Constructor for the Map class.
     *
     * @param mapSize The size of the map.
     * @param rng The RandomNumberGenerator instance used to create rooms.
     */
    public MapGenerator(int mapSize, String seed) {
        this.mapSize = mapSize;
        this.rng = new RandomNumberGenerator(seed);
        this.relativePosition = new HashMap<>(); // Initialize the dictionary

        // Initialize the starting room with the given parameters
        Room[] connections = new Room[4]; // Empty array for room connections
        int branchCount = 0; // No branches initially
        int[] relativeLocation = {0, 0}; // Starting location

        startingRoom = new Room(rng, this, mapSize, connections, branchCount, relativeLocation);

        // Add the starting room to the relative position map
        addRoomAtPosition(0, 0, startingRoom);
    }

    /**
     * Adds a room at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param room The room to add.
     */
    public void addRoomAtPosition(int x, int y, Room room) {
        String key = x + "_" + y;
        relativePosition.put(key, room);
    }

    /**
     * Gets the room at the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The room at the specified coordinates, or null if no room is found.
     */
    public Room getRoomAtPosition(int x, int y) {
        String key = x + "_" + y;
        return relativePosition.get(key);
    }

}
