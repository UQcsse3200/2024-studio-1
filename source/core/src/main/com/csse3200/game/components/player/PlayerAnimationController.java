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
    private PlayerNum playerNum;

    /**
    Indicate the selected player.
     */
    private enum PlayerNum {
        Player1,
        Player2,
        Player3,
        Bear,
        Player4
    }

    /**
     * Assign the Player Number by the associated texture atlas name.
     * @param textureAtlas the texture atlas associated with the player.
     */
    public PlayerAnimationController(String textureAtlas) {
        switch (textureAtlas) {
            case ("images/player/player.atlas"):
                this.playerNum = PlayerNum.Player1;
                break;
            case ("images/player/homeless1.atlas"):
                this.playerNum = PlayerNum.Player2;
                break;
            case "images/npc/bear/bear.atlas":
                this.playerNum = PlayerNum.Bear;
            case ("images/player/homeless2.atlas"):
                this.playerNum = PlayerNum.Player3;
                break;
            case ("images/player/homeless3.atlas"):
                this.playerNum = PlayerNum.Player4;
                break;
        }
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
        stationaryAnimation();
    }

    /**
     * Starts the player stationary animation.
     */
    void stationaryAnimation() {
        if (!death) {
            switch (playerNum) {
                case Bear -> animationController.startAnimation("idle_bottom");
                case Player1, Player2, Player3, Player4-> animationController.startAnimation("idle");
            }
        }
    }

    /**
     * Starts the player walking left animation.
     */
    void walkLeft() {
        if (!death) {
            switch (playerNum) {
                case Player1 -> animationController.startAnimation("walk-left");
                case Bear -> animationController.startAnimation("walk_left");
                case Player2, Player3, Player4 -> animationController.startAnimation("Walk");
            }
        }
    }

    /**
     * Starts the player walking right animation.
     */
    void walkRight() {
        if (!death) {
            switch (playerNum) {
                case Player1 -> animationController.startAnimation("walk-right");
                case Bear -> animationController.startAnimation("walk_right");
                case Player2, Player3, Player4 -> animationController.startAnimation("Walk");
            }
        }
    }

    /**
     * Starts the player walking down animation.
     */
    void walkDown() {
        if (!death) {
            switch (playerNum) {
                case Player1 -> animationController.startAnimation("walk-down");
                case Bear -> animationController.startAnimation("walk_bottom");
                case Player2, Player3, Player4 -> animationController.startAnimation("Walk");
            }
        }
    }

    /**
     * Starts the player walking up animation.
     */
    void walkUp() {
        if (!death) {
            switch (playerNum) {
                case Player1 -> animationController.startAnimation("walk-up");
                case Player2, Player3, Player4 -> animationController.startAnimation("Walk");
                case Bear -> animationController.startAnimation("walk_top");
            }
        }
    }

    /**
     * Starts the player death animation.
     */
    void deathAnimation() {
        switch (playerNum) {
            case Player1 -> animationController.startAnimation( "death-down");
            case Player2, Player3, Player4 -> animationController.startAnimation( "Dead");
            case Bear -> animationController.startAnimation("death_left");
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
            switch (playerNum) {
                case Player1 -> animationController.startAnimation( "damage-down");
                case Player2, Player3, Player4 -> animationController.startAnimation( "Hurt");
                case Bear -> animationController.startAnimation("walk_top");
            }
        }
    }
}
