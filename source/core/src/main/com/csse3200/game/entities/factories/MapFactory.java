package com.csse3200.game.entities.factories;

import com.csse3200.game.areas.Generation.MapGenerator;
import com.csse3200.game.entities.configs.MapConfigs;
import com.csse3200.game.files.FileLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A Factory responsible for loading and managing json file data for the game's Map
 */
public class MapFactory {
    //Assuming that the map is a static json file for the mvp loads the data from a json file called map.json

    public static MapConfigs mapData;

    /** A loader method which loads the map from either a json file or from the Map Generator class. **/
    public static MapConfigs loadMap(String fileDirectory){
        if (fileDirectory != null) {
            mapData = FileLoader.readClass(MapConfigs.class, fileDirectory);
        } else {
            MapGenerator mapGenerator = new MapGenerator(10, "224590ginger5ut");
            mapGenerator.createMap();

            mapData = new MapConfigs();
            mapData.roomConnections = mapGenerator.getPositions();
            List<String> rooms = new ArrayList<>(mapGenerator.getRoomDetails().keySet());
            for (String room : rooms) {
               mapData.roomInfo.get(room).animalIndex = mapGenerator.getRoomDetails().get(room).get("animal_index");
               mapData.roomInfo.get(room).itemIndex = mapGenerator.getRoomDetails().get(room).get("item_index");
            }
            mapData.playerLocation = mapGenerator.startingRoom;
            mapData.mapSize = mapGenerator.getMapSize();
            mapData.seed = mapGenerator.getMapSeed();
        }
        return mapData;
    }

    /**
     * A getter method used to extract the room connection data from the json file for the map. Returns a list of
     * rooms that the input room is connected to.
     * @param room : The room whose connections are required to be extracted.
     * @return : A list of integer arrays which contain coordinates of the rooms that are connected to the room input.
     */
    public static List<String> getRoomConnections(String room) {
        List<String> connections = mapData.roomConnections.get(room);
        if (connections == null) {
            throw new IllegalArgumentException("Room" + room + "doesn't exist or has no connections");
        }
        return connections;
    }

    /**
     * A method responsible for getting a specific room connection using an index input.
     * @param room : The room for which the connection needs to be extracted.
     * @param index : The index of the connection that is required to be extracted.
     * @return : returns a specific room that is connected to the current one.
     */
    public static String getRoomConnection(String room, int index) {
        List<String> roomConnections = getRoomConnections(room);
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
        Integer animalIndices = mapData.roomInfo.get(room).animalIndex;
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
        Integer itemIndices = mapData.roomInfo.get(room).itemIndex;
        if(itemIndices == null) {
            throw new IllegalArgumentException("Room"+ room +"doesn't exist or has no Items");
        }
        return itemIndices;
    }

    /**
     * A method to extract the players start location on the map.
     * @return : returns string coordinates of the player's start position on the map.
     */
    public static String getPlayerLocation() {
        String playerCoordinates = mapData.playerLocation;
        if (playerCoordinates == null) {
            throw new IllegalArgumentException("Player coordinates doesn't exist");
        }
        return playerCoordinates;
    }

    /**
     * Method that extracts the map seed information for the randomly generated map from the json file.
     * @return : The seed of the map.
     */
    public static String getSeed() {
        return mapData.seed;
    }

    /**
     * A method to get the map size mentioned in the json file.
     * @return : returns the size of the map.
     */
    public static int getMapSize() {
        return mapData.mapSize;
    }

}
