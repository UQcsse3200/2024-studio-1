package com.csse3200.game.areas;

import com.csse3200.game.entities.Entity;

public interface RoomSpawner {
    void spawnRoom(Entity player, String specification);
}
