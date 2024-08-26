package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.options.GameOptions;
import com.csse3200.game.options.GameOptions.Difficulty;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    /**
     * Spacing between UI buttons.
     */
    private static final float BTN_SPACING = 15f;
    private Table table;
    /**
     * A nested table that contains the buttons for difficulty selection
     */
    private Table diffBtnsTable;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        Image title = new Image(
                ServiceLocator.getResourceService().getAsset(
                        "images/box_boy_title.png", Texture.class
                )
        );
        diffBtnsTable = new Table();

        Label startBtn = new Label("Start", skin);
        EnumMap<Difficulty, TextButton> difficultyBtns = new EnumMap<>(Difficulty.class);
        for (Difficulty diff : Difficulty.values()) {
            difficultyBtns.put(diff, new TextButton(diff.toString(), skin, "action"));
        }
        TextButton howToPlayBtn = new TextButton("How To Play", skin);
        TextButton loadBtn = new TextButton("Load", skin);
        TextButton settingsBtn = new TextButton("Settings", skin);
        TextButton exitBtn = new TextButton("Exit", skin);

        // Triggers an event when the button is pressed
        startBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        entity.getEvents().trigger("start");
                    }
                });

        difficultyBtns.forEach((difficulty, btn) -> btn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        GameOptions options = new GameOptions(difficulty);
                        logger.debug("{} difficulty button clicked", difficulty.toString());
                        entity.getEvents().trigger("start", options);
                    }
                }
        ));

        howToPlayBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Settings button clicked");
                        entity.getEvents().trigger("settings");
                    }
                });

        loadBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Load button clicked");
                        entity.getEvents().trigger("load");
                    }
                });

        settingsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Settings button clicked");
                        entity.getEvents().trigger("settings");
                    }
                });

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        table.add(title);
        table.row();
        table.add(startBtn).padTop(BTN_SPACING * 2);
        table.row();
        for (TextButton btn : difficultyBtns.values()) {
            diffBtnsTable.add(btn).spaceLeft(BTN_SPACING).spaceRight(BTN_SPACING);
        }
        table.add(diffBtnsTable).padTop(BTN_SPACING);
        table.row();
        table.add(howToPlayBtn).padTop(BTN_SPACING);
        table.row();
        table.add(loadBtn).padTop(BTN_SPACING);
        table.row();
        table.add(settingsBtn).padTop(BTN_SPACING);
        table.row();
        table.add(exitBtn).padTop(BTN_SPACING);

        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        diffBtnsTable.clear();
        super.dispose();
    }

}
