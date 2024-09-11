package com.csse3200.game.ui.terminal.commands;

import com.csse3200.game.GdxGame;
import org.slf4j.Logger;

import java.util.ArrayList;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Terminal command for switching between screens. Will be used to switch to newly implemented
 * screens for manual testing.
 */
public class ScreenSwitchCommand implements Command {

    private static final Logger logger = getLogger(DebugCommand.class);
    private final GdxGame game;

    /**
     * Make new instance of the screen switch command.
     * @param game the overarching game, used so that the command can switch screens
     */
    public ScreenSwitchCommand(GdxGame game) {
        this.game = game;
    }

    /**
     * Switches to the given screen.
     * @param args command args, should have one element which is the screen name that matches a
     * {@link com.csse3200.game.GdxGame.ScreenType}, case insensitive.
     * @return {@inheritDoc}
     */
    @Override
    public boolean action(ArrayList<String> args) {
        if (args.size() != 1) {
            logger.debug("Should give exactly one argument for ScreenSwitch command");
            return false;
        }
        String screenName = args.getFirst().toUpperCase();
        try {
            // Try to convert the given string argument to a ScreenType value and switch to it.
            // If it isn't a valid ScreenType, throw the exception
            game.setScreen(GdxGame.ScreenType.valueOf(screenName));
        } catch (IllegalArgumentException e) {
            logger.debug("Screen switch - not a valid screen: {}", screenName);
            return false;
        }
        return true;
    }
}
