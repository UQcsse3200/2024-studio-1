package com.csse3200.game.components.player;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
    private static final Logger log = LoggerFactory.getLogger(KeyboardPlayerInputComponent.class);
    private final Vector2 walkDirection = Vector2.Zero.cpy();
    private final Map<Integer, Function<Integer, Boolean>> downBindings = new HashMap<>();
    private final Map<Integer, Function<Integer, Boolean>> upBindings = new HashMap<>();

    public KeyboardPlayerInputComponent() {
        super(5);
        downBindings.put(Keys.W, (i) -> walk(Vector2Utils.UP));
        downBindings.put(Keys.A, (i) -> walk(Vector2Utils.LEFT));
        downBindings.put(Keys.S, (i) -> walk(Vector2Utils.DOWN));
        downBindings.put(Keys.D, (i) -> walk(Vector2Utils.RIGHT));

        upBindings.put(Keys.W, (i) -> unWalk(Vector2Utils.UP));
        upBindings.put(Keys.A, (i) -> unWalk(Vector2Utils.LEFT));
        upBindings.put(Keys.S, (i) -> unWalk(Vector2Utils.DOWN));
        upBindings.put(Keys.D, (i) -> unWalk(Vector2Utils.RIGHT));

        downBindings.put(Keys.UP, (i) -> shoot(Vector2Utils.UP));
        downBindings.put(Keys.LEFT, (i) -> shoot(Vector2Utils.LEFT));
        downBindings.put(Keys.DOWN, (i) -> shoot(Vector2Utils.DOWN));
        downBindings.put(Keys.RIGHT, (i) -> shoot(Vector2Utils.RIGHT));

        downBindings.put(Keys.SPACE, (i) -> melee());
    }

    private boolean walk(Vector2 direction) {
        walkDirection.add(direction);
        triggerWalkEvent();
        return true;
    }

    private boolean unWalk(Vector2 direction) {
        walkDirection.sub(direction);
        triggerWalkEvent();
        return true;
    }

    private boolean shoot(Vector2 direction) {
        entity.getEvents().trigger("shoot", direction);
        return true;
    }

    private boolean melee() {
        entity.getEvents().trigger("attack");
        return true;
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        if (!downBindings.containsKey(keycode)) {
            return false;
        }
        downBindings.get(keycode).apply(keycode);
        return true;
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyUp(int)
     */
    @Override
    public boolean keyUp(int keycode) {
        if (!upBindings.containsKey(keycode)) {
            return false;
        }
        upBindings.get(keycode).apply(keycode);
        return true;
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
