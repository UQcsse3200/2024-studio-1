package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * A ui component for displaying player stats, e.g. health.
 */
public class PlayerStatsDisplay extends UIComponent {
  Table table;
  private Image heartImage;
  private Label healthLabel;
  private Label pickaxeLabel;
  private Label shotgunLabel;

  /**
   * Creates reusable ui styles and adds actors to the stage.
   */
  @Override
  public void create() {
    super.create();
    addActors();

    entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
    entity.getEvents().addListener("updateMeleeWeaponCount", this::updateMeleeWeaponCountUI);
    entity.getEvents().addListener("updateRangedWeaponCount", this::updateRangedWeaponCountUI);
  }

  /**
   * Creates actors and positions them on the stage using a table.
   * @see Table for positioning options
   */
  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);
    table.padTop(45f).padLeft(5f);

    // Heart image
    float heartSideLength = 30f;
    heartImage = new Image(ServiceLocator.getResourceService().getAsset("images/heart.png", Texture.class));

    // Health text
    int health = entity.getComponent(CombatStatsComponent.class).getHealth();
    CharSequence healthText = String.format("Health: %d", health);
    healthLabel = new Label(healthText, skin, "small");

    //Weapon text, like the name of weapon
    String weapon = ""; //entity.getComponent(WeaponComponent.class).getWeaponType();
    CharSequence weaponText = String.format("Weapon: %s", weapon);
    pickaxeLabel = new Label("Pickaxe: 0", skin, "large");
    shotgunLabel = new Label("Shotgun: 0", skin, "large");


    table.add(heartImage).size(heartSideLength).pad(5);
    table.add(healthLabel).padLeft(10).left();
    table.row().padTop(10);
    table.add(pickaxeLabel).colspan(2).padLeft(10).left();
    table.row().padTop(10);
    table.add(shotgunLabel).colspan(2).padLeft(10).left();
    stage.addActor(table);
  }

  @Override
  public void draw(SpriteBatch batch)  {
    // draw is handled by the stage
  }

  /**
   * Updates the player's health on the ui.
   * @param health player health
   */
  public void updatePlayerHealthUI(int health) {
    CharSequence text = String.format("Health: %d", health);
    healthLabel.setText(text);
  }

  public void updateMeleeWeaponCountUI(int weaponCount) {
    CharSequence text = String.format("Pickaxe x %d", weaponCount);
    pickaxeLabel.setText(text);
  }

  public void updateRangedWeaponCountUI(int weaponCount) {
    CharSequence text = String.format("Shotgun x %d", weaponCount);
    shotgunLabel.setText(text);
  }

  @Override
  public void dispose() {
    super.dispose();
    heartImage.remove();
    healthLabel.remove();
  }
}
