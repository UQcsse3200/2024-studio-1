package com.csse3200.game.components.playerselect;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates the ui elements and functionality for the player select screen's ui.
 */
public class PlayerSelectDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(PlayerSelectDisplay.class);
    private static final float Z_INDEX = 2f;
    private final GdxGame game;
    private Table table;

    /**
     * Make the component.
     * @param game the overarching game, needed so that buttons can trigger navigation through
     *             screens
     */
    public PlayerSelectDisplay(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        // todo add the players to select from
        table = new Table();
        table.setFillParent(true);
        Button startBtn = new TextButton("Choose this player", skin, "action");
        startBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Player select button pressed");
                playerSelected();
            }
        });
        table.add(startBtn);
        stage.addActor(table);
    }

    private void playerSelected() {
        logger.info("Going to intro cutscene");
        game.setScreen(ScreenType.CUTSCENE);
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
