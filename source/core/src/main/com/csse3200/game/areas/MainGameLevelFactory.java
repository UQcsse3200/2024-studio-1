package com.csse3200.game.areas;

import com.csse3200.game.areas.Generation.MapGenerator;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.RoomFactory;
import com.csse3200.game.entities.factories.StairFactory;
import com.csse3200.game.files.FileLoader;

import java.util.*;

/**
 * This is the main game mode.
 */
public class MainGameLevelFactory implements LevelFactory {
    private static final int DEFAULT_MAP_SIZE = 40;
    private int levelNum;
    Map<String, Room> rooms = new HashMap<>();;

    @Override
    public Level create(int levelNumber) {
        LevelMap map = new LevelMap("seed", DEFAULT_MAP_SIZE);

        RoomFactory roomFactory = new RoomFactory(
                new NPCFactory(),
                new CollectibleFactory(),
                new TerrainFactory(levelNumber)
        );
        // Sprint 4 Switch the MapGenerator to use Rooms
        Set<String> roomKeySet = map.mapData.getPositions().keySet();
        //if (loadGame) {
            // rooms = map from config
        //else{
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
                    System.out.print("NPCRoom at " + roomKey);
                    break;
                case MapGenerator.GAMEROOM:
                    System.out.print("Gameroom at " + roomKey);
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
        return new Level(map, levelNumber, rooms);
    }
    /**
     * Exports the map data to a JSON file.
     *
     * @param filePath The path of the file to write the JSON data to.
     */
    public void exportToJson(String filePath) {
        FileLoader.writeClass(rooms, filePath);

    }
    public int getCurrentLevel() {
        return levelNum;
    }


}
