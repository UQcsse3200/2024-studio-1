package com.csse3200.game.entities.factories;

import com.csse3200.game.entities.configs.MapConfigs;
import com.csse3200.game.files.FileLoader;

import java.util.ArrayList;
import java.util.List;


public class MapFactory {
    private static MapConfigs mapData;

    public static void loadMap (String filePath) {
        mapData = FileLoader.readClass(MapConfigs.class, filePath);
        if (mapData == null){
            throw new IllegalStateException("Failed to load mapData from " + filePath);
        }
    }


    public static List<int[]> getRoomConnections(String room) {
        List<String> connections = mapData.room_connections.get(room);
        List<int[]> coordinatesList = new ArrayList<>();
        for (String connection : connections) {
            //Splitting the room connection coordinates into a list of [x,y]
            //coordinates
            String[] parts = connection.split("_");
            int[] coordinates = new int[]{Integer.parseInt(parts[0]), Integer.parseInt(parts[1])};
            coordinatesList.add(coordinates);
        }
        return coordinatesList;
    }

    public static Integer getAnimalIndex(String room) {
        return mapData.room_info.get(room).animal_index;
    }

    public static Integer getItemIndex(String room) {
        return mapData.room_info.get(room).item_index;
    }

    public static String getPlayerLocation() {
        return mapData.player_location;
    }

    public static long getSeed() {
        return mapData.seed;
    }

    public static int mapSize() {
        return mapData.map_size;
    }
}

