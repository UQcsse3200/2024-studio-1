package com.csse3200.game.entities;

import com.csse3200.game.areas.MainGameArea;

/** room */
public interface Room {
    void spawn(Entity player, MainGameArea mainGameArea);

    void remove_room();
}
