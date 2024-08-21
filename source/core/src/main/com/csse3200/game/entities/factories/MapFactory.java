package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.configs.MapConfigs;
import com.csse3200.game.files.FileLoader;

import java.util.ArrayList;
import java.util.List;


public class MapFactory {
    private static MapConfigs mapData;


    public static MapConfigs loadMap(String filePath) {
        mapData = FileLoader.readClass(MapConfigs.class, filePath);
        if (mapData == null){
            throw new IllegalStateException("Failed to load mapData from " + filePath);
        }
        return mapData;
    }

    public static List<int[]> getRoomConnections(String room) {
        List<String> connections = mapData.room_connections.get(room);
        if (connections == null) {
            throw new IllegalArgumentException("Room"+ room +"doesn't exist or has no connections");
        }
        List<int[]> roomConnections = new ArrayList<>();
        for (String connection : connections) {
            //Splitting the room connection coordinates into a list of [x,y]
            //coordinates
            String[] parts = connection.split("_");
            int[] coordinates = new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
            roomConnections.add(coordinates);
        }
        return roomConnections;
    }
    public static int[] getRoomConnection(String room, int index) {
        List<int[]> roomConnections = getRoomConnections(room);
        if (index < 0 || index > roomConnections.size()) {
            throw new IndexOutOfBoundsException("The index" + index + "is out of bounds");
        }
        return roomConnections.get(index);
    }

    public static Integer getAnimalIndex(String room) {
        Integer animalIndices = mapData.room_info.get(room).animal_index;
        if (animalIndices == null) {
            throw new IllegalArgumentException("Room"+ room +"doesn't exist or has no animals");
        }
        return animalIndices;
    }

    public static int getItemIndex(String room) {
        Integer itemIndices = mapData.room_info.get(room).item_index;
        if(itemIndices == null) {
            throw new IllegalArgumentException("Room"+ room +"doesn't exist or has no Items");
        }
        return itemIndices;
    }

    public static int[] getPlayerLocation() {
        String[] playerCoordinates = mapData.player_location.split("_");
        return new int[]{Integer.parseInt(playerCoordinates[0]), Integer.parseInt(playerCoordinates[1])};
    }

    public static long getSeed() {
        return mapData.seed;
    }

    public static int getmapSize() {
        return mapData.map_size;
    }
}

