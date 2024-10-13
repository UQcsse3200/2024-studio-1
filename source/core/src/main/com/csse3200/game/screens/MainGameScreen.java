package com.csse3200.game.screens;

import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.MapLoadConfig;
import com.csse3200.game.options.GameOptions;

import static com.csse3200.game.options.GameOptions.Difficulty.TEST;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: <a href="https://happycoding.io/tutorials/libgdx/game-screens">...</a>
 */
public class MainGameScreen extends GameScreen {

    /**
     * The main game when creating a new game.
     * @param game the game to run it on.
     */
    public MainGameScreen(GdxGame game) {
        super(game);

        GameOptions gameOptions = game.gameOptions;
        logger.info("Starting game with difficulty {}", gameOptions.difficulty.toString());

        Entity player = gameOptions.playerFactory.create(gameOptions.difficulty);
        player.getEvents().addListener("player_finished_dying", this::loseGame);

        logger.debug("Initialising main game screen entities");
        MapLoadConfig mapConfig = new MapLoadConfig();
        mapConfig.currentLevel = "0";
        LevelFactory levelFactory = new MainGameLevelFactory(false, mapConfig);
        if (gameOptions.difficulty == TEST) {
            new TestGameArea(levelFactory, player);
        } else {
            new MainGameArea(levelFactory, player, false, mapConfig);
        }
    }
}
