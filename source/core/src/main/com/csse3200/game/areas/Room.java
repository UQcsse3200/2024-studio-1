package com.csse3200.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.csse3200.game.entities.Entity;



/**
 * A room of the main game,
 * Rooms are individual screens of the game that have their own entities.
 */
public interface Room {
    /**
     * Spawn all entities that form this room.
     *
     * @param player the player that will play this room.
     * @param GameArea the game area to spawn this room into.
     */
    void spawn(Entity player, GameArea GameArea);

    String getRoomName();

    /**
     * Remove all entities that form this room.
     */
    void removeRoom();

    public boolean getIsRoomComplete();

    public void setRoomComplete();

    public void checkIfRoomComplete();

}


