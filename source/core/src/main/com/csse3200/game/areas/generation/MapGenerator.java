package com.csse3200.game.areas.generation;

import java.util.*;

import com.csse3200.game.utils.RandomNumberGenerator;

/**
 * The MapGenerator class is responsible for generating and managing a game map.
 * It creates rooms, connects them, and provides methods to manipulate and export the map.
 */
public class MapGenerator {
    private Map<String, String> RoomKeys = new HashMap<String, String>();
    public static final int BASEROOM = 0;
    public static final int BOSSROOM = 1;
    public static final int NPCROOM = 2;
    public static final int GAMEROOM = 3;


    public int mapSize;
    public RandomNumberGenerator rng;
    public String player_position;
    public HashMap<String, List<String>> relativePosition;
    public HashMap<String, HashMap<String, Integer>> roomDetails;
    protected final int[][] detlas;

    /**
     * Constructs a new MapGenerator with the specified map size and seed.
     *
     * @param mapSize The size of the map to generate.
     * @param seed The seed for the random number generator.
     */
    public MapGenerator(int mapSize, String seed) {
        this.mapSize = mapSize;
        this.rng = new RandomNumberGenerator(seed);
        this.relativePosition = new HashMap<>();
        this.roomDetails = new HashMap<>();
        this.detlas = new int[][]{
                {1, 0},    // Up
                {0, 1},    // Right
                {0, -1},   // Left
                {-1, 0}    // Down
        };
        this.player_position = "0_0"; // Placeholder

        // Add the starting room to the relative position map
        addBlankRoom(this.player_position, 0, 0, BASEROOM);
    }

    /**
     * Adds a blank room to the map.
     *
     * @param key The key representing the room's position.
     * @param animal_index The index of the animal in the room.
     * @param item_index The index of the item in the room.
     */
    private void addBlankRoom(String key, int animal_index, int item_index, int type) {
        HashMap<String, Integer> roomDetails = new HashMap<>();
        roomDetails.put("animal_index", animal_index);
        roomDetails.put("item_index", item_index);
        roomDetails.put("room_type", type);

        List<String> connections = new ArrayList<>(List.of("", "", "", ""));

        this.relativePosition.put(key, connections);
        this.roomDetails.put(key, roomDetails);
    }

    public String getPlayerPosition() {
        return this.player_position;
    }

    /**
     * Adds a room at the specified position and connects it to an existing room.
     *
     * @param x The x-coordinate of the new room.
     * @param y The y-coordinate of the new room.
     * @param animal_index The index of the animal in the new room.
     * @param item_index The index of the item in the new room.
     * @param detlas_index The index of the direction to connect the new room.
     */
    public void addRoomAtPosition(int x, int y, int animal_index, int item_index, int detlas_index) {
        String key = x + "_" + y;
        // generating new room key
        String room_key = getStringRelativeLocation(x + detlas[detlas_index][0], y + detlas[detlas_index][1]);
        // get room connections 
        List<String> connections = this.relativePosition.get(room_key);

        connections.set(detlas_index, getStringRelativeLocation(x, y));
        connectRooms(key, room_key, detlas_index);
    }

    /**
     * Adds a delta to a room key to get a new room key.
     *
     * @param room_key The original room key.
     * @param detlas The delta to add to the room key.
     * @return The new room key after adding the delta.
     */
    private String addKeyDetlas(String room_key, int[] detlas) {
        int[] positions = parseIntRelativeLocation(room_key);

        for (int i = 0; i < detlas.length; i++) {
            positions[i] = positions[i] + detlas[i];
        }

        return getStringRelativeLocation(positions[0], positions[1]);
    }

