package com.csse3200.game.components.screendisplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.scorescreen.ScoreListComponent;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private ScoreListComponent scoreList;

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
        scoreList = entity.getComponent(ScoreListComponent.class);
        addActors();
    }

    private void addActors() {


        table = new Table();
        table.setFillParent(true);
        table.center();  // Center the table in the stage
        LastAttackAnimal = readClass(String.class, "configs/LastAttack.json", FileLoader.Location.EXTERNAL);

        Label youDied = new Label("You died...", skin, "cutscene");
        Label animalName;
        Image playerDead;

// Setup the playerDead image and animalName based on LastAttackAnimal
        if (LastAttackAnimal.equals("Rat")) {
            playerDead = new Image(new Texture(Gdx.files.internal(animalImagePaths[0])));
            animalName = new Label("Rat:", skin, "cutscene");
        } else if (LastAttackAnimal.equals("Bear")) {
            playerDead = new Image(new Texture(Gdx.files.internal(animalImagePaths[1])));
            animalName = new Label("Bear:", skin, "cutscene");
        } else if (LastAttackAnimal.equals("Bat")) {
            playerDead = new Image(new Texture(Gdx.files.internal(animalImagePaths[2])));
            animalName = new Label("Bat:", skin, "cutscene");
        } else if (LastAttackAnimal.equals("Dog")) {
            playerDead = new Image(new Texture(Gdx.files.internal(animalImagePaths[3])));
            animalName = new Label("Dog:", skin, "cutscene");
        } else if (LastAttackAnimal.equals("Snake")) {
            playerDead = new Image(new Texture(Gdx.files.internal(animalImagePaths[4])));
            animalName = new Label("Snake:", skin, "cutscene");
        } else if (LastAttackAnimal.equals("Dino")) {
            playerDead = new Image(new Texture(Gdx.files.internal(animalImagePaths[5])));
            animalName = new Label("Dino:", skin, "cutscene");
        } else if (LastAttackAnimal.equals("Minotaur")) {
            playerDead = new Image(new Texture(Gdx.files.internal(animalImagePaths[6])));
            animalName = new Label("Minotaur:", skin, "cutscene");
        } else {
            playerDead = new Image(ServiceLocator.getResourceService().getAsset(PLAYER_DEAD, Texture.class));
            animalName = new Label("", skin, "cutscene");
        }

// Add the player image and animal name
        table.add(playerDead).padTop(Y_PADDING).center();
        table.row();
        table.add(animalName).padTop(Y_PADDING).center();

// Add descriptive labels based on LastAttackAnimal
        Label Rat = new Label("You thought you were the predator, but turns out you are just prey!", skin, "cutscene");
        Label Bear = new Label("Too slow, too soft. You didn't stand a chance against my claws!", skin, "cutscene");
        Label Bat = new Label("Couldn't see in the dark, huh? My world, my rules. Better luck next time, human!", skin, "cutscene");
        Label Dog = new Label("Fetch? More like fight! Too bad you didn't put up much of one.", skin, "cutscene");
        Label Snake = new Label("Slither, slither... you never saw me coming, and now it is lights out for you!", skin, "cutscene");
        Label Dino = new Label("Extinct? Not me! Guess it is you who is heading for the history books!", skin, "cutscene");
        Label Minotaur = new Label("Lost in my labyrinth of power, and now you are just another trophy!", skin, "cutscene");

        table.row();
        if (LastAttackAnimal.equals("Rat")) {
            table.add(Rat).padTop(30f).center();
        } else if (LastAttackAnimal.equals("Bear")) {
            table.add(Bear).padTop(30f).center();
        } else if (LastAttackAnimal.equals("Bat")) {
            table.add(Bat).padTop(30f).center();
        } else if (LastAttackAnimal.equals("Dog")) {
            table.add(Dog).padTop(30f).center();
        } else if (LastAttackAnimal.equals("Snake")) {
            table.add(Snake).padTop(30f).center();
        } else if (LastAttackAnimal.equals("Dino")) {
            table.add(Dino).padTop(30f).center();
        } else if (LastAttackAnimal.equals("Minotaur")) {
            table.add(Minotaur).padTop(30f).center();
        } else {
            table.add(youDied).padTop(30f).center();
        }

// Exit button
        Button exitBtn = new TextButton("Go to Main Screen", skin);
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logger.debug("Going to score screen");
                game.setScreen(MAIN_MENU);
            }
        });

// Score section
        Image coins = new Image(new Texture(Gdx.files.internal("images/items/coin.png")));
        Image kills = new Image(new Texture(Gdx.files.internal("images/player/player_dead.png")));

        table.row();
        Label scoreLabel = new Label("Final Scores", skin, "title");
        table.add(scoreLabel).center().padBottom(20).padTop(10);

        table.row();
        Label coinsLabel = new Label("Coins:     "+String.valueOf(scoreList.getScore().get("coins")), skin, "title");
        table.add(coinsLabel).padLeft(10);
        table.add(coins).center().width(50).height(50);
        table.row();
        Label killsLabel = new Label("Kills:     "+String.valueOf(scoreList.getScore().get("kills") ),skin, "title");
        table.add(killsLabel);
        table.add(kills).width(50).height(50).center();

        table.row();
        table.add(exitBtn).colspan(3).center().padTop(Y_PADDING);

// Add table to the stage
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
