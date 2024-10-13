package com.csse3200.game.areas.test;

import com.csse3200.game.areas.*;

/**
 * A Level Factory that creates a test environment.
 */
public class TestLevelFactory implements LevelFactory {
    @Override
    public Level create(int levelNum) {
        return new TestLevel();
    }

    @Override
    public void saveMapData(String filePath, String level) {
        // do nothing because this world can't be saved
    }
}