    /**
     * Connects two rooms in the map.
     *
     * @param host_room The key of the host room.
     * @param new_room The key of the new room to connect.
     * @param detlas_index The index of the direction to connect the rooms.
     */
    private void connectRooms(String host_room, String new_room, int detlas_index) {
        // detlas is made so that 3 - detlas_index = connecting room

        // get the detlas index for both rooms
        int[] detlas_coordinates = this.detlas[detlas_index];
        int[] inv_detlas_coordinates = this.detlas[3 - detlas_index];

        // get the current rooms
        List<String> host_connection = this.relativePosition.get(host_room);
        List<String> new_connection = this.relativePosition.get(new_room);

        String host_key = addKeyDetlas(host_room, detlas_coordinates);
        String new_Room_key = addKeyDetlas(new_room, inv_detlas_coordinates);

        host_connection.set(detlas_index, host_key);
        new_connection.set(3 - detlas_index, new_Room_key);

        this.relativePosition.put(host_room, host_connection);
        this.relativePosition.put(new_room, new_connection);
    }

    /**
     * Gets the relative positions of all rooms in the map.
     *
     * @return A HashMap containing the relative positions of all rooms.
     */
    public HashMap<String, List<String>> getPositions() {
        return this.relativePosition;
    }

    /**
     * Gets the details of all rooms in the map.
     *
     * @return A HashMap containing the details of all rooms.
     */
    public HashMap<String, HashMap<String, Integer>> getRoomDetails() {
        return this.roomDetails;
    }

    /**
     * Calculates a ceiling value based on a logarithmic function.
     *
     * @param x The input value.
     * @return The calculated ceiling value.
     */
    private int calculateCeiling(double x) {
        double a = 45.3;
        double c = 1.0;
        double d = -13.6;
        double result = a * Math.log10(x + c) + d;
        return (int) Math.ceil(result);
    }

    /**
     * Parses a string representation of a relative location into integer coordinates.
     *
     * @param string The string representation of the location.
     * @return An array of two integers representing the x and y coordinates.
     */
    public int[] parseIntRelativeLocation(String string) {
        String[] parts = string.split("_");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        return new int[]{x, y};
    }

    /**
     * Converts integer coordinates to a string representation of a relative location.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return A string representation of the relative location.
     */
    public String getStringRelativeLocation(int x, int y) {
        return x + "_" + y;
    }

    /**
     * Creates the map by generating and connecting rooms.
     */
    public void createMap() {
        int roomCount = (int) calculateCeiling(mapSize);
        while (roomCount > 0) {
            List<String> rooms = new ArrayList<>(relativePosition.keySet());

            String randomRoomKey = rooms.get(rng.getRandomInt(0, rooms.size()));
            int detlas_index = rng.getRandomInt(0, 4);
            String new_Room_key = addKeyDetlas(randomRoomKey, detlas[detlas_index]);
            if (relativePosition.containsKey(new_Room_key) || !relativePosition.containsKey(randomRoomKey)) {
                continue;
            }
            int animal_index = rng.getRandomInt(0, 7);
            int item_index = rng.getRandomInt(0, 6);
            addBlankRoom(new_Room_key, animal_index, item_index, BASEROOM);
            connectRooms(randomRoomKey, new_Room_key, detlas_index);
            roomCount--;
        }
        List<String> setRooms = new ArrayList<>();
        setRooms.add("0_0");

        setBossRoom(setRooms);
        setNpcRoom(setRooms);
        setGameRoom(setRooms);
    }

    /**
     * Set boss room
     * @param setRooms - rooms already set with specific types
     */
    private void setBossRoom(List<String> setRooms) {
        String bossRoom = findFurthestRoom();
        HashMap<String, Integer> details = roomDetails.get(bossRoom);
        details.put("room_type", BOSSROOM);
        RoomKeys.put("Boss", bossRoom);
        setRooms.add(bossRoom);
    }

    /**
     * Set NPC room
     * @param setRooms - rooms already set with specific types
     */
    private void setNpcRoom(List<String> setRooms) {
        List<String> rooms = new ArrayList<>(roomDetails.keySet());
        String randomRoomKey;
        int connections;
        do {
            randomRoomKey = rooms.get(rng.getRandomInt(0, rooms.size()));
            connections = Collections.frequency(relativePosition.get(randomRoomKey), "");
        } while (setRooms.contains(randomRoomKey) ||  connections != 3);
        // Get the random room details
        HashMap<String, Integer> details = roomDetails.get(randomRoomKey);
        details.put("room_type", NPCROOM);
        RoomKeys.put("NPC", randomRoomKey);
        setRooms.add(randomRoomKey);
    }

