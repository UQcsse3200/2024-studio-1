package com.csse3200.game.components.screendisplay;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.entities.PlayerSelection;
import com.csse3200.game.entities.configs.PlayerConfig;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Creates the ui elements and functionality for the player select screen's ui.
 */
public class PlayerSelectDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(PlayerSelectDisplay.class);
    private static final float Z_INDEX = 2f;
    private final GdxGame game;
    private Table table;
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
        table = new Table();
        table.setFillParent(true);

        Map<String, PlayerConfig> configs =
                PlayerSelection.getPlayerConfigs(List.of(PlayerSelection.PLAYERS));

        configs.forEach((filename, config) -> {
            String textureAtlasFilename = config.textureAtlasFilename;

            // Create new AnimationRenderComponent to control animations for each player
            AnimationRenderComponent animator =
                    new AnimationRenderComponent(new TextureAtlas(textureAtlasFilename));

            PlayerSelectAnimation animatedImage = new PlayerSelectAnimation(animator, config.textureAtlasFilename);
            animatedImage.startAnimation();

            table.add(animatedImage).padLeft(X_PADDING).padRight(X_PADDING);
        });

        // Add buttons to choose each player
        table.row();
        configs.forEach((filename, config) -> {
            TextButton button = new TextButton("%s".formatted(config.name), skin, "action");
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Button to select {} pressed", filename);
                    playerSelected(filename);
                }
            });
            table.add(button).padLeft(X_PADDING).padRight(X_PADDING);
        });

        stage.addActor(table);
    }

    private void playerSelected(String filename) {
        logger.info("Player chosen: {}", filename);
        game.gameOptions.chosenPlayer = filename;
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
