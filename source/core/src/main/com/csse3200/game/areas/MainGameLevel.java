package com.csse3200.game.areas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * A layer of the game consisting of a number of rooms for the player to overcome.
 */
public class MainGameLevel implements Level {
    private static final Logger log = LoggerFactory.getLogger(MainGameLevel.class);
    private final LevelMap map;
    private final int levelNumber;
    private final Map<String, Room> rooms;

    public MainGameLevel(LevelMap map, int levelNumber, Map<String, Room> rooms) {
        this.map = map;
        this.levelNumber = levelNumber;
        this.rooms = rooms;
    }

    @Override
    public Room getRoom(String roomKey) {
        log.info("event recognised");
        return rooms.get(roomKey);
    }

    @Override
    public String getStartingRoomKey() {
        return "0_0";
    }

    @Override
    public int getLevelNumber() {
        return levelNumber;
    }

    @Override
    public LevelMap getMap() {
        return map;
    }

    @Override
    public String toString() {
        return "" + levelNumber;
    }
}