    /**
     * Set Game Room
     * @param setRooms - rooms already set with specific types
     */
    private void setGameRoom(List<String> setRooms) {
        List<String> rooms = new ArrayList<>(roomDetails.keySet());
        String randomRoomKey;
        int connections;
        do {
            randomRoomKey = rooms.get(rng.getRandomInt(0, rooms.size()));
            connections = Collections.frequency(relativePosition.get(randomRoomKey), "");
        } while (setRooms.contains(randomRoomKey) ||  connections != 3);

        // Get the random room details
        HashMap<String, Integer> details = roomDetails.get(randomRoomKey);
        details.put("room_type", GAMEROOM);
        RoomKeys.put("GameRoom", randomRoomKey);
        setRooms.add(randomRoomKey);
    }

    /**
     * Find the furthest room from the centre (to be used to spawn boss room)
     * @return - String representation of the room furthest from centre
     */
    public String findFurthestRoom() {
        String location = "0_0";
        int maxDist = 0;
        for (Map.Entry<String, List<String>> entry : relativePosition.entrySet()) {
            if (Collections.frequency(entry.getValue(), "") != 3) {
                // checking whether there is only 1 connection to room
                continue;
            }
            String key = entry.getKey();
            int dist = calculateDistance(key);
            if (maxDist < dist) {
                maxDist = dist;
                location = key;
            }
        }
        return location;
    }

    /**
     * Calculates distance from centre room to current room key
     *
     * @param key - room key
     * @return - the distance in room traversals from first centre room
     */
    public int calculateDistance(String key) {
        String[] parts = key.split("_");
        int x = Math.abs(Integer.parseInt(parts[0]));
        int y = Math.abs(Integer.parseInt(parts[1]));
        return x + y;
    }

    /**
     * Prints the relative positions of all rooms in the map.
     *
     * @return A string representation of the relative positions.
     */
    public String printRelativePosition() {
        System.out.println("Relative Position:");
        StringBuilder logString = new StringBuilder("Relative Position:");
        for (Map.Entry<String, List<String>> entry : relativePosition.entrySet()) {
            String key = entry.getKey();
            List<String> connections = entry.getValue();
            logString.append("Room: ").append(key).append("\n");
            System.out.println("Room: " + key);
            System.out.print("Connections: ");
            logString.append("Connections: ");
            for (String connection : connections) {
                logString.append(connection).append(" ");
                System.out.print(connection + " ");
            }
            logString.append("\n");
            System.out.println();
        }
        return logString.toString();
    }

    /**
     * Prints the details of all rooms in the map.
     */
    public void printRoomDetails() {
        System.out.println("Room Details:");
        for (Map.Entry<String, HashMap<String, Integer>> entry : roomDetails.entrySet()) {
            String key = entry.getKey();
            HashMap<String, Integer> details = entry.getValue();
            System.out.println("Room: " + key);
            for (Map.Entry<String, Integer> detail : details.entrySet()) {
                System.out.println("  " + detail.getKey() + ": " + detail.getValue());
            }
        }
    }
    /**
     * Gets the size of the map.
     *
     * @return The size of the map.
     */
    public int getMapSize() {
        return mapSize;
    }

    /**
     * Gets the seed used for random number generation.
     *
     * @return The seed as a string.
     */
    public String getMapSeed() {
        return rng.getSeed();
    }

    /**
     * Sets the seed when map is loaded
     * @param seed: the seed to be set.
     */
    public void setMapSeed(String seed) {rng.setSeed(seed);}

    public String get_player_position() {
        return this.player_position;
    }

    /**
     * Returns the Map of roomKeys 
     * @return the map of roomKeys
     */

    public Map<String, String> getRoomKeys() {
        return this.RoomKeys;
    }

    /**
     * Returns the key for the roomType
     * @return roomkey 
     */
    public String getRoomKey(String key) {
        return this.RoomKeys.get(key);
    }
}
