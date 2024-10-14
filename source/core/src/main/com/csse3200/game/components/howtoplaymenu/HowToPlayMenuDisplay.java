package com.csse3200.game.components.howtoplaymenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.GdxGame;
import com.csse3200.game.ui.UIComponent;
import com.csse3200.game.utils.StringDecorator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Display for the how to play screen e.g. labels for game description.
 */
public class HowToPlayMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(HowToPlayMenuDisplay.class);
    private final GdxGame game;

    private Table rootTable;

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
        rootTable.defaults().expandX().pad(5f);

        rootTable.add(title).expandX();

        rootTable.row().padTop(20f);
        rootTable.add(howToPlayTable).growX();

        rootTable.row();
        rootTable.add(menuBtns).fillX();

        stage.addActor(rootTable);
    }

    private Table makeHowToPlayTable() {
        Label instruction = new Label("User Guide: ", skin);

        String[] text = {
                "Beast Breakout is a top-down dungeon crawler game, presented using "
                        + "two-dimensional sprites, in which the player controls an unnamed "
                        + "character in a non-specific facility.",
                "On each floor of the facility, the player must fight enraged animals in a room "
                        + "before continuing onto the next room. This is most commonly done by "
                        + "the character's melee or ranged weapon in the style of a "
                        + "twin-stick shooter.",
                "Other methods of defeating enemies become possible as the character gains "
                        + "power-ups, items that are automatically worn by the player-character "
                        + "when picked up that can alter the character's core "
                        + "attributes, such as increasing health or the",
                "strength of their weapons, or cause additional side effects.",
                "When the player loses all of their health the game ends in permadeath and the "
                        + "player must start over from a freshly-generated dungeon. Each floor of "
                        + "the dungeon includes a boss which the player must defeat before "
                        + "continuing to the next level."
        };
        TextButton animalBtn = new TextButton("About Animals", skin);

        animalBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Animals button clicked");
                        animalMenu();
                    }
                });
        TextButton weaponBtn = new TextButton("About Weapons", skin);

        weaponBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Weapons button clicked");
                        weaponMenu();
                    }
                });

        // Position components on the table
        Table table = new Table();
        table.defaults().expandX().pad(10f).left();
        table.add(instruction);
        table.row().padTop(20f);

        for (String line : text) {
            Label label = new Label(line, skin);
            label.setWrap(true);
            table.add(label).left().fillX();
            table.row().pad(10f);
        }
        table.add(animalBtn);
        table.row().padTop(10f);
        table.add(weaponBtn);

        return table;
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

    private void exitMenu() {
        game.setScreen(GdxGame.ScreenType.MAIN_MENU);
    }
    private void animalMenu() {
        game.setScreen(GdxGame.ScreenType.ANIMALS);
    }
    private void weaponMenu() {
        game.setScreen(GdxGame.ScreenType.WEAPONS);
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

