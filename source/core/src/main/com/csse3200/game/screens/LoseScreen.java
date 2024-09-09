package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.screendisplay.LoseScreenDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.LoggerFactory;

/**
 * Screen shown when you lose the game (die).
 */
// todo implement restarting
public class LoseScreen extends StaticScreen {

    // todo add citation
    /**
     * Dead player image, edited from assets/images/player/player.png
     */
    public static final String PLAYER_DEAD = "images/player/player_dead.png";

    /**
     * Make the death screen.
     * @param game the overarching game.
     */
    public LoseScreen(GdxGame game) {
        super(game, new String[]{PLAYER_DEAD}, LoggerFactory.getLogger(LoseScreen.class));
        game.setScreenColour(GdxGame.ScreenColour.BLACK);
    }

    @Override
    protected Entity getUI() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
                .addComponent(new LoseScreenDisplay(game));
        return ui;
    }
}
