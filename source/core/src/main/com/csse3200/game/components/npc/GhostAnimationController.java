package com.csse3200.game.components.npc;

import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class GhostAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("wanderStart", this::animateWander);
    entity.getEvents().addListener("chaseStart", this::animateChase);
  }

  @Override
  public void update() {
    super.update();
    CombatStatsComponent stats = entity.getComponent(CombatStatsComponent.class);
    if (stats != null) {
      if (stats.getHealth() < stats.getMaxHealth() / 2) {
        animator.startAnimation("angry_float");
      } else {
        animator.startAnimation("float");
      }
    }
  }

  void animateWander() {
    animator.startAnimation("float");
  }

  void animateChase() {
    animator.startAnimation("angry_float");
  }
}
