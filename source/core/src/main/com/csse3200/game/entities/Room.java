package com.csse3200.game.entities;

import com.csse3200.game.areas.MainGameArea;

/**
 * A room of the main game,
 * Rooms are individual screens of the game that have their own entities.
 */
public interface Room {
    /**
     * Spawn all entities that form this room.
     *
     * @param player the player that will play this room.
     * @param mainGameArea the game area to spawn this room into.
     */
    void spawn(Entity player, MainGameArea mainGameArea);

    /**
     * Remove all entities that form this room.
     */
    void removeRoom();
    public Boolean isRoomFresh = true;

}
