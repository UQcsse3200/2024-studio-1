package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.howtoplaymenu.HowToPlayMenuDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.LoggerFactory;

/**
 * Screen to give player basic info about the game.
 */
public class HowToPlayScreen extends StaticScreen {

    public HowToPlayScreen(GdxGame game) {
        super(game, new String[0], LoggerFactory.getLogger(HowToPlayScreen.class));
    }

    @Override
    protected Entity getUI() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new HowToPlayMenuDisplay(game))
                .addComponent(new InputDecorator(stage, 10));
        return ui;
    }

}
