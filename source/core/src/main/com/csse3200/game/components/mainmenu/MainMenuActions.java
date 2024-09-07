package com.csse3200.game.components.mainmenu;

import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.components.Component;
import com.csse3200.game.options.GameOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Main Menu Screen and does something when one of the
 * events is triggered.
 */
public class MainMenuActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuActions.class);
    private final GdxGame game;

    /**
     * Make the component.
     * @param game the overarching game, needed so that buttons can trigger navigation through
     *             screens
     */
    public MainMenuActions(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("player_select", this::onPlayerSelect);
        entity.getEvents().addListener("load", this::onLoad);
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("settings", this::onSettings);
        entity.getEvents().addListener("how-to-play", this::onHowToPlay);
    }

    /**
     * Set the game options to the selected options and go to the player select screen.
     * @param options the options chosen by the player to use for the game.
     */
    private void onPlayerSelect(GameOptions options) {
        logger.info("Going to player selection");
        game.gameOptions = options;
        game.setScreen(ScreenType.PLAYER_SELECT);
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
        game.setScreen(ScreenType.SETTINGS);
    }

    private void onHowToPlay() {
        logger.info("Launching how to play screen");
        game.setScreen(ScreenType.HOW_TO_PLAY);
    }

}
