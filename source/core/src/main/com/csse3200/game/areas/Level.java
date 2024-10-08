package com.csse3200.game.areas;

import com.csse3200.game.entities.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * A layer of the game consisting of a number of rooms for the player to overcome.
 */
public class Level {
    private static final Logger log = LoggerFactory.getLogger(Level.class);
    private final LevelMap map;
    private final int levelNumber;
    private final Map<String, Room> rooms;

    public Level(LevelMap map, int levelNumber, Map<String, Room> rooms) {
        this.map = map;
        this.levelNumber = levelNumber;
        this.rooms = rooms;
    }
    public Room getRoom(String roomKey) {
        log.info("event recognised");
        return rooms.get(roomKey);
    }

    public String getStartingRoomKey() {
        return "0_0";
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public LevelMap getMap() {return map;}
    public String toString(){
        return ""+levelNumber;
    }
}
