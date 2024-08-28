package com.csse3200.game.components.maingame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
  private Table table;

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

    table = new Table();
    table.top().right();
    table.setFillParent(true);

    ImageButton mainMenuBtn = new ImageButton(skin);
    mainMenuBtn.getStyle().imageUp = new TextureRegionDrawable(icons[0][0]);
    mainMenuBtn.getStyle().imageOver = new TextureRegionDrawable(iconsOver[0][0]);
    mainMenuBtn.getStyle().imageDown = new TextureRegionDrawable(iconsDown[0][0]);

    // Triggers an event when the button is pressed.
    mainMenuBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          logger.debug("Exit button clicked");
          entity.getEvents().trigger("exit");
        }
      });

    table.add(mainMenuBtn).padTop(10f).padRight(10f);

    stage.addActor(table);
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
