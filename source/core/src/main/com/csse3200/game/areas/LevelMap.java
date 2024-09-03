package com.csse3200.game.areas;

import com.csse3200.game.entities.Room;

import java.util.Map;
import java.util.Set;

public class LevelMap {
    private final Map<String, Room> rooms;

    public LevelMap(Map<String, Room> rooms) {
        this.rooms = rooms;
    }

    public Room getRoom(String name) {
        return rooms.get(name);
    }

    public Set<String> listRooms() {
        return rooms.keySet();
    }
}
