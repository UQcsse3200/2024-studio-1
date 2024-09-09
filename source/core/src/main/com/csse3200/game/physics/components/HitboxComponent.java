package com.csse3200.game.physics.components;

/**
 * Physics comp
 */
public class HitboxComponent extends ColliderComponent {

  /** Constructs collider component */
  public HitboxComponent() {
    super();
  }

  /**
   * Constructs collider component
   *
   * @param boxRatio pass 1f for default box size, otherwise greater value will lead to smaller bounding box
   */
  public HitboxComponent(float boxRatio) {
    super(boxRatio);
  }

  /**
   * Constructs collider component
   *
   * @param translateX translate bounding box on x-axis
   * @param translateY translate bounding box on y-axis
   */
  public HitboxComponent(float translateX, float translateY) {
    super(translateX, translateY);
  }

  /**
   * Constructs collider component
   *
   * @param boxRatio pass 1f for default box size, otherwise greater value will lead to smaller bounding box
   * @param translateX translate bounding box on x-axis
   * @param translateY translate bounding box on y-axis
   */
  public HitboxComponent(float boxRatio, float translateX, float translateY) {
    super(boxRatio, translateX, translateY);
  }

  @Override
  public void create() {
    setSensor(true);
    super.create();
  }
}