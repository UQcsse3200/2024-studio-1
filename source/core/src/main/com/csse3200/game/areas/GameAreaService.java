package com.csse3200.game.areas;

public class GameAreaService {
//    private static final Logger log = LoggerFactory.getLogger(GameAreaService.class);
    private final GameController gameController;

    public GameAreaService(GameController gameArea) {
        this.gameController = gameArea;
    }

    public GameArea getGameArea() {
        return this.gameController.getGameArea();
    }

    public GameController getGameController() {
        return this.gameController;
    }

    public void update() {
        this.gameController.spawnCurrentRoom();
    }
}
