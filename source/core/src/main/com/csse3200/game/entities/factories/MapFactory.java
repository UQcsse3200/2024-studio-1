package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.configs.MapConfigs;
import com.csse3200.game.files.FileLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A Factory responsible for loading and managing json file data for the game's Map
 */
public class MapFactory {
    //Assuming that the map is a static json file for the mvp loads the data from a json file called map.json
    public static final MapConfigs mapData = FileLoader.readClass(MapConfigs.class, "configs/map.json");

    /**
     * A getter method used to extract the room connection data from the json file for the map. Returns a list of
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
     * A method responsible for getting a specific room connection using an index input.
     * @param room : The room for which the connection needs to be extracted.
     * @param index : The index of the connection that is required to be extracted.
     * @return : returns a specific room that is connected to the current one.
     */
    public static int[] getRoomConnection(String room, int index) {
        List<int[]> roomConnections = getRoomConnections(room);
        //checks for out of bounds indices and returns an exception.
        if (index < 0 || index > roomConnections.size()) {
            throw new IndexOutOfBoundsException("The index" + index + "is out of bounds");
        }
        return roomConnections.get(index);
    }

    /**
     * A method responsible for extracting the index related to the type of animal to be spawned given the room number.
     * @param room : The room for which the animal index is to be extracted.
     * @return : Returns the index which specifies which animal is to be spawned in the room specified.
     */
    public static Integer getAnimalIndex(String room) {
        Integer animalIndices = mapData.room_info.get(room).animal_index;
        if (animalIndices == null) {
            throw new IllegalArgumentException("Room"+ room +"doesn't exist or has no animals");
        }
        return animalIndices;
    }

    /**
     * Method responsible for extracting the index which indicates which items are to be spawned in the room specified.
     * @param room : The room for which the item index information is needed.
     * @return : the index for which item is to be spawned.
     */
    public static int getItemIndex(String room) {
        Integer itemIndices = mapData.room_info.get(room).item_index;
        if(itemIndices == null) {
            throw new IllegalArgumentException("Room"+ room +"doesn't exist or has no Items");
        }
        return itemIndices;
    }

    /**
     * A method to extract the players start location on the map.
     * @return : returns the x and y coordinates of the player's start position on the map.
     */
    public static int[] getPlayerLocation() {
        String[] playerCoordinates = mapData.player_location.split("_");
        return new int[]{Integer.parseInt(playerCoordinates[0]), Integer.parseInt(playerCoordinates[1])};
    }

    /**
     * Method that extracts the map seed information for the randomly generated map from the json file.
     * @return : The seed of the map.
     */
    public static long getSeed() {
        return mapData.seed;
    }

    /**
     * A method to get the map size mentioned in the json file.
     * @return : returns the size of the map.
     */
    public static int getMapSize() {
        return mapData.map_size;
    }

}
