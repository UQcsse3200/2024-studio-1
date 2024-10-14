package com.csse3200.game.components.settingsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.GdxGame.ScreenType;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.files.UserSettings.DisplaySettings;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.utils.StringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Settings menu display and logic. If you bork the settings, they can be changed manually in
 * CSSE3200Game/settings.json under your home directory (This is C:/users/[username] on Windows).
 */
public class SettingsMenuDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(SettingsMenuDisplay.class);
    private static final String PERCENT_FORMAT = "%.0f%%";
    private final GdxGame game;

    private Table rootTable;
    private TextField fpsText;
    private CheckBox fullScreenCheck;
    private CheckBox vsyncCheck;
    private Slider musicVolumeSlider;
    private Slider soundVolumeSlider;
    private CheckBox muteCheck;
    private SelectBox<StringDecorator<DisplayMode>> displayModeSelect;

    public SettingsMenuDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        Label title = new Label("Settings", skin, "title");
        Table settingsTable = makeSettingsTable();
        Table menuBtns = makeMenuBtns();

        rootTable = new Table();
        rootTable.setFillParent(true);

        rootTable.add(title).expandX().top().padTop(20f);

        rootTable.row().padTop(30f);
        rootTable.add(settingsTable).expandX().expandY();

        rootTable.row();
        rootTable.add(menuBtns).fillX();

        stage.addActor(rootTable);
    }

    private Table makeSettingsTable() {
        // Get current values
        UserSettings.Settings settings = UserSettings.get();

        // Create components
        Label fpsLabel = new Label("FPS Cap:", skin);
        fpsText = new TextField(Integer.toString(settings.fps), skin);

        Label fullScreenLabel = new Label("Fullscreen:", skin);
        fullScreenCheck = new CheckBox("", skin);
        fullScreenCheck.setChecked(settings.fullscreen);

        Label vsyncLabel = new Label("VSync:", skin);
        vsyncCheck = new CheckBox("", skin);
        vsyncCheck.setChecked(settings.vsync);

        Label musicVolumeLabel = new Label("Music Volume:", skin);
        musicVolumeSlider = new Slider(0f, 1f, 0.05f, false, skin);
        musicVolumeSlider.setValue(settings.musicVolume);
        Label musicVolumeValue = new Label(String.format(PERCENT_FORMAT, settings.musicVolume * 100), skin);

        Label soundVolumeLabel = new Label("Sound Volume:", skin);
        soundVolumeSlider = new Slider(0f, 1f, 0.05f, false, skin);
        soundVolumeSlider.setValue(settings.soundVolume);
        Label soundVolumeValue = new Label(String.format(PERCENT_FORMAT, settings.soundVolume * 100), skin);


        Label muteLabel = new Label("Mute:", skin);
        muteCheck = new CheckBox("", skin);
        muteCheck.setChecked(settings.mute);

        Label displayModeLabel = new Label("Resolution:", skin);
        displayModeSelect = new SelectBox<>(skin);
        Monitor selectedMonitor = Gdx.graphics.getMonitor();
        displayModeSelect.setItems(getDisplayModes(selectedMonitor));
        displayModeSelect.setSelected(getActiveMode(displayModeSelect.getItems()));

        Label actionLabel = new Label("Action: ", skin);
        SelectBox<String> actionSelect = new SelectBox<>(skin);
        actionSelect.setItems(actions);
        Label keyLabel = new Label("Key: ", skin);
        SelectBox<String> keySelect = new SelectBox<>(skin);
        keySelect.setItems(keys);

        // Position Components on table
        Table table = new Table();

        table.add(fpsLabel).right().padRight(15f);
        table.add(fpsText).width(100).left();

        table.row().padTop(10f);
        table.add(fullScreenLabel).right().padRight(15f);
        table.add(fullScreenCheck).left();

        table.row().padTop(10f);
        table.add(vsyncLabel).right().padRight(15f);
        table.add(vsyncCheck).left();

        table.row().padTop(10f);
        table.add(muteLabel).right().padRight(15f);
        table.add(muteCheck).left();

        table.row().padTop(10f);
        table.add(displayModeLabel).right().padRight(15f);
        table.add(displayModeSelect).left();

        table.row().padTop(10f);
        Table musicVolumeTable = new Table();
        musicVolumeTable.add(musicVolumeSlider).width(100).left();
        musicVolumeTable.add(musicVolumeValue).left().padLeft(5f).expandX();

        table.add(musicVolumeLabel).right().padRight(15f);
        table.add(musicVolumeTable).left();

        table.row().padTop(10f);
        Table soundVolumeTable = new Table();
        soundVolumeTable.add(soundVolumeSlider).width(100).left();
        soundVolumeTable.add(soundVolumeValue).left().padLeft(5f).expandX();

        table.add(soundVolumeLabel).right().padRight(15f);
        table.add(soundVolumeTable).left();

        table.row().padTop(10f);
        table.add(actionLabel).right().padRight(15f);
        table.add(actionSelect).left();
        table.add(keyLabel).right().padRight(15f);
        table.add(keySelect).left();

        musicVolumeSlider.addListener(
                (Event event) -> {
                    float value = musicVolumeSlider.getValue();
                    musicVolumeValue.setText(String.format("%.0f%%", value * 100));
                    return true;
                });

        soundVolumeSlider.addListener(
                (Event event) -> {
                    float value = soundVolumeSlider.getValue();
                    soundVolumeValue.setText(String.format("%.0f%%", value * 100));
                    return true;
                });
        return table;
    }

    private StringDecorator<DisplayMode> getActiveMode(Array<StringDecorator<DisplayMode>> modes) {
        DisplayMode active = Gdx.graphics.getDisplayMode();

        for (StringDecorator<DisplayMode> stringMode : modes) {
            DisplayMode mode = stringMode.object;
            if (active.width == mode.width
                    && active.height == mode.height
                    && active.refreshRate == mode.refreshRate) {
                return stringMode;
            }
        }
        return null;
    }

    private Array<StringDecorator<DisplayMode>> getDisplayModes(Monitor monitor) {
        DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(monitor);
        Array<StringDecorator<DisplayMode>> arr = new Array<>();

        for (DisplayMode displayMode : displayModes) {
            arr.add(new StringDecorator<>(displayMode, this::prettyPrint));
        }

        return arr;
    }

    private String prettyPrint(DisplayMode displayMode) {
        return displayMode.width + "x" + displayMode.height + ", " + displayMode.refreshRate + "hz";
    }

    private Table makeMenuBtns() {
        TextButton exitBtn = new TextButton("Exit", skin);
        TextButton applyBtn = new TextButton("Apply", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        exitMenu();
                    }
                });

        applyBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Apply button clicked");
                        applyChanges();
                    }
                });

        Table table = new Table();
        table.add(exitBtn).expandX().left().pad(0f, 15f, 15f, 0f);
        table.add(applyBtn).expandX().right().pad(0f, 0f, 15f, 15f);
        return table;
    }

    private void applyChanges() {
        // Create a confirmation dialog
        Dialog confirmationDialog = new Dialog("Confirm Changes", skin) {
            @Override
            protected void result(Object object) {
                if (Boolean.TRUE.equals(object)) {
                    // If "Okay" is pressed, apply changes and go to the main menu
                    logger.debug("Confirmed changes");
                    UserSettings.Settings settings = UserSettings.get();

                    Integer fpsVal = parseOrNull(fpsText.getText());
                    if (fpsVal != null) {
                        settings.fps = fpsVal;
                    }
                    settings.fullscreen = fullScreenCheck.isChecked();
                    settings.mute = muteCheck.isChecked();
                    settings.displayMode = new DisplaySettings(displayModeSelect.getSelected().object);
                    settings.vsync = vsyncCheck.isChecked();
                    settings.musicVolume = musicVolumeSlider.getValue();
                    settings.soundVolume = soundVolumeSlider.getValue();

                    UserSettings.set(settings, true);
                    game.setScreen(ScreenType.MAIN_MENU); // Redirect to main menu
                } else {
                    // If "Cancel" is pressed, stay on the same screen
                    logger.debug("Cancelled changes");
                }
            }
        };

        // Add dialog text and buttons
        confirmationDialog.text("Are you sure you want to apply these changes?");
        confirmationDialog.button("Cancel", false); // "Cancel" button dismisses the dialog
        confirmationDialog.button("Okay", true);    // "Okay" button applies the changes

        // Create a solid color background using Pixmap
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GRAY);  // Set the desired background color
        pixmap.fill();

        Texture texture = new Texture(pixmap);
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));

        // Set the background to the dialog
        confirmationDialog.setBackground(drawable);

        // Set the size of the dialog (you can adjust these values as needed)
        confirmationDialog.setSize(600, 400); // Increase dialog size

        // Optional: Add padding around the content
        confirmationDialog.padRight(40); // Adjust padding as needed
        confirmationDialog.padLeft(40); // Adjust padding as needed
        confirmationDialog.padTop(40); // Adjust padding as needed
        confirmationDialog.padBottom(25); // Adjust padding as needed

        // Dispose of pixmap to free resources
        pixmap.dispose();

        // Show the dialog
        confirmationDialog.show(stage);
    }



    private void exitMenu() {
        game.setScreen(ScreenType.MAIN_MENU);
    }

    private Integer parseOrNull(String num) {
        try {
            return Integer.parseInt(num, 10);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        rootTable.clear();
        super.dispose();
    }
}