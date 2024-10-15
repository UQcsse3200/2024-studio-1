package com.csse3200.game.areas;

/**
 * A public interface for interacting with Levels.
 * <p>
 * Levels represent a collection of rooms,
 * typically ending in a Boss Room with stairs to a new Level.
 */
public interface Level {

    /**
     * Get a room by door key.
     *
     * @param roomKey the key that selects the room.
     * @return the fully constructed room.
     */
    Room getRoom(String roomKey);

    /**
     * Get the key for the first room the player should spawn into on this level.
     *
     * @return the key for the first room.
     */
    String getStartingRoomKey();

    /**
     * Get number for this level (the "height" in the building.)
     *
     * @return the level number.
     */
    int getLevelNumber();

    /**
     * Get the map for this level.
     *
     * @return the level map.
     */
    LevelMap getMap();
}
