package com.csse3200.game.areas.test;

import com.csse3200.game.areas.Level;
import com.csse3200.game.areas.LevelFactory;
import com.csse3200.game.areas.LevelMap;
import com.csse3200.game.areas.generation.TestMapGenerator;

import java.util.Map;

/**
 * A Level Factory that creates a test environment.
 */
public class TestLevelFactory implements LevelFactory {
    @Override
    public Level create(int levelNum) {
        return new Level(
                new LevelMap(new TestMapGenerator()),
                0,
                Map.of("0_0", new TestRoom())
        );
    }

    @Override
    public void saveMapData(String filePath, String level) {
        // do nothing because this world can't be saved
    }
}
