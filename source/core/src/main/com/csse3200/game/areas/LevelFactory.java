package com.csse3200.game.areas;

public interface LevelFactory {
    /**
     * Create a new Level.
     *
     * @param levelNum which level to make, 0 is the "ground floor".
     * @return the new Level.
     */
    Level create(int levelNum);
}


