package com.csse3200.game.areas.generation;

import java.util.*;

import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.RandomNumberGenerator;

import static com.csse3200.game.areas.generation.RoomType.*;

/**
 * The MapGenerator class is responsible for generating and managing a game map.
 * It creates rooms, connects them, and provides methods to manipulate and export the map.
 */
public class RandomMapGenerator implements MapGenerator {
    private final Map<String, String> roomKeys = new HashMap<>();

    private final int mapSize;
    private final RandomNumberGenerator rng;
    private final String playerPosition;
    private final HashMap<String, List<String>> relativePosition;
    private final HashMap<String, HashMap<String, Integer>> roomDetails;
    private final int[][] detlas;

    /**
     * Constructs a new MapGenerator with the specified map size and seed.
     *
     * @param mapSize The size of the map to generate.
     */
    public RandomMapGenerator(int mapSize) {
        this.mapSize = mapSize;
        this.rng = ServiceLocator.getRandomService().getRandomNumberGenerator(RandomMapGenerator.class);
        this.relativePosition = new HashMap<>();
        this.roomDetails = new HashMap<>();
        this.detlas = new int[][]{
                {1, 0},    // Up
                {0, 1},    // Right
                {0, -1},   // Left
                {-1, 0}    // Down
        };
        this.playerPosition = "0_0"; // Placeholder

        // Add the starting room to the relative position map
        addBlankRoom(this.playerPosition, 0, 0, BASE_ROOM.num);
    }

    @Override
    public Map<String, String> getRoomKeys() {
        return new HashMap<>(roomKeys);
    }

    @Override
    public Map<String, List<String>> getPositions() {
        return new HashMap<>(this.relativePosition);
    }

    @Override
    public Map<String, Map<String, Integer>> getRoomDetails() {
        return new HashMap<>(this.roomDetails);
    }

    @Override
    public void createMap() {
        int roomCount = calculateCeiling(mapSize);
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
            addBlankRoom(new_Room_key, animal_index, item_index, BASE_ROOM.num);
            connectRooms(randomRoomKey, new_Room_key, detlas_index);
            roomCount--;
        }
        List<String> setRooms = new ArrayList<>();
        setRooms.add("0_0");

        setBossRoom(setRooms);
        setShopRoom(setRooms);
    }

    @Override
    public int getMapSize() {
        return mapSize;
    }

    @Override
    public String getMapSeed() {
        return rng.getSeed();
    }

    @Override
    public String getPlayerPosition() {
        return this.playerPosition;
    }


    /**
     * Adds a blank room to the map.
     *
     * @param key          The key representing the room's position.
     * @param animal_index The index of the animal in the room.
     * @param item_index   The index of the item in the room.
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

    /**
     * Adds a delta to a room key to get a new room key.
     *
     * @param room_key The original room key.
     * @param detlas   The delta to add to the room key.
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
     * @param host_room    The key of the host room.
     * @param new_room     The key of the new room to connect.
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
    private int[] parseIntRelativeLocation(String string) {
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
    private String getStringRelativeLocation(int x, int y) {
        return x + "_" + y;
    }

    /**
     * Set boss room
     *
     * @param setRooms - rooms already set with specific types
     */
    private void setBossRoom(List<String> setRooms) {
        String bossRoom = findFurthestRoom();
        HashMap<String, Integer> details = roomDetails.get(bossRoom);
        details.put("room_type", BOSS_ROOM.num);
        roomKeys.put("Boss", bossRoom);
        setRooms.add(bossRoom);
    }

    /**
     * Set Shop room
     *
     * @param setRooms - rooms already set with specific types
     */
    private void setShopRoom(List<String> setRooms) {
        List<String> rooms = new ArrayList<>(roomDetails.keySet());
        String randomRoomKey;
        do {
            randomRoomKey = rooms.get(rng.getRandomInt(0, rooms.size()));
        } while (setRooms.contains(randomRoomKey));

        // Get the random room details
        HashMap<String, Integer> details = roomDetails.get(randomRoomKey);
        details.put("room_type", SHOP_ROOM.num);
        roomKeys.put("Shop", randomRoomKey);
        setRooms.add(randomRoomKey);
    }

    /**
     * Find the furthest room from the centre (to be used to spawn boss room)
     *
     * @return - String representation of the room furthest from centre
     */
    private String findFurthestRoom() {
        String location = "0_0";
        int maxDist = 0;
        for (Map.Entry<String, List<String>> entry : relativePosition.entrySet()) {
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
    private int calculateDistance(String key) {
        String[] parts = key.split("_");
        int x = Math.abs(Integer.parseInt(parts[0]));
        int y = Math.abs(Integer.parseInt(parts[1]));
        return x + y;
    }
}



