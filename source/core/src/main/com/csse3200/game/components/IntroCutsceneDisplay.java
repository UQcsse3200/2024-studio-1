package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
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
    private static final float Y_PADDING = 20f;
    /**
     * Proportion of the screen width to be taken up by the cutscene text.
     */
    private static final float WIDTH_PROPORTION = .8f;
    /*
    Cutscene text is largely taken from storyline wiki page:
    https://github.com/UQcsse3200/2024-studio-1/wiki/Storyline
     */
    private static final String CUTSCENE_TEXT = """
            They escaped.

            All of them.

            You tested them, tortured them, tormented them, \
            all those poor, helpless animals.

            But now you're the helpless one.

            The humans have left, or rather, they left you behind. \
            Hope you enjoyed your beer, because it may be your last.

            The beasts have broken out.

            Are you ready to put them back in their place?""";
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

        // Cutscene text
        Label label = new Label(CUTSCENE_TEXT, skin, "cutscene");
        label.setWrap(true);
        table.add(label).width(Gdx.graphics.getWidth() * WIDTH_PROPORTION);

        // Button to start game
        Button startBtn = new TextButton("I'm ready", skin, "action");
        startBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Start game button pressed");
                onStart();
            }
        });
        table.row();
        table.add(startBtn).padTop(Y_PADDING);

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
