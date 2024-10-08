package com.csse3200.game.components.mainmenu;

import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.components.Component;
import com.csse3200.game.options.GameOptions.Difficulty;
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
        entity.getEvents().addListener("exit", this::onExit);
        entity.getEvents().addListener("settings", this::onSettings);
        entity.getEvents().addListener("achievements", this::achievements);
        entity.getEvents().addListener("how-to-play", this::onHowToPlay);
        entity.getEvents().addListener("load-game", this::loadGame);
    }

    /**
     * Set the game's difficulty to the selected difficulty and go to the player select screen.
     * @param difficulty the difficulty chosen by the player.
     */
    private void onPlayerSelect(Difficulty difficulty, boolean shouldLoad) {
        logger.info("Going to player selection");
        game.gameOptions.difficulty = difficulty;
        game.gameOptions.shouldLoad = shouldLoad;
        game.setScreen(ScreenType.PLAYER_SELECT);
    }

    private void achievements() {
        logger.info("Going to achievements screen");
        game.setScreen(ScreenType.ACHIEVEMENTS);
    }

    /**
     * Exits the game.
     */
    private void onExit() {
        logger.info("Exit game");
        game.exit();
        System.exit(0);
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
    private void loadGame() {
        logger.info("Loading Game");
        game.setScreen(ScreenType.LOAD_GAME);
    }

}
