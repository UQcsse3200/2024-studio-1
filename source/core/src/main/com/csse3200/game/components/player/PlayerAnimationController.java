package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

import java.util.Objects;


/**
 *
 * This class listens to events triggered by the KeyboardPlayerInputComponent
 * and starts the relevant character animation based on the current event.
 */
public class PlayerAnimationController extends Component {
    AnimationRenderComponent animationController;
    private boolean death = false;
    private boolean animationStopped = false;
    private PlayerType playerType;

    public PlayerAnimationController(String textureAtlas) {
        switch (textureAtlas) {
            case ("images/player/player.atlas"):
                this.playerType = PlayerType.Player1;
                break;
            case ("images/player/homeless1.atlas"):
                this.playerType = PlayerType.Player2;
                break;
        }
    }

    private enum PlayerType {
        Player1,
        Player2
    }

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
            switch (playerType) {
                case Player1 -> animationController.startAnimation("walk-left");
                case Player2 -> animationController.startAnimation("Walk");
            }
        }
    }

    /**
     * Starts the player walking right animation.
     */
    void walkRight() {
        if (!death) {
            switch (playerType) {
                case Player1 -> animationController.startAnimation("walk-right");
                case Player2 -> animationController.startAnimation("Walk");
            }
        }
    }

    /**
     * Starts the player walking down animation.
     */
    void walkDown() {
        if (!death) {
            switch (playerType) {
                case Player1 -> animationController.startAnimation("walk-down");
                case Player2 -> animationController.startAnimation("Walk");
            }
        }
    }

    /**
     * Starts the player walking up animation.
     */
    void walkUp() {
        if (!death) {
            switch (playerType) {
                case Player1 -> animationController.startAnimation("walk-up");
                case Player2 -> animationController.startAnimation("Walk");
            }
        }
    }

    /**
     * Starts the player death animation.
     */
    void deathAnimation() {
        switch (playerType) {
            case Player1 -> animationController.startAnimation( "death-down");
            case Player2 -> animationController.startAnimation( "Dead");
        }
        death = true;
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
            switch (playerType) {
                case Player1 -> animationController.startAnimation( "damage-down");
                case Player2 -> animationController.startAnimation( "Hurt");
            }
        }
    }
}