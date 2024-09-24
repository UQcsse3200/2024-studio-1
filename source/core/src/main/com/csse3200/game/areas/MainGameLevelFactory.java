package com.csse3200.game.areas;

import com.csse3200.game.areas.Generation.MapGenerator;
import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.RoomFactory;
import com.csse3200.game.entities.factories.StairFactory;

import java.util.*;

/**
 * This is the main game mode.
 */
public class MainGameLevelFactory implements LevelFactory {
    private static final int DEFAULT_MAP_SIZE = 40;
    private int levelNum;

    @Override
    public Level create(int levelNumber) {
        LevelMap map = new LevelMap("seed", DEFAULT_MAP_SIZE);

        RoomFactory roomFactory = new RoomFactory(
                new NPCFactory(),
                new CollectibleFactory(),
                new TerrainFactory(levelNumber)
        );
        // Sprint 4 Switch the MapGenerator to use Rooms
        Map<String, Room> rooms = new HashMap<>();
        Set<String> room_keySet = map.mapData.getPositions().keySet();
        for (String room_key : room_keySet) {
            int itemIndex = map.mapData.getRoomDetails().get(room_key).get("item_index");
            int animalIndex = map.mapData.getRoomDetails().get(room_key).get("animal_index");
            int roomType = map.mapData.getRoomDetails().get(room_key).get("room_type");
            switch (roomType) {
                case MapGenerator.BOSSROOM:
                    rooms.put(room_key, roomFactory.createBossRoom(
                            map.mapData.getPositions().get(room_key),
                            "0,0,14,10," + levelNumber + "," + levelNumber));
                    // Not sure whether "boss" or key should be used here
//                    rooms.put("BOSS", roomFactory.createBossRoom(List.of("", "", "", "", ""),
//                            "0,0,14,10," + levelNumber + "," + levelNumber));
                    break;
                case MapGenerator.NPCROOM:
                    System.out.print("NPCRoom at " + room_key);
                    break;
                case MapGenerator.GAMEROOM:
                    System.out.print("Gameroom at " + room_key);
                    break;
                default:
                    rooms.put(room_key, roomFactory.createRoom(
                            map.mapData.getPositions().get(room_key),
                            "0,0,14,10," + animalIndex + "," + itemIndex));
                    break;
            }
        }
        return new Level(map, levelNumber, rooms);
    }

    public int getCurrentLevel() {
        return levelNum;
    }


}
