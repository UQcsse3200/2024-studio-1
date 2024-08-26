package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a Dino entity's state and plays the animation when one
 * of the events is triggered.
 */
public class DinoAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("idle", this::animateIdle);
        entity.getEvents().addListener("gesture", this::animateGesture);
        entity.getEvents().addListener("walk", this::animateWalk);
        entity.getEvents().addListener("attack", this::animateAttack);
        entity.getEvents().addListener("death", this::animateDeath);
    }

    void animateIdle() {
        animator.startAnimation("idle");
    }
    void animateGesture() {
        animator.startAnimation("gesture");
    }
    void animateWalk() {
        animator.startAnimation("walk");
    }
    void animateAttack() {
        animator.startAnimation("attack");
    }
    void animateDeath() {
        animator.startAnimation("death");
    }
}
