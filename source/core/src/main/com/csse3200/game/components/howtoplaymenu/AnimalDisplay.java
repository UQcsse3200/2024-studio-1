package com.csse3200.game.components.howtoplaymenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.files.UserSettings;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.utils.StringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimalDisplay extends UIComponent{
    private static final Logger logger = LoggerFactory.getLogger(AnimalDisplay.class);
    private final GdxGame game;

    private Table rootTable;
    private TextField fpsText;
    private CheckBox fullScreenCheck;
    private CheckBox vsyncCheck;
    private Slider uiScaleSlider;
    private SelectBox<StringDecorator<Graphics.DisplayMode>> displayModeSelect;

    public AnimalDisplay(GdxGame game) {
        super();
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        Label title = new Label("Animals", skin, "title");
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
        Table table = new Table();

        String[] animalDescriptions = {
                "A small, cunning rodent.",
                "A loyal companion.",
                "A mythical creature with the body of a man and the head of a bull.",
                "A prehistoric giant.",
                "A nocturnal flying mammal.",
                "A slithering reptile.",
                "Another slithering reptile."
        };

        String[] animalImagePaths = {
                "images/how-to-play/animals/rat.png",
                "images/how-to-play/animals/dog.png",
                "images/how-to-play/animals/minotaur.png",
                "images/how-to-play/animals/dino.png",
                "images/how-to-play/animals/bat.png",
                "images/how-to-play/animals/snake.png",
                "images/how-to-play/animals/bear.png"
        };

        for (int i = 0; i < animalDescriptions.length; i++) {
            Image animalImage = new Image(new Texture(Gdx.files.internal(animalImagePaths[i])));
            Label animalLabel = new Label(animalDescriptions[i], skin);

            table.add(animalImage).size(75, 70).pad(10);
            table.add(animalLabel).pad(10).left().row();
        }

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
        TextButton exitBtn = new TextButton("Back", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Back button clicked");
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
        game.setScreen(GdxGame.ScreenType.HOW_TO_PLAY);
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
