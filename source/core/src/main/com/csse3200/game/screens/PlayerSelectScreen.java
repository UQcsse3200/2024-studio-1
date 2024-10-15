package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.EscapeScreenInputComponent;
import com.csse3200.game.components.MenuExitComponent;
import com.csse3200.game.components.screendisplay.PlayerSelectDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.GdxGame.ScreenColour.DEFAULT;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * The screen where you select a character then start the main game.
 */
public class PlayerSelectScreen extends StaticScreen {

    public static final String BG_IMAGE = "images/bg_logo.png";

    /**
     * Make the screen.
     * @param game the overarching game.
     */
    public PlayerSelectScreen(GdxGame game) {
        super(game, getTextures(), getLogger(PlayerSelectScreen.class), DEFAULT);
    }

    private static String[] getTextures() {
        // Player images are loaded when you create them. If this is changed, they would probably
        // need to be added here instead
        return new String[]{BG_IMAGE};
    }

    @Override
    protected Entity getUI() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
                .addComponent(new PlayerSelectDisplay(game))
                .addComponent(new MenuExitComponent(game))
                .addComponent(new EscapeScreenInputComponent(game));
        return ui;
    }
}
