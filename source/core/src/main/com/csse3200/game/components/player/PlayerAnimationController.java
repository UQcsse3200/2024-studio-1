package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 *
 * This class listens to events triggered by the KeyboardPlayerInputComponent
 * and starts the relevant character animation based on the current event.
 */
public class PlayerAnimationController extends Component {
    AnimationRenderComponent animationController;
    private boolean death = false;
    private boolean animationStopped = false;

    /**
     * Adds the event listener to the character entity to trigger an animation change.
     */
    @Override
    public void create() {
        super.create();

        animationController = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("walkLeft", this::walkLeft);
        entity.getEvents().addListener("walkRight", this::walkRight);
        entity.getEvents().addListener("walkUp", this::walkUp);
        entity.getEvents().addListener("walkDown", this::walkDown);
        entity.getEvents().addListener("walkStop", this::stationaryAnimation);
        entity.getEvents().addListener("death", this::deathAnimation);
        entity.getEvents().addListener("playerHit", this::damageAnimation);
        entity.getEvents().addListener("stopAnimation", this::stopAnimation);
        animationController.startAnimation("idle");
    }

    /**
     * Starts the player stationary animation.
     */
    void stationaryAnimation() {
        if (!death) {
            animationController.startAnimation("idle");
        }
    }

    /**
     * Starts the player walking left animation.
     */
    void walkLeft() {
        if (!death) {
            animationController.startAnimation("walk-left");
        }
    }

    /**
     * Starts the player walking right animation.
     */
    void walkRight() {
        if (!death) {
            animationController.startAnimation("walk-right");
        }
    }

    /**
     * Starts the player walking down animation.
     */
    void walkDown() {
        if (!death) {
            animationController.startAnimation("walk-down");
        }
    }

    /**
     * Starts the player walking up animation.
     */
    void walkUp() {
        if (!death) {
            animationController.startAnimation("walk-up");
        }
    }

    /**
     * Starts the player death animation.
     */
    void deathAnimation() {
        death = true;
        animationController.startAnimation("death-down");
    }

    /**
     * Stops the player death animation.
     */
    boolean stopAnimation() {
        if (animationController.isFinished()) {
            animationController.stopAnimation();
            animationStopped = true;
        }
        return animationStopped;
    }

    /**
     * Starts the player damage animation.
     */
    void damageAnimation() {
        if (!death) {
            animationController.startAnimation("damage-down");
        }
    }
}
