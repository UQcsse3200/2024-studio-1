package com.csse3200.game.components.screendisplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;  // Import InputMultiplexer
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
import java.util.ArrayList;

import static com.csse3200.game.GdxGame.ScreenType.MAIN_MENU;
import static com.csse3200.game.files.FileLoader.readClass;
import static com.csse3200.game.screens.LoseScreen.PLAYER_DEAD;
import static com.csse3200.game.services.ServiceLocator.getResourceService;

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

    private ArrayList<String> animals = new ArrayList<String>();
    private ArrayList<String> animalDeathScreens = new ArrayList<String>();

    private Texture backgroundTexture;

    String[] animalImagePaths = {
            "images/how-to-play/animals/rat.png",
            "images/how-to-play/animals/bear.png",
            "images/how-to-play/animals/bat.png",
            "images/how-to-play/animals/dog.png",
            "images/how-to-play/animals/snake.png",
            "images/how-to-play/animals/dino.png",
            "images/how-to-play/animals/minotaur.png",
            "images/how-to-play/animals/dragon.png",
            "images/how-to-play/animals/werewolf.png",
            "images/how-to-play/animals/cthulu.png",
            "images/how-to-play/animals/kitsune.png",
            "images/how-to-play/animals/birdman.png"
    };

    String[] backgroundPaths = {
            "images/death_screen/rat_bg.jpg",
            "images/death_screen/bear_bg.jpg",
            "images/death_screen/bat_bg.jpg",
            "images/death_screen/dog_bg.jpg",
            "images/death_screen/snake_bg.jpg",
            "images/death_screen/dino_bg.jpg",
            "images/death_screen/minotaur_bg.jpg",
            "images/death_screen/dragon_bg.jpg",
            "images/death_screen/werewolf_bg.jpg",
            "images/death_screen/cthulu_bg.jpg",
            "images/death_screen/kitsune_bg.jpg",
            "images/death_screen/birdman_bg.jpg",
    };

    public static String capitalizeFirstChar(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

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

        // Use InputMultiplexer to handle both stage (for button clicks) and custom input (for ESC key)
        InputMultiplexer inputMultiplexer = new InputMultiplexer();

        // Add the stage to handle button clicks and other UI events
        inputMultiplexer.addProcessor(stage);

        // Add custom input processor to handle the ESC key
        inputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    logger.debug("Esc key pressed, going back to menu");
                    game.setScreen(MAIN_MENU); // Go back to menu
                    return true;
                }
                return false;
            }
        });

        // Set the input processor to the multiplexer
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    private void addActors() {
      
        animals.add("Rat");
        animals.add("Bear");
        animals.add("Bat");
        animals.add("Dog");
        animals.add("Snake");
        animals.add("Dino");
        animals.add("Minotaur");
        animals.add("Dragon");
        animals.add("Werewolf");
        animals.add("Cthulu");
        animals.add("Kitsune");
        animals.add("Birdman");

        animalDeathScreens.add("You thought you were the predator, but turns out you are just prey!");
        animalDeathScreens.add("Too slow, too soft. You didn't stand a chance against my claws!");
        animalDeathScreens.add("Couldn't see in the dark, huh? My world, my rules. Better luck next time, human!");
        animalDeathScreens.add("Fetch? More like fight! Too bad you didn't put up much of one.");
        animalDeathScreens.add("Slither, slither... you never saw me coming, and now it is lights out for you!");
        animalDeathScreens.add("Extinct? Not me! Guess it is you who is heading for the history books!");
        animalDeathScreens.add("Lost in my labyrinth of power, and now you are just another trophy!");
        animalDeathScreens.add("Foolish mortal, my fire has forged empires and destroyed heroes. What made you think you could survive?");
        animalDeathScreens.add("Ah, the moon's glow reveals the truth: you were never a match for the beast within me!");
        animalDeathScreens.add("You are but a fleeting whisper in the vast eternity of my dreams. Insignificant, erased!");
        animalDeathScreens.add("Deceived by shadows and tricked by tales, your end came at the hands of my cunning!");
        animalDeathScreens.add("From the skies I watched, and with swift wings I struck. Did you really think you could escape my gaze?");



        LastAttackAnimal = readClass(String.class, "configs/LastAttack.json", FileLoader.Location.EXTERNAL);
        if(!LastAttackAnimal.equals("Unknown")){
            int firstSpaceIndex = LastAttackAnimal.indexOf(' ');
            if (firstSpaceIndex != -1) { // Check if there is at least one space in the string
                LastAttackAnimal = LastAttackAnimal.substring(0, firstSpaceIndex);
            }
            LastAttackAnimal = capitalizeFirstChar(LastAttackAnimal);
            System.out.println(LastAttackAnimal);
        }
        Label youDied = new Label("You died...", skin, "cutscene");
        Label animalName;
        Image playerDead;

   
        if(animals.contains(LastAttackAnimal)){
            backgroundTexture = new Texture(Gdx.files.internal(backgroundPaths[animals.indexOf(LastAttackAnimal)]));
            Image background = new Image(backgroundTexture);
            background.setFillParent(true); // Make it fill the screen
            stage.addActor(background);
            table = new Table();
            table.setFillParent(true);
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
            table = new Table();
            table.setFillParent(true);
            // See assets/images/player/player_asset_citation.txt
            playerDead = new Image(
                    ServiceLocator.getResourceService().getAsset(PLAYER_DEAD, Texture.class)
            );
            animalName = new Label("", skin, "cutscene");
            table.add(playerDead).padTop(Y_PADDING);
            table.row();
            table.add(animalName).padTop(Y_PADDING);
            table.row();
            table.add(youDied).padTop(30f);
        }
        Button exitBtn = new TextButton("Back to menu", skin);
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
        // Dispose the background texture
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
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
