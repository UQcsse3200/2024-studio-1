package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.mainmenu.MainMenuActions;
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;

import static com.csse3200.game.GdxGame.ScreenColour.DEFAULT;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * The game screen containing the main menu.
 */
public class MainMenuScreen extends StaticScreen {

    private static final String[] mainMenuTextures = {"images/bg_logo.png"};
    private MainMenuDisplay mainMenuDisplay;

    /**
     * Make the screen.
     * @param game the overarching game.
     */
    public MainMenuScreen(GdxGame game) {
        super(game, mainMenuTextures, getLogger(MainMenuScreen.class), DEFAULT);
    }

    @Override
    public void resize(int width, int height) {
        mainMenuDisplay.resize(width, height);
        super.resize(width, height);
    }

    @Override
    protected Entity getUI() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        mainMenuDisplay = new MainMenuDisplay();
        ui.addComponent(mainMenuDisplay)
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new MainMenuActions(game));
        return ui;
    }
}
