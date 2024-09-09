package com.csse3200.game.physics.components;

public class PlayerHitboxComponent extends ColliderComponent {

  /**
   * Constructs collider component
   */
  public PlayerHitboxComponent() {
    super(1f, 0f, 40f);
  }

  @Override
  public void create() {
    setSensor(true);
    super.create();
  }
}