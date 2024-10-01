package com.csse3200.game.areas;

import com.csse3200.game.areas.Generation.MapGenerator;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.configs.MapLoadConfig;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.RoomFactory;
import com.csse3200.game.files.FileLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This is the main game mode.
 */
public class MainGameLevelFactory implements LevelFactory {
    private static final int DEFAULT_MAP_SIZE = 40;
    private static final Logger log = LoggerFactory.getLogger(MainGameLevelFactory.class);
    public static final String MAP_SAVE_PATH = "saves/MapSave.json";
    private int levelNum;
    private final Map<String, Room> rooms;
    private final List<String> completedRooms;
    private LevelMap map;
    private boolean shouldLoad;
    private String loadedSeed = "";
    private List<String> loadedRooms;


    public MainGameLevelFactory(boolean shouldLoad) {
        this.shouldLoad = shouldLoad;
        rooms = new HashMap<>();
        loadedRooms = new ArrayList<>();
        completedRooms = new ArrayList<>();
    }

    @Override
    public Level create(int levelNumber) {
        String seed;
        // default seed for junit tests
        if (!shouldLoad) {
            seed = "seed";
            map = new LevelMap(seed + levelNumber, DEFAULT_MAP_SIZE);
        } else {
            // For loaded games, append the level number to the loaded seed
            loadFromJson(MAP_SAVE_PATH);
            map = new LevelMap(loadedSeed, DEFAULT_MAP_SIZE);
        }

        RoomFactory roomFactory = new RoomFactory(
                new NPCFactory(),
                new CollectibleFactory(),
                new TerrainFactory(levelNumber)
        );

        Set<String> roomKeySet = map.mapData.getPositions().keySet();
        for (String roomKey : roomKeySet) {
            int itemIndex = map.mapData.getRoomDetails().get(roomKey).get("item_index");
            int animalIndex = map.mapData.getRoomDetails().get(roomKey).get("animal_index");
            int roomType = map.mapData.getRoomDetails().get(roomKey).get("room_type");
            switch (roomType) {
                case MapGenerator.BOSSROOM:
                    rooms.put(roomKey, roomFactory.createBossRoom(
                            map.mapData.getPositions().get(roomKey),
                            "0,0,14,10," + levelNumber + "," + levelNumber, roomKey));
                    break;
                case MapGenerator.NPCROOM:
                    rooms.put(roomKey, roomFactory.createShopRoom(
                            map.mapData.getPositions().get(roomKey),
                            "0,0,14,10," + 0 + "," + levelNumber, roomKey));
                    break;
                case MapGenerator.GAMEROOM:
                    rooms.put(roomKey, roomFactory.createGambleRoom(
                            map.mapData.getPositions().get(roomKey),
                            "0,0,14,10," + levelNumber + "," + levelNumber, roomKey));
                    break;
                default:
                    rooms.put(roomKey, roomFactory.createRoom(
                            map.mapData.getPositions().get(roomKey),
                            "0,0,14,10," + animalIndex + "," + itemIndex, roomKey));
                    break;
            }
        }

        if (shouldLoad) {
            setRoomsComplete(loadedRooms);
            shouldLoad = false;
        }

        // Store the current level number
        this.levelNum = levelNumber;

        return new Level(map, levelNumber, rooms);
    }


    /**
     * Exports the map data to a JSON file.
     *
     * @param filePath The path of the file to write the JSON data to.
     */
    public void exportToJson(String filePath) {
        completedRooms.add(map.mapData.getMapSeed());
        for (Room room : rooms.values()) {
            if (room.getIsRoomComplete()) {
                completedRooms.add(room.getRoomName());
            }
        }
        FileLoader.writeClass(completedRooms, filePath, FileLoader.Location.EXTERNAL);
    }


    /**
     * Loads map data from the save file and extracts the completed rooms into a list which
     * is later set to completed in the create method above
     * @param filePath: Path for the save file
     */
    public void loadFromJson (String filePath) {
        MapLoadConfig mapLoadConfig = new MapLoadConfig();
        mapLoadConfig.savedMap = FileLoader.readClass(ArrayList.class, filePath, FileLoader.Location.EXTERNAL);
        loadedSeed = mapLoadConfig.savedMap.getFirst();
        mapLoadConfig.savedMap.remove(0);
        loadedRooms.addAll(mapLoadConfig.savedMap);
    }
    /**
     * Sets the rooms that have been completed in the saved game as completed in the loaded
     * game.
     * @param roomNames Room keys that have been completed.
     */

    public void setRoomsComplete( List<String> roomNames) {
        for (String roomName : roomNames) {
            rooms.get(roomName).setIsRoomComplete();
            }
    }

    public int getCurrentLevel() {
        return levelNum;
    }


}
