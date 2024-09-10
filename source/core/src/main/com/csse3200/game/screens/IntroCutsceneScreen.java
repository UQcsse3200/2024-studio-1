package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenColour;
import com.csse3200.game.components.screendisplay.IntroCutsceneDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;

import static com.csse3200.game.services.ServiceLocator.getRenderService;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Cutscene shown at the start of the game, right after choosing a player.
 */
public class IntroCutsceneScreen extends StaticScreen {

    // todo add textures as needed
    private static final String[] cutsceneTextures = {};
    private static final int PRIORITY = 10;

    /**
     * Make the screen.
     * @param game the overarching game.
     */
    public IntroCutsceneScreen(GdxGame game) {
        super(game, cutsceneTextures, getLogger(IntroCutsceneScreen.class));
        game.setScreenColour(ScreenColour.BLACK);
    }

    @Override
    protected Entity getUI() {
        Stage stage = getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new IntroCutsceneDisplay(game))
                .addComponent(new InputDecorator(stage, PRIORITY));
        return ui;
    }
}
