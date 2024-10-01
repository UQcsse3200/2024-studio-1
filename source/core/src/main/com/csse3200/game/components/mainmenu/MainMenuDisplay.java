package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.options.GameOptions.Difficulty;
import com.csse3200.game.screens.MainMenuScreen;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;

import static com.csse3200.game.files.FileLoader.Location.EXTERNAL;
import static com.csse3200.game.files.FileLoader.Location.LOCAL;
import static com.csse3200.game.files.FileLoader.fileExists;
import static com.csse3200.game.services.ServiceLocator.getResourceService;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    /**
     * Spacing between UI buttons.
     */
    private static final float BTN_SPACING = 8f;
    private Table table;
    /**
     * A nested table that contains the buttons for difficulty selection
     */
    private Table diffBtnsTable;

    private Image bg_logo;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Check if all load files exist.
     * @return true if all load files exist, false otherwise.
     */
    private static boolean loadFilesExist() {
        for (String path : MainMenuScreen.SAVE_PATHS) {
            if (!fileExists(path, LOCAL)) {
                logger.info("Save file not found: {}", path);
                return false;
            }
        }
        logger.info("Can load from save files - all save files found");
        return true;
    }

    private void addActors() {
        UserSettings.Settings settings = UserSettings.get();

        table = new Table();
        table.setFillParent(true);
        bg_logo = new Image(
                getResourceService().getAsset("images/bg_logo.png", Texture.class));

        diffBtnsTable = new Table();

        Label startBtn = new Label("Start", skin);
        EnumMap<Difficulty, TextButton> difficultyBtns = new EnumMap<>(Difficulty.class);
        for (Difficulty diff : Difficulty.values()) {
            difficultyBtns.put(diff, new TextButton(diff.toString(), skin, "action"));
        }
        TextButton howToPlayBtn = new TextButton("How To Play", skin);
        TextButton achievementsBtn = new TextButton("Achievements", skin);
        TextButton settingsBtn = new TextButton("Settings", skin);
        TextButton exitBtn = new TextButton("Exit", skin);

        CheckBox shouldLoadBtn = new CheckBox("Load from save file", skin, "load-btn");
        boolean canLoad = loadFilesExist();
        shouldLoadBtn.setChecked(true);

        if (settings.displayMode == null) {
            settings.displayMode = new UserSettings.DisplaySettings(Gdx.graphics.getDisplayMode());
        }

        bg_logo.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        bg_logo.setPosition(0, 0);

        // Triggers an event when the button is pressed

        difficultyBtns.forEach((difficulty, btn) -> btn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("{} difficulty button clicked", difficulty.toString());
                        boolean shouldLoad = canLoad && shouldLoadBtn.isChecked();
                        entity.getEvents().trigger(
                                "player_select", difficulty, shouldLoad);
                    }
                }
        ));

        howToPlayBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("How To Play button clicked");
                        entity.getEvents().trigger("how-to-play");
                    }
                });

        achievementsBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Achievements button clicked");
                        entity.getEvents().trigger("achievements");
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

        table.defaults().pad(BTN_SPACING);
        diffBtnsTable.defaults().pad(BTN_SPACING);
        table.add(startBtn).padTop(BTN_SPACING * 2);
        table.row();
        for (TextButton btn : difficultyBtns.values()) {
            diffBtnsTable.add(btn);
        }
        table.add(diffBtnsTable);
        table.row();
        if (canLoad) {
            table.add(shouldLoadBtn);
            table.row();
        }

        for (TextButton button : new TextButton[]{
                howToPlayBtn, achievementsBtn, settingsBtn, exitBtn}) {
            table.add(button);
            table.row();
        }

        stage.addActor(bg_logo);
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

    public void resize(int width, int height) {
        bg_logo.setSize(width, height);
    }
}
