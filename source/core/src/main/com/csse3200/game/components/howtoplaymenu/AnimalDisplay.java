package com.csse3200.game.components.howtoplaymenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimalDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(AnimalDisplay.class);
    private final GdxGame game;

    private Table rootTable;

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
                "Rat - 100 Health, 10 Base Attack, 1 Attack Range, 2 Attack Rate",
                "Dog - 300 Health, 20 Base Attack, 1.5 Attack Range, 1 Attack Rate",
                "Minotaur - 400 Health, 35 Base Attack, 2 Attack Range, 0.5 Attack Rate",
                "Dino - 300 Health, 10 Base Attack, 1.2 Attack Range, 1 Attack Rate",
                "Bat - 50 Health, 10 Base Attack, 1 Attack Range, 1.2 Attack Rate",
                "Snake - 300 Health, 10 Base Attack, 1 Attack Range, 0.8 Attack Rate",
                "Bear - 600 Health, 35 Base Attack, 1.5 Attack Range, 0.5 Attack Rate",
                "Dragon - 200 Health, 15 Base Attack, 12 Max Attack Range, 1.5 Max Attack Rate",
                "Kitsune - 1000 Health, 40 Base Attack, 12 Max Attack Range, 0.8 Max Attack Rate",
                "Birdman - 1500 Health, 40 Base Attack, 2 Max Attack Range, 10 Max Attack Rate",
                "Cthulu - 1000 Health, 30 Base Attack, 3 Max Attack Range, 10 Max Attack Rate",
                "Werewolf - 1000 Health, 30 Base Attack, 3 Max Attack Range, 10 Max Attack Rate"
        };

        String[] animalImagePaths = {
                "images/how-to-play/animals/rat.png",
                "images/how-to-play/animals/dog.png",
                "images/how-to-play/animals/minotaur.png",
                "images/how-to-play/animals/dino.png",
                "images/how-to-play/animals/bat.png",
                "images/how-to-play/animals/snake.png",
                "images/how-to-play/animals/bear.png",
                "images/how-to-play/animals/dragon.png",
                "images/how-to-play/animals/kitsune.png",
                "images/how-to-play/animals/birdman.png",
                "images/how-to-play/animals/cthulu.png",
                "images/how-to-play/animals/Werewolf.png"
        };

        for (int i = 0; i < animalDescriptions.length; i++) {
            Image animalImage = new Image(new Texture(Gdx.files.internal(animalImagePaths[i])));
            Label animalLabel = new Label(animalDescriptions[i], skin);

            table.add(animalImage).size(75, 70).pad(10);
            table.add(animalLabel).pad(10).left().row();
        }

        return table;
    }

    private Table makeMenuBtns() {
        TextButton exitBtn = new TextButton("Back", skin);

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Back button clicked");
                        game.setScreen(GdxGame.ScreenType.HOW_TO_PLAY);
                    }
                });

        Table table = new Table();
        table.add(exitBtn).expandX().left().pad(0f, 15f, 15f, 0f);
        return table;
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