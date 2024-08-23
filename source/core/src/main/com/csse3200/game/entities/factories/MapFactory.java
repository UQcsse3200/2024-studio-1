package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.configs.MapConfigs;
import com.csse3200.game.files.FileLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * A Factory responsible for loading and managing json file data for the game's Map
 */
public class MapFactory {
    //Assuming that the map is a static json file for the mvp loads the data from a json file called map.json
    public static final MapConfigs mapData = FileLoader.readClass(MapConfigs.class, "configs/map.json");

    /**
     * A getter method used to extract the room connection data from the json file for the map. returns a list of
     * rooms that the input room is connected to.
     * @param room : The room whose connections are required to be extracted.
     * @return : A list of integer arrays which contain coordinates of the rooms that are connected to the room input.
     */
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

    /**
     *
     * @param room
     * @param index
     * @return
     */
    public static int[] getRoomConnection(String room, int index) {
        List<int[]> roomConnections = getRoomConnections(room);
        if (index < 0 || index > roomConnections.size()) {
            throw new IndexOutOfBoundsException("The index" + index + "is out of bounds");
        }
        return roomConnections.get(index);
    }

    /**
     *
     * @param room
     * @return
     */
    public static Integer getAnimalIndex(String room) {
        Integer animalIndices = mapData.room_info.get(room).animal_index;
        if (animalIndices == null) {
            throw new IllegalArgumentException("Room"+ room +"doesn't exist or has no animals");
        }
        return animalIndices;
    }

    /**
     *
     * @param room
     * @return
     */
    public static int getItemIndex(String room) {
        Integer itemIndices = mapData.room_info.get(room).item_index;
        if(itemIndices == null) {
            throw new IllegalArgumentException("Room"+ room +"doesn't exist or has no Items");
        }
        return itemIndices;
    }

    /**
     *
     * @return
     */
    public static int[] getPlayerLocation() {
        String[] playerCoordinates = mapData.player_location.split("_");
        return new int[]{Integer.parseInt(playerCoordinates[0]), Integer.parseInt(playerCoordinates[1])};
    }

    /**
     *
     * @return
     */
    public static long getSeed() {
        return mapData.seed;
    }

    /**
     *
     * @return
     */
    public static int getmapSize() {
        return mapData.map_size;
    }
}

