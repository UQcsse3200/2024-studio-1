package com.csse3200.game.areas;

public class GameAreaService {
//    private static final Logger log = LoggerFactory.getLogger(GameAreaService.class);
    private final MainGameArea gameArea;

    public GameAreaService(MainGameArea gameArea) {
        this.gameArea = gameArea;
    }

    public MainGameArea getGameArea() {
        return this.gameArea;
    }

    public void update() {
        this.gameArea.spawnCurrentRoom();
    }
}
