package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a bear entity's state and plays the animation when one
 * of the events is triggered.
 *
 */
public class BearAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("idle", this::animateIdle);
        entity.getEvents().addListener("walk_left", this::animateWalkLeft);
        entity.getEvents().addListener("walk_right", this::animateWalkRight);
        entity.getEvents().addListener("run_left", this::animateRunLeft);
        entity.getEvents().addListener("run_right", this::animateRunRight);
        entity.getEvents().addListener("attack_left", this::animateAttackLeft);
        entity.getEvents().addListener("attack_right", this::animateAttackRight);
        entity.getEvents().addListener("death", this::animateDeath);
    }

    void animateIdle() {
        animator.startAnimation("idle");
    }
    void animateWalkRight() {
        animator.startAnimation("walk_right");
    }
    void animateWalkLeft() {
        animator.startAnimation("walk_left");
    }
    void animateRunLeft() {
        animator.startAnimation("run_left");
    }
    void animateRunRight() {
        animator.startAnimation("run_right");
    }
    void animateAttackLeft() {
        animator.startAnimation("attack_left");
    }
    void animateAttackRight() {
        animator.startAnimation("attack_right");
    }
    void animateDeath() {
        animator.startAnimation("death");
    }
}
