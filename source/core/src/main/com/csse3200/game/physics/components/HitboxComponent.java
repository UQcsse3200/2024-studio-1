package com.csse3200.game.physics.components;

import com.badlogic.gdx.math.Vector2;

/**
 * Physics comp
 */
public class HitboxComponent extends ColliderComponent {
  @Override
  public void create() {
    setSensor(true);
    super.create();
  }

  /**
   * Set physics as a box with a given size. Box is centered around the entity.
   * @param size
   * @return
   */
  public HitboxComponent setSize(Vector2 size) {
    return (HitboxComponent) super.setAsBox(size);
  }
}
