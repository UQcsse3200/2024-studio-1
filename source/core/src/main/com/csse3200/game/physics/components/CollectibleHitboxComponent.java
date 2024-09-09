package com.csse3200.game.physics.components;

/**
 * Physics comp
 */
public class CollectibleHitboxComponent extends ColliderComponent {

  /**
   * Constructs collider component
   */
  public CollectibleHitboxComponent() {
    super(8f);
  }

  @Override
  public void create() {
    setSensor(true);
    super.create();
  }
}