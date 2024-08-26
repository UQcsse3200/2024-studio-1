package com.csse3200.game.components.howtoplaymenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.settingsmenu.SettingsMenuDisplay;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.options.GameOptions;
import com.csse3200.game.options.GameOptions.Difficulty;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.utils.StringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HowToPlayMenuDisplay extends UIComponent{
    private static final Logger logger = LoggerFactory.getLogger(HowToPlayMenuDisplay.class);
    private final GdxGame game;

    private Table rootTable;
    private TextField fpsText;
    private CheckBox fullScreenCheck;
    private CheckBox vsyncCheck;
    private Slider uiScaleSlider;
    private SelectBox<StringDecorator<Graphics.DisplayMode>> displayModeSelect;

    public HowToPlayMenuDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        Label title = new Label("How To Play", skin, "title");
        Table howToPlayTable = makeHowToPlayTable();
        Table menuBtns = makeMenuBtns();

        rootTable = new Table();
        rootTable.setFillParent(true);

        rootTable.add(title).expandX().top().padTop(20f);

        rootTable.row().padTop(30f);
        rootTable.add(howToPlayTable).expandX().expandY();

        rootTable.row();
        rootTable.add(menuBtns).fillX();

        stage.addActor(rootTable);
    }

    private Table makeHowToPlayTable() {
        Label instruction = new Label("Instructions: ", skin);
        Label gameDescription1 = new Label(
                "Beast Breakout is a top-down dungeon crawler game, presented using two-dimensional sprites, in which the player controls",
                skin
        );
        Label gameDescription2 = new Label(
                "an unnamed character in a non-specific facility.",
                skin
        );
        Label gameDescription3 = new Label(
                "On each floor of the facility, the player must fight enraged animals in a room before continuing onto the next room. This is",
                skin
        );
        Label gameDescription4 = new Label(
                "most commonly done by the character's melee or ranged weapon in the style of a twin-stick shooter.",
                skin
        );
        Label gameDescription5 = new Label(
                "Other methods of defeating enemies become possible as the character gains power-ups, items that are automatically worn",
                skin
        );
        Label gameDescription6 = new Label(
                "by the player-character when picked up that can alter the character's core attributes, such as increasing health or the",
                skin
        );
        Label gameDescription7 = new Label(
                "strength of their weapons, or cause additional side effects.",
                skin
        );
        Label gameDescription8 = new Label(
                "When the player loses all of their health the game ends in permadeath and the player must start over from a freshly-",
                skin
        );
        Label gameDescription9 = new Label(
                "generated dungeon. Each floor of the dungeon includes a boss which the player must defeat before continuing to the next level.",
                skin
        );

        // Position components on the table
        Table table = new Table();
        table.add(instruction).left().padRight(10f);
        table.row().padTop(40f);
        table.add(gameDescription1).left().expandX();
        table.row().padTop(10f);
        table.add(gameDescription2).left().expandX();
        table.row().padTop(40f);
        table.add(gameDescription3).left().expandX();
        table.row().padTop(10f);
        table.add(gameDescription4).left().expandX();
        table.row().padTop(40f);
        table.add(gameDescription5).left().expandX();
        table.row().padTop(10f);
        table.add(gameDescription6).left().expandX();
        table.row().padTop(10f);
        table.add(gameDescription7).left().expandX();
        table.row().padTop(40f);
        table.add(gameDescription8).left().expandX();
        table.row().padTop(10f);
        table.add(gameDescription9).left().expandX();
        table.row().padTop(10f);


        table.row().padTop(10f);

        return table;
    }

    private StringDecorator<Graphics.DisplayMode> getActiveMode(Array<StringDecorator<Graphics.DisplayMode>> modes) {
        Graphics.DisplayMode active = Gdx.graphics.getDisplayMode();

        for (StringDecorator<Graphics.DisplayMode> stringMode : modes) {
            Graphics.DisplayMode mode = stringMode.object;
            if (active.width == mode.width
                    && active.height == mode.height
                    && active.refreshRate == mode.refreshRate) {
                return stringMode;
            }
        }
        return null;
    }

    private Array<StringDecorator<Graphics.DisplayMode>> getDisplayModes(Graphics.Monitor monitor) {
        Graphics.DisplayMode[] displayModes = Gdx.graphics.getDisplayModes(monitor);
        Array<StringDecorator<Graphics.DisplayMode>> arr = new Array<>();

        for (Graphics.DisplayMode displayMode : displayModes) {
            arr.add(new StringDecorator<>(displayMode, this::prettyPrint));
        }

        return arr;
    }

    private String prettyPrint(Graphics.DisplayMode displayMode) {
        return displayMode.width + "x" + displayMode.height + ", " + displayMode.refreshRate + "hz";
    }

    private Table makeMenuBtns() {
        TextButton exitBtn = new TextButton("Exit", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Exit button clicked");
                        exitMenu();
                    }
                });

        Table table = new Table();
        table.add(exitBtn).expandX().left().pad(0f, 15f, 15f, 0f);
        return table;
    }

    private void applyChanges() {
        UserSettings.Settings settings = UserSettings.get();

        Integer fpsVal = parseOrNull(fpsText.getText());
        if (fpsVal != null) {
            settings.fps = fpsVal;
        }
        settings.fullscreen = fullScreenCheck.isChecked();
        settings.uiScale = uiScaleSlider.getValue();
        settings.displayMode = new UserSettings.DisplaySettings(displayModeSelect.getSelected().object);
        settings.vsync = vsyncCheck.isChecked();

        UserSettings.set(settings, true);
    }

    private void exitMenu() {
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
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
    public void update() {
        stage.act(ServiceLocator.getTimeSource().getDeltaTime());
    }

    @Override
    public void dispose() {
        rootTable.clear();
        super.dispose();
    }
}

