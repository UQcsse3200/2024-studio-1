package com.csse3200.game.components.mainmenu;

import com.csse3200.game.GdxGame;
import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
    private GdxGame game;

    public MainMenuActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("start", this::onStart);
        entity.getEvents().addListener("load", this::onLoad);
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("settings", this::onSettings);
    }

    /**
     * Swaps to the Main Game screen.
     */
    private void onStart() {
        logger.info("Start game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    /**
     * Intended for loading a saved game state.
     * Load functionality is not actually implemented.
     */
    private void onLoad() {
        logger.info("Load game");
    }

    /**
     * Exits the game.
     */
    private void onExit() {
        logger.info("Exit game");
        game.exit();
    }

    /**
     * Swaps to the Settings screen.
     */
    private void onSettings() {
        logger.info("Launching settings screen");
        game.setScreen(GdxGame.ScreenType.SETTINGS);
    }

    /**
     * The difficulty of the game. Will likely affect map creation (number of rooms). May affect
     * other features in the future.
     */
    public enum Difficulty {
        EASY {
            @Override
            public String toString() {
                return "Easy";
            }
        }, MEDIUM {
            @Override
            public String toString() {
                return "Medium";
            }
        }, HARD {
            @Override
            public String toString() {
                return "Hard";
            }
        }
    }

}
