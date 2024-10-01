package com.csse3200.game.components.screendisplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.actors.StatBar;
import com.csse3200.game.entities.PlayerSelection;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;

import static com.csse3200.game.screens.PlayerSelectScreen.BG_IMAGE;
import static com.csse3200.game.services.ServiceLocator.getResourceService;

/**
 * Creates the ui elements and functionality for the player select screen's ui.
 */
public class PlayerSelectDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(PlayerSelectDisplay.class);
    private static final float Z_INDEX = 2f;
    private final GdxGame game;
    private Table rootTable;
    private static final float ROOT_PADDING = 10f;
    private static final float STAT_TABLE_PADDING = 2f;
    private static final float BAR_HEIGHT = 20;
    private static final float PROPORTION = 0.9f;
    private static final float X_PADDING = 10f;

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

    /**
     * Populate the stage with player animations and buttons to select them.
     */
    private void addActors() {
        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().pad(ROOT_PADDING);

        Map<String, PlayerConfig> configs =
                PlayerSelection.getPlayerConfigs(Arrays.stream(PlayerSelection.PLAYERS).toList());

        configs.forEach((filename, config) -> {
            TextureRegion idleTexture = new TextureAtlas(config.textureAtlasFilename)
                    .findRegion("idle");
            Image playerImage = new Image(idleTexture);
            rootTable.add(playerImage);
        });

        // Add stat bars
        rootTable.row().fill().uniform();
        configs.forEach((filename, config) -> {
            Table statTable = new Table();
            statTable.defaults().pad(STAT_TABLE_PADDING);
            addStat(statTable, "HLTH", config.health, PlayerConfig.MAX_HEALTH);
            addStat(statTable, "SPD", config.speed.len(), PlayerConfig.MAX_SPEED.len());
            rootTable.add(statTable);
        });

        // Add buttons to choose each player
        rootTable.row().fillX();
        configs.forEach((filename, config) -> {
            TextButton button = new TextButton("%s".formatted(config.name), skin, "action");
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Button to select {} pressed", filename);
                    playerSelected(filename);
                }
            });
            button.getLabel().setWrap(true);
            rootTable.add(button);
        });

        Image bgImage = new Image(getResourceService().getAsset(BG_IMAGE, Texture.class));
        bgImage.setFillParent(true);

        stage.addActor(bgImage);
        stage.addActor(rootTable);
    }

    /**
     * Add a row in the stat table (has stat name and stat bar).
     * @param table The stat table to add the stat to.
     * @param name The name of the stat (short and capitalised).
     * @param value The value of this stat.
     * @param maxValue The max possible value of this stat.
     */
    private void addStat(Table table, String name, float value, float maxValue) {
        float proportion = value / maxValue;
        Label statName = new Label(name, skin);
        table.add(statName).pad(STAT_TABLE_PADDING);

        StatBar statBar = new StatBar(proportion);
        table.add(statBar).pad(STAT_TABLE_PADDING).expandX().fillX().height(BAR_HEIGHT);

        table.row();
    }

    private void playerSelected(String filename) {
        logger.info("Player chosen: {}", filename);
        game.gameOptions.chosenPlayer = filename;
        game.setScreen(ScreenType.CUTSCENE);
    }

    @Override
    public void dispose() {
        rootTable.clear();
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
