package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events triggered by the KeyboardPlayerInputComponent
 * and starts the relevant character animation based on the current event.
 */
public class PlayerAnimationController extends Component {
    AnimationRenderComponent animationController;
    private boolean death = false;
    private boolean animationStopped = false;
    private PlayerNum playerNum;
    private Direction previousDirection;
    private Direction previousDirectionHorizontal;

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
     Indicate the previous direction player.
     */
    private enum Direction {
        up,
        down,
        left,
        right
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
                break;
            case ("images/player/homeless2.atlas"):
                this.playerNum = PlayerNum.Player3;
                break;
            case ("images/player/homeless3.atlas"):
                this.playerNum = PlayerNum.Player4;
                break;
        }
        this.previousDirection = Direction.down;
        this.previousDirectionHorizontal = Direction.left;
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
     * Starts the player stationary animation, directionally based.
     */
    private void stationaryAnimation() {
        if (!death) {
            String animationName;

            // Determine the animation name based on previous direction and player
            if (playerNum != PlayerNum.Player1 && playerNum != PlayerNum.Bear) {
                animationName = (previousDirectionHorizontal == Direction.right) ? "Idle_right" : "Idle_left";
            } else {
                animationName = switch (previousDirection) {
                    case down -> (playerNum == PlayerNum.Bear) ? "idle_bottom" : "idle";
                    case left -> (playerNum == PlayerNum.Bear) ? "idle_left" : "idle";
                    case right -> (playerNum == PlayerNum.Bear) ? "idle_right" : "idle";
                    case up -> (playerNum == PlayerNum.Bear) ? "idle_top" : "idle";
                };
            }
            animationController.startAnimation(animationName);
        }
    }

    /**
     * Starts the player walking left animation.
     */
    private void walkLeft() {
        if (!death) {
            switch (playerNum) {
                case Player1 -> animationController.startAnimation("walk-left");
                case Bear -> animationController.startAnimation("walk_left");
                case Player2, Player3, Player4 -> animationController.startAnimation("Walk_left");
            }
            previousDirectionHorizontal = Direction.left;
            previousDirection = Direction.left;
        }
    }

    /**
     * Starts the player walking right animation.
     */
    private void walkRight() {
        if (!death) {
            switch (playerNum) {
                case Player1 -> animationController.startAnimation("walk-right");
                case Bear -> animationController.startAnimation("walk_right");
                case Player2, Player3, Player4 -> animationController.startAnimation("Walk_right");
            }
            previousDirection = Direction.right;
            previousDirectionHorizontal = Direction.right;
        }
    }

    /**
     * Starts the player walking down animation, or the left/right walking animations
     *      * for players without the vertical animations.
     */
    private void walkDown() {
        if (!death) {
            String animationName;

            // Determine the animation name based on previous direction and player
            animationName = switch (playerNum) {
                case Player1 -> "walk-down";
                case Player2, Player3, Player4 ->
                        (previousDirectionHorizontal == Direction.left) ? "Walk_left" : "Walk_right";
                case Bear -> "walk_bottom";
            };

            animationController.startAnimation(animationName);
            previousDirection = Direction.down;
        }
    }

    /**
     * Starts the player walking up animation, or the left/right walking animations
     * for players without the vertical animations.
     */
    private void walkUp() {
        if (!death) {
            String animationName;

            // Determine the animation name based on previous direction and player
            animationName = switch (playerNum) {
                case Player1 -> "walk-up";
                case Player2, Player3, Player4 ->
                        (previousDirectionHorizontal == Direction.left) ? "Walk_left" : "Walk_right";
                case Bear -> "walk_top";
            };

            animationController.startAnimation(animationName);
            previousDirection = Direction.up;
        }
    }

    /**
     * Starts the player death animation.
     */
    void deathAnimation() {
        String animationName;

        // Determine the animation name based on previous direction and player
        if (playerNum != PlayerNum.Player1 && playerNum != PlayerNum.Bear) {
            animationName =
                    (previousDirectionHorizontal == Direction.right) ? "Dead_right" : "Dead_left";
        } else {
            switch (previousDirection) {
                case down -> animationName =
                        (playerNum == PlayerNum.Player1) ? "death-down" : "death-left";
                case left -> animationName =
                        (playerNum == PlayerNum.Player1) ? "death-left" : "death_left";
                case right -> animationName =
                        (playerNum == PlayerNum.Player1) ? "death-right" : "death_left";
                default -> animationName =
                        (playerNum == PlayerNum.Player1) ? "death-up" : "death-down";
            }
        }

        animationController.startAnimation(animationName);
        death = true;
    }


    /**
     * Stops the player death animation.
     */
    public boolean stopAnimation() {
        if (animationController.isFinished()) {
            animationController.stopAnimation();
            animationStopped = true;
        }
        return animationStopped;
    }

    /**
     * Starts the player damage animation.
     */
    private void damageAnimation() {
        if (!death) {
            String animationName;

            // Determine the animation name based on previous direction and player
            switch (playerNum) {
                case Player1 -> animationName = "damage-down" ;
                case Player2, Player3, Player4 -> animationName =
                        (previousDirectionHorizontal == Direction.right) ? "Hurt_right" : "Hurt_left";
                case Bear -> animationName =
                        (previousDirectionHorizontal == Direction.right) ? "attack_right" : "attack_left";
                default -> animationName = "idle";
            }

            animationController.startAnimation(animationName);
        }
    }
}
