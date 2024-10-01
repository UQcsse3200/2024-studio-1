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

        entity.getEvents().addListener("fire_attack_left", this::animateFireLeft);
        entity.getEvents().addListener("fire_attack_right", this::animateFireRight);
    }

    void animateFireLeft() {
        animator.startAnimation("fire_attack_left");
    }

    void animateFireRight() {
        animator.startAnimation("fire_attack_right");
    }
}
