package com.csse3200.game.screens;

import com.csse3200.game.GdxGame;
import com.csse3200.game.areas.*;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.MapLoadConfig;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.entities.factories.PlayerFactory;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.options.GameOptions;
import com.csse3200.game.services.*;
import com.csse3200.game.ui.terminal.Terminal;
import com.csse3200.game.ui.terminal.TerminalDisplay;
import com.csse3200.game.options.GameOptions.Difficulty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.csse3200.game.GdxGame.ScreenType.LOSE;
import static com.csse3200.game.areas.GameController.MAP_SAVE_PATH;
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
        boolean shouldLoad = false;
        GameArea gameArea = new GameArea();

        GameOptions gameOptions = game.gameOptions;
        logger.info("Starting game with difficulty {}", gameOptions.difficulty.toString());

        Entity player = gameOptions.playerFactory.create(gameOptions.difficulty);
        player.getEvents().addListener("player_finished_dying", this::loseGame);

        logger.debug("Initialising main game screen entities");
        MapLoadConfig mapConfig = new MapLoadConfig();
        mapConfig.currentLevel = "0";
        LevelFactory levelFactory = new MainGameLevelFactory(false, mapConfig);
        if (gameOptions.difficulty == TEST) {
            new TestGameArea(gameArea, levelFactory, player);
        } else {
            new GameController(gameArea, levelFactory, player, shouldLoad, mapConfig);
        }
    }
}
