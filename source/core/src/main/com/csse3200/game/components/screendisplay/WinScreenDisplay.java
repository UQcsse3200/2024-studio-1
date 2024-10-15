package com.csse3200.game.components.screendisplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.WinScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.csse3200.game.GdxGame.ScreenType.MAIN_MENU;

/**
 * Display and actions for the win screen (happy player image, you win label, button that goes
 * back to main menu).
 */
public class WinScreenDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(WinScreenDisplay.class);
    private static final float Z_INDEX = 2f;
    private static final float Y_PADDING = 10f;
    private final GdxGame game;
    private Table table;
    private final String backgroundImagePath;
    private Image backgroundImage;

    /**
     * Make the component.
     *
     * @param game the overarching game, needed so that buttons can trigger navigation through
     *             screens
     */
    public WinScreenDisplay(GdxGame game , String backgroundImagePath) {
        this.game = game;
        this.backgroundImagePath = backgroundImagePath;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        // Set up background image
        Texture backgroundTexture = ServiceLocator.getResourceService().getAsset(backgroundImagePath, Texture.class);
        backgroundImage = new Image(new TextureRegionDrawable(backgroundTexture));
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backgroundImage);

        Label youWin = new Label("You win!", skin);
        table.row();
        table.add(youWin).padTop(Y_PADDING);

        Button exitBtn = new TextButton("Back to menu", skin);
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Going back to menu from game win screen");
                game.setScreen(MAIN_MENU);
            }
        });
        table.row();
        table.add(exitBtn).padTop(Y_PADDING);

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
        backgroundImage.remove();
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw handled by stage
    }
}