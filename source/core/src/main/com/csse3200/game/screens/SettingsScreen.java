package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.GdxGame.ScreenColour.DEFAULT;

/**
 * The game screen containing the settings.
 */
public class SettingsScreen extends StaticScreen {

    public SettingsScreen(GdxGame game) {
        super(game, new String[0], LoggerFactory.getLogger(SettingsScreen.class), DEFAULT);
    }

    @Override
    protected Entity getUI() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new SettingsMenuDisplay(game)).addComponent(new InputDecorator(stage, 10));
        return ui;
    }

}
