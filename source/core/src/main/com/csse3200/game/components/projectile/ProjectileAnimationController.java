package com.csse3200.game.components.projectile;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.npc.DirectionalNPCComponent;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a entity's state and plays the animation when one
 * of the events is triggered.
 */
public class ProjectileAnimationController extends Component {
    AnimationRenderComponent animator;
    DirectionalNPCComponent directionalComponent;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        directionalComponent = this.entity.getComponent(DirectionalNPCComponent.class);

        entity.getEvents().addListener("fire_attack", this::animateFire);
    }

    void animateFire() {
        if (animator.hasAnimation("fire_attack_right") && animator.hasAnimation("fire_attack_left")) {
            triggerDirectionalAnimation("fire_attack");
        } else if (animator.hasAnimation("fire_attack")) {
            animator.startAnimation("fire_attack");
        } else {
            throw new IllegalStateException("No fire_attack animation found");
        }
    }

    private void triggerDirectionalAnimation(String baseAnimation) {
        String direction = directionalComponent.getDirection();
        if (direction.equals("right")) {
            animator.startAnimation(baseAnimation + "_right");
        } else {
            animator.startAnimation(baseAnimation + "_left");
        }
    }
}
