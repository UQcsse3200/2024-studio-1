package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.EscapeScreenInputComponent;
import com.csse3200.game.components.MenuExitComponent;
import com.csse3200.game.components.achievementsscreen.AchievementsListComponent;
import com.csse3200.game.components.screendisplay.AchievementsScreenDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.GdxGame.ScreenColour.DEFAULT;

/**
 * Screen to show information about current player achievements, accessed from main menu.
 */
public class AchievementsScreen extends StaticScreen {

    /**
     * Create the screen. Achievement image assets are loaded in
     * {@link AchievementsScreenDisplay}, not here.
     * @param game the overarching game, needed so that the exit button can trigger screen
     *             transitions.
     */
    public AchievementsScreen(GdxGame game) {
        // textures will be loaded after achievements are read
        super(game, new String[0], LoggerFactory.getLogger(AchievementsScreen.class), DEFAULT);
    }

    @Override
    protected Entity getUI() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
                .addComponent(new AchievementsScreenDisplay())
                .addComponent(new AchievementsListComponent())
                .addComponent(new MenuExitComponent(game))
                .addComponent(new EscapeScreenInputComponent(game));
        return ui;
    }
}
