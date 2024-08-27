package com.csse3200.game.areas.Generation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.csse3200.game.areas.RandomNumberGenerator;


public class MapGenerator {
    private int mapSize;
    private RandomNumberGenerator rng;
    public String startingRoom;
    private HashMap<String, String[]> relativePosition;
    private HashMap<String, HashMap<String, Integer>> roomDetails;
    protected final int[][] detlas;

    public MapGenerator(int mapSize, String seed) {
        this.mapSize = mapSize;
        this.rng = new RandomNumberGenerator(seed);
        this.relativePosition = new HashMap<>();
        this.roomDetails = new HashMap<>();
        this.detlas = new int[][] {
            {1, 0},    // Up
            {0, -1},   // Right  
            {0, 1},    // Left 
            {-1, 0}    // Down
        };
        this.startingRoom = "0_0"; // Placeholder

        // Add the starting room to the relative position map

        addBlankRoom(this.startingRoom, 0, 0);
    
    }

    private void addBlankRoom(String key, int animal_index, int item_index) {
        HashMap<String, Integer> roomDetails = new HashMap<>();
        roomDetails.put("animal_index", animal_index);
        roomDetails.put("item_index", item_index);

        String[] connections = {"", "", "", ""};

        this.relativePosition.put(key, connections);
        this.roomDetails.put(key, roomDetails);
    }

    public void addRoomAtPosition(int x, int y, int animal_index, int item_index, int detlas_index) {
        String key = x + "_" + y;
        // generting new room

        // generating new room key
        String room_key = getStringRelativeLocation(x + detlas[detlas_index][0], y + detlas[detlas_index][1]);
        // get room connections 
        String[] connections = this.relativePosition.get(room_key);
        
        connections[detlas_index] = getStringRelativeLocation(x, y);
        
        connectRooms(key, room_key, detlas_index);
    }


    private String addKeyDetlas(String room_key, int[] detlas) {
        int[] positions = parseIntRelativeLocation(room_key);

        for (int i = 0; i < detlas.length; i++) {
            positions[i] = positions[i] + detlas[i];
        }

        return getStringRelativeLocation(positions[0], positions[1]);   
    }

    private void connectRooms(String host_room, String new_room, int detlas_index) {
        // detlas is made so that 3 - dedetlas_indextlas = connecting room 


        // get the detlas index for both rooms
        int[] detlas_coordinates = this.detlas[detlas_index];
        int[] inv_detlas_coordinates = this.detlas[3 - detlas_index];

        // get the current rooms
        String[] host_connection = this.relativePosition.get(host_room);
        String[] new_connection = this.relativePosition.get(new_room);

        String host_key = addKeyDetlas(host_room, detlas_coordinates);
        String new_Room_key = addKeyDetlas(new_room, inv_detlas_coordinates);

        host_connection[detlas_index] = host_key;
        new_connection[3 - detlas_index] = new_Room_key;

        this.relativePosition.put(host_room, host_connection);
        this.relativePosition.put(new_room, new_connection);
    }

    public HashMap<String, String[]> getPositions() {
        return this.relativePosition;
    }

    public HashMap<String, HashMap<String, Integer>> getRoomDetails() {
        return this.roomDetails;
    }
    
    // currently using a function that should work as rudementary difficulty 
    private int calculateCeiling(double x) {
        double a = 45.3;
        double c = 1.0;
        double d = -13.6;
        double result = a * Math.log10(x + c) + d;
        return (int) Math.ceil(result);
    }

    public int[] parseIntRelativeLocation(String string) {
        String[] parts = string.split("_");
        int y = Integer.parseInt(parts[0]);
        int x = Integer.parseInt(parts[1]);
        return new int[] { x, y };
    }

    public String getStringRelativeLocation(int x, int y) {
        return Integer.toString(x) + "_" + Integer.toString(y);
    }

    public void createMap() {
        int roomCount = (int) calculateCeiling(mapSize);
        while (0 < roomCount) {
            // 0_0 : [0_1, "", "", ""]
            List<String> rooms = new ArrayList<>(relativePosition.keySet());

            String randomRoomKey = rooms.get(rng.getRandomInt(0, rooms.size()));
            int detlas_index = rng.getRandomInt(0, 4);
            String new_Room_key = addKeyDetlas(randomRoomKey, detlas[detlas_index]);
            if (relativePosition.containsKey(new_Room_key) || !relativePosition.containsKey(randomRoomKey)) {
                continue;
            }
            int animal_index = rng.getRandomInt(0, 7);
            int item_index = rng.getRandomInt(0, 15);
            addBlankRoom(new_Room_key, animal_index, item_index);
            connectRooms(randomRoomKey, new_Room_key, detlas_index);
            roomCount--;
        }
    }

    public void printRelativePosition() {
        System.out.println("Relative Position:");
        for (Map.Entry<String, String[]> entry : relativePosition.entrySet()) {
            String key = entry.getKey();
            String[] connections = entry.getValue();
            System.out.println("Room: " + key);
            System.out.print("Connections: ");
            for (String connection : connections) {
                System.out.print(connection + " ");
            }
            System.out.println(); 
        }
    }


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
    }


