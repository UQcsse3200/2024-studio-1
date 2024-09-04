package com.csse3200.game.areas;

public class GameAreaService {
    private final MainGameArea gameArea;

    public GameAreaService(MainGameArea gameArea) {
        this.gameArea = gameArea;
    }

    public MainGameArea getGameArea() {
        return this.gameArea;
    }

    
}
