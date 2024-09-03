package com.csse3200.game.areas;

import com.csse3200.game.areas.terrain.TerrainFactory;
import com.csse3200.game.entities.Room;
import com.csse3200.game.entities.factories.CollectibleFactory;
import com.csse3200.game.entities.factories.NPCFactory;
import com.csse3200.game.entities.factories.RoomFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the main game mode.
 */
public class MainGameLevelFactory implements LevelFactory {
    private static final int DEFAULT_MAP_SIZE = 20;

    @Override
    public Level create(int levelNumber) {
        LevelMap map = new LevelMap("seed", DEFAULT_MAP_SIZE);
        RoomFactory roomFactory = new RoomFactory(
                new NPCFactory(),
                new CollectibleFactory(),
                new TerrainFactory()
        );
        Map<String, Room> rooms = new HashMap<>();
        // FIXME make ALL the real Rooms
        rooms.put("0_0", roomFactory.createRoom("0,0,14,10,0,0"));
        return new Level(map, levelNumber, rooms);
    }
}
