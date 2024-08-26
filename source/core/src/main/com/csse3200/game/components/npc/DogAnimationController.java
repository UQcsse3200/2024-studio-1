package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a dog entity's state and plays the animation when one
 * of the events is triggered.
 */
public class DogAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("wanderStop", this::animateIdle);
        entity.getEvents().addListener("wanderStart", this::animateWalk);
        entity.getEvents().addListener("chaseStart", this::animateRun);
        entity.getEvents().addListener("chaseEnd", this::animateStop);
        entity.getEvents().addListener("attack", this::animateAttack);
        entity.getEvents().addListener("death", this::animateDeath);
    }

    void animateIdle() {
        animator.startAnimation("idle");
    }
    void animateWalk() {
        animator.startAnimation("walk");
    }
    void animateRun() {
        animator.startAnimation("walk");
    }
    void animateStop() {
        animator.startAnimation("gesture");
    }
    void animateAttack() {
        animator.startAnimation("attack");
    }
    void animateDeath() {
        animator.startAnimation("death");
    }
}
