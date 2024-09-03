package com.csse3200.game.areas;

public class MainGameLevelFactory implements LevelFactory {
    private static final int DEFAULT_MAP_SIZE = 20;

    @Override
    public Level create(int levelNumber) {
        LevelMap map = new LevelMap("seed", DEFAULT_MAP_SIZE);
        return new Level(map);
    }
}
