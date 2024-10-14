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
     * Indicate the selected player.
     */
    public enum PlayerNum {
        PLAYER_1,
        PLAYER_2,
        PLAYER_3,
        BEAR,
        PLAYER_4
    }

    /**
     * Indicate the previous direction player.
     */
    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    /**
     * Assign the Player Number by the associated texture atlas name.
     * @param textureAtlas the texture atlas associated with the player.
     */
    public PlayerAnimationController(String textureAtlas) {
        switch (textureAtlas) {
            case ("images/player/player.atlas"):
                this.playerNum = PlayerNum.PLAYER_1;
                break;
            case ("images/player/homeless1.atlas"):
                this.playerNum = PlayerNum.PLAYER_2;
                break;
            case "images/npc/bear/bear.atlas":
                this.playerNum = PlayerNum.BEAR;
                break;
            case ("images/player/homeless2.atlas"):
                this.playerNum = PlayerNum.PLAYER_3;
                break;
            case ("images/player/homeless3.atlas"):
                this.playerNum = PlayerNum.PLAYER_4;
                break;
        }
        this.previousDirection = Direction.DOWN;
        this.previousDirectionHorizontal = Direction.LEFT;
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
     * Returns the currently selected player number.
     * @return The selected player number (e.g. PLAYER_1, PLAYER_2, etc.)
     */
    public PlayerNum getPlayerNum() {
        return playerNum;
    }

    /**
     * Starts the player stationary animation, directionally based.
     */
    private void stationaryAnimation() {
        if (!death) {
            String animationName;

            // Determine the animation name based on previous direction and player
            if (playerNum != PlayerNum.PLAYER_1 && playerNum != PlayerNum.BEAR) {
                animationName = (previousDirectionHorizontal == Direction.RIGHT) ? "Idle_right" : "Idle_left";
            } else {
                animationName = switch (previousDirection) {
                    case DOWN -> (playerNum == PlayerNum.BEAR) ? "idle_bottom" : "idle";
                    case LEFT -> (playerNum == PlayerNum.BEAR) ? "idle_left" : "idle-left";
                    case RIGHT -> (playerNum == PlayerNum.BEAR) ? "idle_right" : "idle-right";
                    case UP -> (playerNum == PlayerNum.BEAR) ? "idle_top" : "idle-up";
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
                case PLAYER_1 -> animationController.startAnimation("walk-left");
                case BEAR -> animationController.startAnimation("walk_left");
                case PLAYER_2, PLAYER_3, PLAYER_4 -> animationController.startAnimation("Walk_left");
            }
            previousDirectionHorizontal = Direction.LEFT;
            previousDirection = Direction.LEFT;
        }
    }

    /**
     * Starts the player walking right animation.
     */
    private void walkRight() {
        if (!death) {
            switch (playerNum) {
                case PLAYER_1 -> animationController.startAnimation("walk-right");
                case BEAR -> animationController.startAnimation("walk_right");
                case PLAYER_2, PLAYER_3, PLAYER_4 -> animationController.startAnimation("Walk_right");
            }
            previousDirection = Direction.RIGHT;
            previousDirectionHorizontal = Direction.RIGHT;
        }
    }

    /**
     * Starts the player walking down animation, or the left/right walking animations
     * for players without the vertical animations.
     */
    private void walkDown() {
        if (!death) {
            String animationName;

            // Determine the animation name based on previous direction and player
            animationName = switch (playerNum) {
                case PLAYER_1 -> "walk-down";
                case PLAYER_2, PLAYER_3, PLAYER_4 ->
                        (previousDirectionHorizontal == Direction.LEFT) ? "Walk_left" : "Walk_right";
                case BEAR -> "walk_bottom";
            };

            animationController.startAnimation(animationName);
            previousDirection = Direction.DOWN;
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
                case PLAYER_1 -> "walk-up";
                case PLAYER_2 , PLAYER_3, PLAYER_4 ->
                        (previousDirectionHorizontal == Direction.LEFT) ? "Walk_left" : "Walk_right";
                case BEAR -> "walk_top";
            };

            animationController.startAnimation(animationName);
            previousDirection = Direction.UP;
        }
    }

    /**
     * Starts the player death animation.
     */
    void deathAnimation() {
        String animationName;

        // Determine the animation name based on previous direction and player
        if (playerNum != PlayerNum.PLAYER_1 && playerNum != PlayerNum.BEAR) {
            animationName =
                    (previousDirectionHorizontal == Direction.RIGHT) ? "Dead_right" : "Dead_left";
        } else {
            switch (previousDirection) {
                case DOWN -> animationName =
                        (playerNum == PlayerNum.PLAYER_1) ? "death-down" : "death-left";
                case LEFT -> animationName =
                        (playerNum == PlayerNum.PLAYER_1) ? "death-left" : "death_left";
                case RIGHT -> animationName =
                        (playerNum == PlayerNum.PLAYER_1) ? "death-right" : "death_left";
                default -> animationName =
                        (playerNum == PlayerNum.PLAYER_1) ? "death-up" : "death-down";
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
                case PLAYER_1 -> animationName = "damage-down";
                case PLAYER_2 , PLAYER_3, PLAYER_4 -> animationName =
                        (previousDirectionHorizontal == Direction.RIGHT) ? "Hurt_right" : "Hurt_left";
                case BEAR -> animationName =
                        (previousDirectionHorizontal == Direction.RIGHT) ? "attack_right" : "attack_left";
                default -> animationName = "idle";
            }

            animationController.startAnimation(animationName);
        }
    }
}
