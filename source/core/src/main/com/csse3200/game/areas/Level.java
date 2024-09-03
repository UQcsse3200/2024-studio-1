package com.csse3200.game.areas;

import com.csse3200.game.entities.Room;

/**
 * A layer of the game consisting of a number of rooms for the player to overcome.
 */
public class Level {
    private final LevelMap map;

    public Level(LevelMap map) {
        this.map = map;
    }

    public Room getRoom(String roomKey) {
        return new Room();
    }
}
