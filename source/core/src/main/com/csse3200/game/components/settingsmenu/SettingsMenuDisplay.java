package com.csse3200.game.components.settingsmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Graphics.Monitor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
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
    private final GdxGame game;

    private Table rootTable;
    private TextField fpsText;
    private CheckBox fullScreenCheck;
    private CheckBox vsyncCheck;
    private Slider uiScaleSlider;
    private Slider musicVolumeSlider;
    private Slider soundVolumeSlider;
    private CheckBox muteCheck;
    private CheckBox WASDCheckbox;
    private CheckBox ArrowsCheckbox;
    private CheckBox cutsceneCheck;
    private SelectBox<StringDecorator<DisplayMode>> displayModeSelect;
    private SelectBox<String> actionSelect;
    private SelectBox<String> keySelect;

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

        Label uiScaleLabel = new Label("UI Scale (Unused):", skin);
        uiScaleSlider = new Slider(0.2f, 2f, 0.1f, false, skin);
        uiScaleSlider.setValue(settings.uiScale);
        Label uiScaleValue = new Label(String.format("%.2fx", settings.uiScale), skin);

        Label musicVolumeLabel = new Label("Music Volume:", skin);
        musicVolumeSlider = new Slider(0f, 1f, 0.05f, false, skin);
        musicVolumeSlider.setValue(settings.musicVolume);
        Label musicVolumeValue = new Label(String.format("%.0f%%", settings.musicVolume * 100), skin);

        Label soundVolumeLabel = new Label("Sound Volume:", skin);
        soundVolumeSlider = new Slider(0f, 1f, 0.05f, false, skin);
        soundVolumeSlider.setValue(settings.soundVolume);
        Label soundVolumeValue = new Label(String.format("%.0f%%", settings.soundVolume * 100), skin);


        Label muteLabel = new Label("Mute:", skin);
        muteCheck = new CheckBox("", skin);
        muteCheck.setChecked(settings.mute);

        Label cutsceneLabel = new Label("Enable Cutscenes:", skin);
        cutsceneCheck = new CheckBox("", skin);
        cutsceneCheck.setChecked(settings.enableCutscene);

        Label displayModeLabel = new Label("Resolution:", skin);
        displayModeSelect = new SelectBox<>(skin);
        Monitor selectedMonitor = Gdx.graphics.getMonitor();
        displayModeSelect.setItems(getDisplayModes(selectedMonitor));
        displayModeSelect.setSelected(getActiveMode(displayModeSelect.getItems()));

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
        table.add(cutsceneLabel).right().padRight(15f);
        table.add(cutsceneCheck).left();

        table.row().padTop(10f);
        Table uiScaleTable = new Table();
        uiScaleTable.add(uiScaleSlider).width(100).left();
        uiScaleTable.add(uiScaleValue).left().padLeft(5f).expandX();

        table.add(uiScaleLabel).right().padRight(15f);
        table.add(uiScaleTable).left();

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

        Label walkLabel = new Label("Key Control: ", skin);
        WASDCheckbox = new CheckBox("move with WASD and shoot with arrows", skin);
        ArrowsCheckbox=new  CheckBox("shoot with WASD and move with arrows", skin);

        ButtonGroup<CheckBox> walkButtonGroup = new ButtonGroup<>(WASDCheckbox,ArrowsCheckbox);

        if (settings.controlsWithWASD) {
            walkButtonGroup.setChecked("move with WASD and shoot with arrows");  
        } else {
            walkButtonGroup.setChecked("shoot with WASD and move with arrows");  
        }
        table.row();
        table.add(walkLabel).pad(10);
        table.add(WASDCheckbox).pad(10);
        table.add(ArrowsCheckbox).pad(10);

        // Listener for uiScaleSlider
        uiScaleSlider.addListener(
                (Event event) -> {
                    float value = uiScaleSlider.getValue();
                    uiScaleValue.setText(String.format("%.2fx", value));
                    return true;
                });
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
                if ((Boolean) object) {
                    // If "Okay" is pressed, apply changes and go to the main menu
                    logger.debug("Confirmed changes");
                    UserSettings.Settings settings = UserSettings.get();

                    Integer fpsVal = parseOrNull(fpsText.getText());
                    if (fpsVal != null) {
                        settings.fps = fpsVal;
                    }
                    settings.fullscreen = fullScreenCheck.isChecked();
                    settings.mute = muteCheck.isChecked();
                    settings.uiScale = uiScaleSlider.getValue();
                    settings.displayMode = new DisplaySettings(displayModeSelect.getSelected().object);
                    settings.vsync = vsyncCheck.isChecked();
                    settings.musicVolume = musicVolumeSlider.getValue();
                    settings.enableCutscene = cutsceneCheck.isChecked();
                    settings.soundVolume = soundVolumeSlider.getValue();
                    settings.controlsWithWASD = WASDCheckbox.isChecked();

                    UserSettings.set(settings, true);
                    game.setScreen(ScreenType.MAIN_MENU); // Redirect to main menu
                } else {
                    // If "Cancel" is pressed, stay on the same screen
                    logger.debug("Cancelled changes");
                }
            }
        };

    

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
