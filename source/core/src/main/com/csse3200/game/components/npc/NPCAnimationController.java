package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to a entity's state and plays the animation when one
 * of the events is triggered.
 */
public class NPCAnimationController extends Component {
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

        entity.getEvents().addListener("Idle_left", this::animateIdleLeft);
        entity.getEvents().addListener("Hurt_left", this::animateHurtLeft);
        entity.getEvents().addListener("Walk_left", this::animateWalkLeft);
        entity.getEvents().addListener("Attack_left", this::animateAttackLeft);
        entity.getEvents().addListener("Death_left", this::animateDeathLeft);
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

    void animateIdleLeft() {
        animator.startAnimation("Idle_left");
    }
    void animateHurtLeft() {
        animator.startAnimation("Hurt_left");
    }
    void animateWalkLeft() {
        animator.startAnimation("Walk_left");
    }
    void animateAttackLeft() {
        animator.startAnimation("Attack_left");
    }
    void animateDeathLeft() {
        animator.startAnimation("Death_left");
    }
}
