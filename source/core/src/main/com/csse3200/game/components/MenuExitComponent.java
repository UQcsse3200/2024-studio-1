package com.csse3200.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;

import static com.csse3200.game.GdxGame.ScreenType.MAIN_MENU;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Add a button at the bottom left to exit to the main menu.
 */
public class MenuExitComponent extends UIComponent {

    private static final Logger logger = getLogger(MenuExitComponent.class);

    private Table exitTable;
    private GdxGame game;

    /**
     * Make the component.
     * @param game the game, needed so the exit button can trigger screen transitions.
     */
    public MenuExitComponent(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        super.create();

        exitTable = new Table();
        exitTable.setFillParent(true);
        exitTable.defaults().pad(20f);
        exitTable.bottom().left();

        TextButton button = new TextButton("Exit", skin);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.info("Exiting back to main menu");
                game.setScreen(MAIN_MENU);
            }
        });
        exitTable.add(button);

        stage.addActor(exitTable);

        // Combine the Stage input processor with a custom InputAdapter using InputMultiplexer
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage); // Retain UI click functionality
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    logger.info("ESC key pressed, exiting back to main menu");
                    game.setScreen(MAIN_MENU);
                    return true;
                }
                return false;
            }
        });

        Gdx.input.setInputProcessor(multiplexer); // Set the multiplexer as the input processor
    }

    @Override
    public void dispose() {
        exitTable.clear();
        super.dispose();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw handled by stage
    }
}