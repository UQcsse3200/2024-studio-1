package com.csse3200.game.entities;

import com.csse3200.game.areas.GameArea;

import com.badlogic.gdx.math.GridPoint2;


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
    void spawn(Entity player, GameArea mainGameArea);

    String getRoomName();

    /**
     * Remove all entities that form this room.
     */
    void removeRoom();

    public boolean getIsRoomComplete();

    public void setIsRoomComplete();

    public void checkIfRoomComplete();

}


