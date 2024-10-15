package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.screendisplay.WinScreenDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.GdxGame.ScreenColour.DEFAULT;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Screen shown when you win the game.
 */
public class WinScreen extends StaticScreen {

    public static final String BACKGROUND_IMAGE = "images/backgrounds/win_background.png";


    /**
     * Make the win screen.
     *
     * @param game the overarching game.
     */
    public WinScreen(GdxGame game) {
        super(game, new String[]{BACKGROUND_IMAGE}, getLogger(WinScreen.class), DEFAULT);
    }

    @Override
    protected Entity getUI() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
                .addComponent(new WinScreenDisplay(game, BACKGROUND_IMAGE));
        return ui;
    }
}
