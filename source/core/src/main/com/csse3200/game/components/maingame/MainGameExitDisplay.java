package com.csse3200.game.components.maingame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.files.FileLoader;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameExitDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);
  private static final float Z_INDEX = 2f;
  private static final float BTN_SPACING = 15f;
  private Table table;
  private ImageButton pauseBtn;
  private Table pauseTable;
  private SelectBox<String> actionSelect;
  private SelectBox<String> keySelect;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    Texture iconSheet = ServiceLocator.getResourceService()
                .getAsset("images/ui_white_icons.png", Texture.class);
    TextureRegion[][] icons = new TextureRegion(iconSheet).split(
    iconSheet.getWidth() / 6,
    iconSheet.getHeight() / 6);

    Texture iconOverSheet = ServiceLocator.getResourceService()
                .getAsset("images/ui_white_icons_over.png", Texture.class);
    TextureRegion[][] iconsOver = new TextureRegion(iconOverSheet).split(
    iconOverSheet.getWidth() / 6,
    iconOverSheet.getHeight() / 6);

    Texture iconDownSheet = ServiceLocator.getResourceService()
                .getAsset("images/ui_white_icons_down.png", Texture.class);
    TextureRegion[][] iconsDown = new TextureRegion(iconDownSheet).split(
    iconDownSheet.getWidth() / 6,
    iconDownSheet.getHeight() / 6);

    /*TextureAtlas atlas = ServiceLocator.getResourceService()
                .getAsset("flat-earth/skin/flat-earth-ui.atlas", TextureAtlas.class);
    TextureRegion windowTexture = atlas.findRegion("list");*/
    Texture blackDotTrans = ServiceLocator.getResourceService()
                .getAsset("images/black_dot_transparent.png", Texture.class);

    table = new Table();
    table.top().right();
    table.setFillParent(true);

    pauseTable = new Table();
    pauseTable.setFillParent(true);
    pauseTable.setBackground(new TextureRegionDrawable(new TextureRegion(blackDotTrans)));

    pauseBtn = new ImageButton(skin);
    pauseBtn.getStyle().imageUp = new TextureRegionDrawable(icons[1][1]);
    pauseBtn.getStyle().imageOver = new TextureRegionDrawable(iconsOver[1][1]);
    pauseBtn.getStyle().imageDown = new TextureRegionDrawable(iconsDown[1][1]);

    TextButton resumeBtn = new TextButton("Resume", skin);
    TextButton saveBtn = new TextButton("Save", skin);
    TextButton restartBtn = new TextButton("Restart", skin);
    TextButton exitBtn = new TextButton("Exit", skin);

    Label keybindingsLabel = new Label("Keybindings : ", skin);
    Label actionLabel = new Label("Action: ", skin);
    actionSelect = new SelectBox<>(skin);
    actionSelect.setItems(actions);
    Label keyLabel = new Label("Key: ", skin);
    keySelect = new SelectBox<>(skin);
    keySelect.setItems(keys);


    //window = new Image(windowTexture);

    // Triggers an event when the button is pressed.
    pauseBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          pauseGame();
        }
      });
    resumeBtn.addListener(
      new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
             unpause();
          }
      });
    exitBtn.addListener(
      new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
             unpause();
              logger.debug("Exit button clicked");
              entity.getEvents().trigger("exit");
          }
      });
    saveBtn.addListener(
            new ChangeListener() {
              @Override
              public void changed(ChangeEvent changeEvent, Actor actor) {
                System.out.println("Save button clicked");
                Label saveLabel = new Label("Game saved!", skin);
                pauseTable.add(saveLabel).padTop(BTN_SPACING);
                pauseTable.row();
                saveGame();
              }});

    table.add(pauseBtn).padTop(10f).padRight(10f);

    pauseTable.add(resumeBtn).padTop(BTN_SPACING);
    pauseTable.row();
    pauseTable.add(saveBtn).padTop(BTN_SPACING);
    pauseTable.row();
    pauseTable.add(restartBtn).padTop(BTN_SPACING);
    pauseTable.row();
    pauseTable.add(exitBtn).padTop(BTN_SPACING);
    pauseTable.row();

    pauseTable.row().padTop(10f);
    pauseTable.add(keybindingsLabel);
    pauseTable.row().padTop(10f);
    pauseTable.add(actionLabel);
    pauseTable.row();
    pauseTable.add(actionSelect);
    pauseTable.row();
    pauseTable.add(keyLabel);
    pauseTable.row();
    pauseTable.add(keySelect);

    stage.addActor(table);
  }

  // todo refactor pausing to use events instead of public methods/fields

  /**
   * Pause the game, show the pause menu.
   */
  public void pauseGame() {
    MainGameScreen.isPaused = true;
    logger.info("Game paused, {}", ServiceLocator.getEntityService());

    stage.addActor(pauseTable);
    table.remove();
  }

  /**
   * Unpause the game, remove the pause menu.
   */
  public void unpause() {
    MainGameScreen.isPaused = false;
    pauseTable.remove();
    stage.addActor(table);
  }

  public class EntityCoordinates {
    private float x;
    private float y;

    public EntityCoordinates(float x, float y) {
      this.x = x;
      this.y = y;
    }

    public float getX() {
      return x;
    }

    public float getY() {
      return y;
    }
  }

  public void saveGame() {
    Array<EntityCoordinates> entities = new Array<>();
    for (Entity entity : ServiceLocator.getEntityService().getEntities()) {
       Vector2 pos = entity.getPosition();
       float x = pos.x;
       float y = pos.y;
       EntityCoordinates coordinates = new EntityCoordinates(x, y);
       entities.add(coordinates);
    }
    String filePath = "configs/save.json";
    FileLoader.writeClass(entities, filePath, FileLoader.Location.EXTERNAL);
    logger.debug("Game saved to: " + filePath);
  }

  public void resize(int width, int height){
    //window.setSize(width/4, height/4);
  }

  @Override
  public void draw(SpriteBatch batch) {
    // draw is handled by the stage
  }

  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  @Override
  public void dispose() {
    table.clear();
    super.dispose();
  }
}
