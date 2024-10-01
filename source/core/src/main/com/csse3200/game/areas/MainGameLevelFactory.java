package com.csse3200.game.areas;

import com.csse3200.game.areas.generation.MapGenerator;
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

        if (!shouldLoad) {
            map = new LevelMap("seed", DEFAULT_MAP_SIZE);
        } else {
            loadFromJson(MAP_SAVE_PATH);
            map = new LevelMap(loadedSeed, DEFAULT_MAP_SIZE);
        }
            RoomFactory roomFactory = new RoomFactory(
                    new NPCFactory(),
                    new CollectibleFactory(),
                    new TerrainFactory(levelNumber)
            );
            // Sprint 4 Switch the MapGenerator to use Rooms
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

                    System.out.println("THIS IS THE BOSS_ROOM LOCATION " +  roomKey);
                    break;
                case MapGenerator.NPCROOM:
                rooms.put(roomKey, roomFactory.createShopRoom(
                    map.mapData.getPositions().get(roomKey),
                    "0,0,14,10," + levelNumber + "," + levelNumber, roomKey));
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
            //creating and adding a boss room instance into the Map containing the rooms for
            // the level
        if (shouldLoad) {
            setRoomsComplete(loadedRooms);
            shouldLoad = false;
        }
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
        FileLoader.writeClass(completedRooms, filePath, FileLoader.Location.LOCAL);
    }

    public void loadFromJson (String filePath) {
        MapLoadConfig mapLoadConfig = new MapLoadConfig();
        mapLoadConfig.savedMap = FileLoader.readClass(ArrayList.class, filePath, FileLoader.Location.LOCAL);
        loadedSeed = mapLoadConfig.savedMap.getFirst();
        mapLoadConfig.savedMap.remove(0);
        loadedRooms.addAll(mapLoadConfig.savedMap);
    }
    /**
     * Sets the rooms that have been completed in the saved game as completed in the loaded
     * game.
     * @param roomNames Room keys that have been completed.
     */

    public void setRoomsComplete(List<String> roomNames) {
        for (String roomName : roomNames) {
            rooms.get(roomName).setIsRoomComplete();
        }
    }

    public int getCurrentLevel() {
        return levelNum;
    }


}
