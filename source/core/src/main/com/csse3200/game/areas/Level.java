package com.csse3200.game.areas;

import com.csse3200.game.entities.Room;

import java.util.Map;

/**
 * A layer of the game consisting of a number of rooms for the player to overcome.
 */
public class Level {
    private final LevelMap map;
    private final int levelNumber;
    private final Map<String, Room> rooms;
    public int roomTraversals;

    public Level(LevelMap map, int levelNumber, Map<String, Room> rooms) {
        this.map = map;
        this.levelNumber = levelNumber;
        this.rooms = rooms;
        this.roomTraversals = 0;
    }
    public Room getRoom(String roomKey) {
        return rooms.get(roomKey);
    }

    public String getStartingRoomKey() {
        return "0_0";
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getRoomTraversals() { return roomTraversals;}

}
