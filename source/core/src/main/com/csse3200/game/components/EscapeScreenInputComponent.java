package com.csse3200.game.components;

import com.badlogic.gdx.Input;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.input.InputComponent;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * A component that handles pressing of the Escape key so the user can go back to the previous
 * screen.
 */
public class EscapeScreenInputComponent extends InputComponent {

    private static final Logger logger = getLogger(EscapeScreenInputComponent.class);
    private final GdxGame game;
    private final ScreenType destinationScreen;

    private static final ScreenType DEFAULT_DESTINATION = ScreenType.MAIN_MENU;

    /**
     * Make the component.
     * @param game the game this is a part of, allows the component to trigger screen transitions.
     * @param destinationScreen the screen to go to when you press Escape.
     */
    public EscapeScreenInputComponent(GdxGame game, ScreenType destinationScreen) {
        this.game = game;
        this.destinationScreen = destinationScreen;
    }

    /**
     * Make the component. The screen transitioned to upon pressing Escape will be the main menu.
     * @param game the game this is a part of, allows the component to trigger screen transitions.
     */
    public EscapeScreenInputComponent(GdxGame game) {
        this.game = game;
        this.destinationScreen = DEFAULT_DESTINATION;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            logger.debug("User pressed Escape, starting a screen transition");
            game.setScreen(destinationScreen);
            return true;
        }
        return false;
    }
}
