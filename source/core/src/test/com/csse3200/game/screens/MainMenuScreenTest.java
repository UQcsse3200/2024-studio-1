/*
package com.csse3200.game.screens;

import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.mainmenu.MainMenuActions;
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.input.InputDecorator;
import com.csse3200.game.options.GameOptions;
import com.csse3200.game.rendering.RenderService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class MainMenuScreenTest {

    private GdxGame game;
    private Entity ui;
    private MainMenuScreen screen;

    @BeforeEach
    void setUp() {
        this.game = new GdxGame();
        game.create();
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerResourceService(new ResourceService());
        this.ui = new Entity();
        ui.addComponent(new MainMenuActions(game));
        ServiceLocator.getEntityService().register(ui);
    }

    @Test
    void gameGetsDifficulty() {
        GameOptions options = new GameOptions(GameOptions.Difficulty.EASY);
        ui.getEvents().trigger("start", options);
        assertEquals(game.gameOptions.difficulty, GameOptions.Difficulty.EASY);
    }

    // Unused for now
    @AfterEach
    void tearDown() {
    }

}
*/