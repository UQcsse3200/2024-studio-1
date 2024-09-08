package com.csse3200.game.areas;

/**
 * A level factory is essentially a "game mode",
 * it decides the behaviour of the game by supplying "levels"
 */
public interface LevelFactory {
    /**
     * Create a new Level.
     *
     * @param levelNum which level to make, 0 is the "ground floor".
     * @return the new Level.
     */
    Level create(int levelNum);
}


