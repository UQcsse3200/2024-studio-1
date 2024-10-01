package com.csse3200.game.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.screendisplay.PlayerSelectDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

import static com.csse3200.game.GdxGame.ScreenColour.DEFAULT;
import static com.csse3200.game.entities.PlayerSelection.*;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * The screen where you select a character then start the main game.
 */
public class PlayerSelectScreen extends StaticScreen {

    /**
     * Make the screen.
     * @param game the overarching game.
     */
    public PlayerSelectScreen(GdxGame game) {
        super(
                game,
                getPlayerConfigs(List.of(PLAYERS))
                        .values()
                        .stream()
                        .map(config -> config.textureFilename)
                        .toArray(String[]::new),
                getLogger(PlayerSelectScreen.class),
                DEFAULT
        );
    }

    @Override
    protected Entity getUI() {
        Stage stage = ServiceLocator.getRenderService().getStage();
        Entity ui = new Entity();
        ui.addComponent(new InputDecorator(stage, 10))
                .addComponent(new PlayerSelectDisplay(game));
        return ui;
    }
}
