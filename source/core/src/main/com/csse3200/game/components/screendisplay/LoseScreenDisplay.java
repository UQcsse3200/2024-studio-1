package com.csse3200.game.components.screendisplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;

import static com.csse3200.game.GdxGame.ScreenType.MAIN_MENU;
import static com.csse3200.game.files.FileLoader.readClass;
import static com.csse3200.game.screens.LoseScreen.PLAYER_DEAD;

/**
 * Display and actions for the losing/death screen.
 */
public class LoseScreenDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(LoseScreenDisplay.class);
    private static final float Z_INDEX = 2f;
    private static final float Y_PADDING = 100f;
    private final GdxGame game;
    private Table table;
    private String LastAttackAnimal;

    private ArrayList<String> animals = new ArrayList<String>();
    private ArrayList<String> animalDeathScreens = new ArrayList<String>();


    String[] animalImagePaths = {
            "images/how-to-play/animals/rat.png",
            "images/how-to-play/animals/bear.png",
            "images/how-to-play/animals/bat.png",
            "images/how-to-play/animals/dog.png",
            "images/how-to-play/animals/snake.png",
            "images/how-to-play/animals/dino.png",
            "images/how-to-play/animals/minotaur.png"
    };

    /**
     * Make the component.
     *
     * @param game the overarching game, needed so that buttons can trigger navigation through
     *             screens
     */
    public LoseScreenDisplay(GdxGame game) {
        this.game = game;
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        animals.add("Rat");
        animals.add("Bear");
        animals.add("Bat");
        animals.add("Dog");
        animals.add("Snake");
        animals.add("Dino");
        animals.add("Minotaur");
        animalDeathScreens.add("You thought you were the predator, but turns out you are just prey!");
        animalDeathScreens.add("Too slow, too soft. You didn't stand a chance against my claws!");
        animalDeathScreens.add("Couldn't see in the dark, huh? My world, my rules. Better luck next time, human!");
        animalDeathScreens.add("Fetch? More like fight! Too bad you didn't put up much of one.");
        animalDeathScreens.add("Slither, slither... you never saw me coming, and now it is lights out for you!");
        animalDeathScreens.add("Extinct? Not me! Guess it is you who is heading for the history books!");
        animalDeathScreens.add("Lost in my labyrinth of power, and now you are just another trophy!");
        table = new Table();
        table.setFillParent(true);
        LastAttackAnimal = readClass(String.class, "configs/LastAttack.json", FileLoader.Location.EXTERNAL);
        Label youDied = new Label("You died...", skin, "cutscene");
        Label animalName;
        Image playerDead;

        if(animals.contains(LastAttackAnimal)){
            playerDead = new Image(new Texture(Gdx.files.internal(animalImagePaths[animals.indexOf(LastAttackAnimal)])));
            animalName = new Label(LastAttackAnimal+ ":", skin, "cutscene");
            table.add(playerDead).padTop(Y_PADDING);
            table.row();
            table.add(animalName).padTop(Y_PADDING);
            Label animalLabel = new Label(animalDeathScreens.get(animals.indexOf(LastAttackAnimal)), skin, "cutscene");
            table.row();
            table.add(animalLabel).padTop(30f);
        }
        else{
            // See assets/images/player/player_asset_citation.txt
            playerDead = new Image(
                    ServiceLocator.getResourceService().getAsset(PLAYER_DEAD, Texture.class)
            );
            animalName = new Label("", skin, "cutscene");
            table.add(playerDead).padTop(Y_PADDING);
            table.row();
            table.add(animalName).padTop(Y_PADDING);
            table.add(youDied).padTop(30f);
        }
        Button exitBtn = new TextButton("Back to menu", skin);
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Going back to menu from death screen");
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
