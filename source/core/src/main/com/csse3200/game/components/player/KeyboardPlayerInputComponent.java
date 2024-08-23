package com.csse3200.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
    private final Vector2 walkDirection = Vector2.Zero.cpy();

    public KeyboardPlayerInputComponent() {
        super(5);
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.W:
                walkDirection.add(Vector2Utils.UP);
                triggerWalkEvent();
                return true;
            case Keys.A:
                walkDirection.add(Vector2Utils.LEFT);
                triggerWalkEvent();
                return true;
            case Keys.S:
                walkDirection.add(Vector2Utils.DOWN);
                triggerWalkEvent();
                return true;
            case Keys.D:
                walkDirection.add(Vector2Utils.RIGHT);
                triggerWalkEvent();
                return true;
            case Keys.SPACE:
                entity.getEvents().trigger("attack");
                return true;
            default:
                return false;
        }
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyUp(int)
     */
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Keys.W:
                walkDirection.sub(Vector2Utils.UP);
                triggerWalkEvent();
                return true;
            case Keys.A:
                walkDirection.sub(Vector2Utils.LEFT);
                triggerWalkEvent();
                return true;
            case Keys.S:
                walkDirection.sub(Vector2Utils.DOWN);
                triggerWalkEvent();
                return true;
            case Keys.D:
                walkDirection.sub(Vector2Utils.RIGHT);
                triggerWalkEvent();
                return true;
            default:
                return false;
        }
    }

  /**
   * Triggers specific player walk events
   * based on the current direction.
   */
  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
      String direction = getDirection(walkDirection);
      switch (direction) {
        case "LEFT":
          entity.getEvents().trigger("walkLeft");
          break;
        case "UP":
          entity.getEvents().trigger("walkUp");
          break;
        case "RIGHT":
          entity.getEvents().trigger("walkRight");
          break;
        case "DOWN":
          entity.getEvents().trigger("walkDown");
          break;
        case "NONE":
          // Handle no movement or default case
          break;
      }
    }
  }

  /**
   * Takes in a Vector2 direction and processes the string eqivalent.
   *
   * @return The direction as a simplified string.
   */
  private static String getDirection(Vector2 vector) {
      if (vector.epsilonEquals(Vector2Utils.LEFT)) return "LEFT";
      if (vector.epsilonEquals(Vector2Utils.RIGHT)) return "RIGHT";
      if (vector.epsilonEquals(Vector2Utils.UP)) return "UP";
      if (vector.epsilonEquals(Vector2Utils.DOWN)) return "DOWN";
      return "NONE";
  }
}
