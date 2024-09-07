package com.csse3200.game.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.IntroCutsceneScreen;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates the ui elements and functionality for the intro cutscene's ui.
 */
public class IntroCutsceneDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(IntroCutsceneScreen.class);
    private static final float Z_INDEX = 2f;
    private final GdxGame game;
    private Table table;

    /**
     * Make the component.
     * @param game the overarching game, needed so that buttons can trigger navigation through
     *             screens
     */
    public IntroCutsceneDisplay(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        // todo make the cutscene more interesting
        table = new Table();
        table.setFillParent(true);
        Label label = new Label("Cutscene", skin);
        Button startBtn = new TextButton("I'm ready", skin, "action");
        startBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("I'm ready (start game) button pressed");
                onStart();
            }
        });
        table.add(startBtn);
        stage.addActor(table);
    }

    /**
     * Swaps to the Main Game screen.
     */
    private void onStart() {
        logger.info("Start game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by stage
    }
}
