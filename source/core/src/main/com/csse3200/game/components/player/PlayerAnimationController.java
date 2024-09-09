package com.csse3200.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 *
 * This class listens to events triggered by the KeyboardPlayerInputComponent
 * and starts the relevant character animation based on the current event.
 */
public class PlayerAnimationController extends Component {
    AnimationRenderComponent animationController;

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
        animationController.startAnimation("idle");
    }

    /**
     * Starts the player stationary animation.
     */
    void stationaryAnimation() { animationController.startAnimation("idle"); }

    /**
     * Starts the player walking left animation.
     */
    void walkLeft() {
        animationController.startAnimation("walk-left");
    }

    /**
     * Starts the player walking right animation.
     */
    void walkRight() {
        animationController.startAnimation("walk-right");
    }

    /**
     * Starts the player walking down animation.
     */
    void walkDown() {
        animationController.startAnimation("walk-down");
    }

    /**
     * Starts the player walking up animation.
     */
    void walkUp() {
        animationController.startAnimation("walk-up");
    }
}
