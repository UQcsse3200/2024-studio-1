package com.csse3200.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.areas.GameController;
import com.csse3200.game.entities.configs.MapLoadConfig;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.options.GameOptions.Difficulty;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;

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

    private Image backgroundLogo;

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
        MapLoadConfig mapLoadConfig = FileLoader.readClass(MapLoadConfig.class,
                    GameController.MAP_SAVE_PATH, FileLoader.Location.EXTERNAL);
        PlayerConfig playerConfig = FileLoader.readClass(PlayerConfig.class,
                "saves/player_save.json", FileLoader.Location.EXTERNAL);

        logger.info("{}\n{} are read", playerConfig, mapLoadConfig);
        return  mapLoadConfig != null && playerConfig != null;
    }

    private void addActors() {
        UserSettings.Settings settings = UserSettings.get();

        table = new Table();
        table.setFillParent(true);
        backgroundLogo = new Image(
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
        TextButton loadButton = new TextButton("Load", skin);
        boolean canLoad = loadFilesExist();


        if (settings.displayMode == null) {
            settings.displayMode = new UserSettings.DisplaySettings(Gdx.graphics.getDisplayMode());
        }

        backgroundLogo.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundLogo.setPosition(0, 0);

        // Triggers an event when the button is pressed

        difficultyBtns.forEach((difficulty, btn) -> btn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("{} difficulty button clicked", difficulty);
                        entity.getEvents().trigger(
                                "player_select", difficulty);
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
        loadButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Load Button Pressed");
                        entity.getEvents().trigger("load-game");
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

        if(canLoad) {
            table.add(loadButton);
            table.row();
        }
        for (TextButton button : new TextButton[]{
                howToPlayBtn, achievementsBtn, settingsBtn, exitBtn}) {
            table.add(button);
            table.row();
        }
        stage.addActor(backgroundLogo);
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
        backgroundLogo.setSize(width, height);
    }
}
