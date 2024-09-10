package com.csse3200.game.components.maingame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.screens.MainGameScreen;

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

    //window = new Image(windowTexture);

    // Triggers an event when the button is pressed.
    pauseBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          MainGameScreen.isPaused=true;
          stage.addActor(pauseTable);
          table.remove();
        }
      });
    resumeBtn.addListener(
      new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
             MainGameScreen.isPaused=false;
            pauseTable.remove();
            stage.addActor(table);
          }
      });
    exitBtn.addListener(
      new ChangeListener() {
          @Override
          public void changed(ChangeEvent changeEvent, Actor actor) {
             MainGameScreen.isPaused=false;
              logger.debug("Exit button clicked");
              entity.getEvents().trigger("exit");
          }
      });

    table.add(pauseBtn).padTop(10f).padRight(10f);

    pauseTable.add(resumeBtn).padTop(BTN_SPACING);
    pauseTable.row();
    pauseTable.add(saveBtn).padTop(BTN_SPACING);
    pauseTable.row();
    pauseTable.add(restartBtn).padTop(BTN_SPACING);
    pauseTable.row();
    pauseTable.add(exitBtn).padTop(BTN_SPACING);

    stage.addActor(table);
  }

  public void pauseGame()
  {
    InputEvent event1 = new InputEvent();
    event1.setType(InputEvent.Type.touchDown);
    pauseBtn.fire(event1);

    InputEvent event2 = new InputEvent();
    event2.setType(InputEvent.Type.touchUp);
    pauseBtn.fire(event2);
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
