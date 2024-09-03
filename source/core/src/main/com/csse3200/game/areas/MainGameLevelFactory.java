package com.csse3200.game.areas;

import java.util.HashMap;

public class MainGameLevelFactory implements LevelFactory {
    @Override
    public Level create(int levelNumber) {
        return new Level(new LevelMap(new HashMap<>()));
    }
}
