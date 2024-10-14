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

/**
 * Creates the ui elements and functionality for the intro cutscene's ui.
 */
public class IntroCutsceneDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(IntroCutsceneDisplay.class);
    private static final int Y_PADDING_BUTTON = 40;
    private static final int X_PADDING_BUTTON = 20;
    private static final String ACTION_STYLE = "action";

    private final GdxGame game;
    private Table table;
    private int currentPage = 0;

    private Texture[] cutsceneTextures;
    private Image backgroundImage;

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

        logger.debug("Creating cutscene");
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

        logger.debug("Loading cutscene textures");

        // Define the texture file paths in a fixed array
        String[] texturePaths = {
                "images/screen/cutscene1.jpg",
                "images/screen/cutscene2.jpg",
                "images/screen/cutscene3.jpg",
                "images/screen/cutscene4.jpg"
        };

        // Initialize the array of textures
        cutsceneTextures = new Texture[texturePaths.length];

        // Load each texture in a loop using the predefined paths
        for (int i = 0; i < texturePaths.length; i++) {
            cutsceneTextures[i] = new Texture(Gdx.files.internal(texturePaths[i]));
        }
    }

    private void updateCutscene() {
        // Clear all actors from the stage
        stage.clear();

        // Update the background image for the current page
        backgroundImage.setDrawable(new TextureRegionDrawable(new TextureRegion(cutsceneTextures[currentPage])));
        stage.addActor(backgroundImage); // Re-add the background image to the stage

        // Add buttons below the image
        addActors();
    }

    private void addActors() {
        // Clear previous buttons from the table
        table.clear(); // Clear any existing buttons in the main table

        // Check if we're on the last page
        if (currentPage == cutsceneTextures.length - 1) {
            // On the last page, add the "Start Game" button
            // Create a new button table for the start button
            Table buttonTable = new Table();
            buttonTable.setFillParent(true);
            buttonTable.bottom().padBottom(Y_PADDING_BUTTON); // Position the buttons at the bottom with padding

            Button startGameBtn = new TextButton("Start Game", skin, ACTION_STYLE);
            startGameBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    onStart(); // Start the game when the button is clicked
                }
            });

            buttonTable.add(startGameBtn); // Add the start button
            stage.addActor(buttonTable); // Add the button table to the stage
            return; // Exit the method
        }

        // For all other pages, add buttons
        Table buttonTable = new Table(); // Create a new table for buttons
        buttonTable.setFillParent(true); // Make sure the table takes the full screen
        buttonTable.bottom().padBottom(Y_PADDING_BUTTON); // Position the buttons at the bottom with padding

        // Show the "Skip" button if not on the last page
        Button skipBtn = new TextButton("Skip", skin, ACTION_STYLE);
        skipBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                skipToLastPage(); // Skip to the last page
            }
        });
        buttonTable.add(skipBtn).padRight(X_PADDING_BUTTON); // Add skip button with padding to the right

        // Show the "Next" button if not on the last page
        addNextButton(buttonTable); // Add the next button into buttonTable

        // Add button table to the stage
        stage.addActor(buttonTable);
    }

    private void addNextButton(Table buttonTable) {
        Button nextBtn = new TextButton(
                currentPage == cutsceneTextures.length - 1 ? "Start Game" : "Next",
                skin, ACTION_STYLE);
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

        buttonTable.add(nextBtn).padLeft(X_PADDING_BUTTON); // Add next button with padding to the
        // left
    }

    private void skipToLastPage() {
        currentPage = cutsceneTextures.length - 1;
        updateCutscene(); // Update the cutscene to the last page
    }

    private void onStart() {
        logger.debug("Finished cutscene, going to main game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    @Override
    public void dispose() {
        logger.debug("Disposing cutscene textures");
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