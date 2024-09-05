package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.mainmenu.MainMenuActions;
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the main menu.
 */
public class MainMenuScreen extends StaticScreen {
    private static final String[] mainMenuTextures = {
            "images/box_boy_title.png", "images/bg_logo.png"
    };

    public MainMenuScreen(GdxGame game) {
        super(game, mainMenuTextures, LoggerFactory.getLogger(MainMenuScreen.class));
    }

    @Override
    protected Entity getUI() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new MainMenuDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new MainMenuActions(game));
        return ui;
    }
}
