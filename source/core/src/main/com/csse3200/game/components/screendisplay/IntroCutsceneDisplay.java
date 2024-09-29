package com.csse3200.game.components.screendisplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class IntroCutsceneDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(IntroCutsceneDisplay.class);
    private static final int Y_PADDING_BUTTON = 40;
    private static final int X_PADDING_BUTTON = 10;

    private final GdxGame game;
    private Table table;
    private int currentPage = 0;

    private Texture[] cutsceneTextures;
    private Image backgroundImage;

    public IntroCutsceneDisplay(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        loadTextures();

        // Create a background image that will fill the screen
        backgroundImage = new Image(cutsceneTextures[currentPage]);
        backgroundImage.setFillParent(true); // Make it fill the screen
        stage.addActor(backgroundImage); // Add it to the stage first, behind everything else

        // Create a table for buttons
        table = new Table();
        table.setFillParent(true);
        table.top().center();  // Align content to the top
        stage.addActor(table);  // Add the table above the background

        // Create a listener for key presses (like ESC)
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    skipToLastPage();
                    return true;
                }
                return false;
            }
        });

        updateCutscene(); // Start with the first page
    }

    private void loadTextures() {
        cutsceneTextures = new Texture[4]; // Adjusted size based on the number of cutscenes
        cutsceneTextures[0] = new Texture(Gdx.files.internal("images/screen/cutscene1.jpg"));
        cutsceneTextures[1] = new Texture(Gdx.files.internal("images/screen/cutscene2.jpg"));
        cutsceneTextures[2] = new Texture(Gdx.files.internal("images/screen/cutscene3.jpg"));
        cutsceneTextures[3] = new Texture(Gdx.files.internal("images/screen/cutscene4.jpg"));
    }

    private void updateCutscene() {
        // Clear previous elements in the table
        table.clear();

        // Update the background image for the current page
        backgroundImage.setDrawable(new TextureRegionDrawable(new TextureRegion(cutsceneTextures[currentPage])));

        // Add buttons below the image
        addActors();
    }

    private void addActors() {
        // Add buttons container
        Table buttonTable = new Table(); // Use a new table for button layout
        buttonTable.setFillParent(true); // Make sure the table takes the full screen
        buttonTable.bottom().padBottom(Y_PADDING_BUTTON); // Position the buttons at the bottom with padding

        // Add "Skip to Game" button if not on the last page
        if (currentPage < cutsceneTextures.length - 1) {
            Button skipBtn = new TextButton("Skip", skin, "action");
            skipBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logger.debug("Skip button pressed");
                    onSkip();
                }
            });
            buttonTable.add(skipBtn).padRight(X_PADDING_BUTTON); // Add skip button with padding to the right
        }

        // Add next button
        addNextButton(buttonTable); // Add the next button into buttonTable

        // Add button table to the stage
        stage.addActor(buttonTable);
    }

    private void addNextButton(Table buttonTable) {
        Button nextBtn = new TextButton(currentPage == cutsceneTextures.length - 1 ? "Start Game" : "Next", skin, "action");
        nextBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (currentPage < cutsceneTextures.length - 1) {
                    currentPage++;
                    updateCutscene(); // Go to the next page
                } else {
                    onStart(); // Start the game
                }
            }
        });

        buttonTable.add(nextBtn).padLeft(X_PADDING_BUTTON); // Add next button with padding to the left
    }

    private void onSkip() {
        logger.info("Skip to last page");
        currentPage = cutsceneTextures.length - 1; // Skip to the last page
        updateCutscene(); // Update the cutscene
    }

    private void skipToLastPage() {
        currentPage = cutsceneTextures.length - 1;
        updateCutscene(); // Update the cutscene to the last page
    }

    private void onStart() {
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    @Override
    public void dispose() {
        for (Texture texture : cutsceneTextures) {
            texture.dispose();
        }
        table.clear();
        super.dispose();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // Drawing handled by stage
    }
}