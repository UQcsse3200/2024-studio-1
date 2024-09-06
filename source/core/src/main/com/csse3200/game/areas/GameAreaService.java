package com.csse3200.game.areas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameAreaService {
    private static final Logger log = LoggerFactory.getLogger(GameAreaService.class);
    private final MainGameArea gameArea;

    public GameAreaService(MainGameArea gameArea) {
        this.gameArea = gameArea;
    }

    public MainGameArea getGameArea() {
        return this.gameArea;
    }

    public void update() {
        log.info("update");
        this.gameArea.spawnCurrentRoom();
    }
}
