package com.csse3200.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;

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
  public ColliderComponent setSize(Vector2 size) {
    return super.setAsBox(size);
  }
}
