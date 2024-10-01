package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.entities.Entity;

/**
 * The bleed item reduces 5% health of enemies per second for 5 seconds.
 * The bleed item is a boss drop after killing the werewolf
 * The weapons on which the bleed effect is available is axe and knife
 */
public class BleedBuff extends BuffItem {

  // Percentage of health to reduce per second
  private final int healthReductionPerc = 5;

  public BleedBuff() {}

  @Override
  public void effect(Entity entity) {
    // Listen for events when the player hits an enemy with Bleed weapon
    entity.getEvents().addListener("BleedWeaponHit", this::applyBleedEffect);

  }

  private void applyBleedEffect(Entity enemy) {
    CombatStatsComponent enemyStats = enemy.getComponent(CombatStatsComponent.class);

    if (enemyStats != null) {
      // Schedule bleed damage over 5 seconds
      Timer.schedule(new Timer.Task() {
        @Override
        public void run() {
          if (enemyStats.getHealth() > 0) {
            float bleedAmount = enemyStats.getHealth() * (healthReductionPerc / 100f);
            enemyStats.setHealth((int) (enemyStats.getHealth() - bleedAmount));
          } else {
            // Stop applying bleed effect if the enemy is dead
            cancel();
          }
        }
      }, 1, 1, 5); // Reduce health for 5 seconds
    }
  }

  @Override
  public String getName() {
    return "Bleed Buff";
  }

  @Override
  public Texture getIcon() {
    return new Texture("images/items/bleed.png");
  }

  @Override
  public void drop(Inventory inventory) {}

  @Override
  public String getBuffSpecification() {
    return "bleedbuff";
  }

  public int getHealthReductionPerc() {
    return healthReductionPerc;
  }
}
