package com.csse3200.game.components.player;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.csse3200.game.components.player.KeyMapping.KeyBinding.*;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
    private static final Logger log = LoggerFactory.getLogger(KeyboardPlayerInputComponent.class);
    private final Vector2 walkDirection = Vector2.Zero.cpy();
    private final Map<Integer, Action> downBindings;
    private final Map<Integer, Action> upBindings;
    private Vector2 directionShooting = null;
    // Timer and task for holding down a shoot button
    private final Timer holdShoot = new Timer();
    private RepeatShoot taskShoot;
    private final Timer holdMelee = new Timer();
    private RepeatMelee taskMelee;
    private static final int inputDelay = 15; // time between 'button held' method calls in milliseconds

    /**
     * TimerTask used to repeatedly shoot in a direction
     */
    private class RepeatShoot extends TimerTask{
        private final Vector2 directionShooting;
        public RepeatShoot(Vector2 direction){
            this.directionShooting = direction;
        }
        @Override
        public void run() {
            shoot(this.directionShooting);
        }
    }

    private class RepeatMelee extends TimerTask{
        @Override
        public void run() {
            melee();
        }
    }

    /**
     * Something the player does.
     */
    private interface Action {
        boolean act(int key);
    }

    /**
     * Create a new KeyboardPlayerInputComponent with specified keyBindings
     * @param keyMapping a mapping of the keys to their actions.
     */
    public KeyboardPlayerInputComponent(KeyMapping keyMapping) {
        super(5);
        this.upBindings = bindKeys(keyMapping, getUpActions());
        this.downBindings = bindKeys(keyMapping, getDownActions());
    }

    /**
     * Create a new KeyboardPlayerInputComponent with default keybindings.
     */
    public KeyboardPlayerInputComponent() {
        this(new KeyMapping());
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

    /**
     * Method used to setup the timer to hold the shoot button
     * triggered whenever the player inputs to shoot (default is arrow keys)
     * @param direction The direction to shoot in
     */
    private boolean holdShoot(Vector2 direction){
        if (this.taskShoot != null) {
            this.taskShoot.cancel();
        }
        this.directionShooting = direction;
        this.taskShoot = new RepeatShoot(direction);
        this.holdShoot.scheduleAtFixedRate(taskShoot, inputDelay, inputDelay);
        return true;
    }

    private boolean shoot(Vector2 direction) {
        entity.getEvents().trigger("shoot", direction);
        return true;
    }

    /**
     * Method used to stop calling the 'shoot' method
     * Called when the player releases the input to shoot (default is arrow keys)
     * @param direction
     * @return (not sure why this needs to return)
     */
    private boolean unShoot(Vector2 direction) {
        if (this.directionShooting == direction){
            this.taskShoot.cancel();
            this.directionShooting = null;
        }
        return true;
    }

    private boolean melee() {
        entity.getEvents().trigger("attackMelee");
        return true;
    }

    private boolean holdMelee(){
        if (this.taskMelee != null) {
            this.taskMelee.cancel();
        }
        this.taskMelee = new RepeatMelee();
        this.holdMelee.scheduleAtFixedRate(taskMelee, inputDelay, inputDelay);
        return true;
    }

    private boolean unMelee() {
        if (this.taskMelee != null) {
            this.taskMelee.cancel();
        }
        return true;
    }


    private boolean useItem(Integer num) {
        switch (num) {
            case 1 -> entity.getEvents().trigger("use1");
            case 2 -> entity.getEvents().trigger("use2");
            case 3 -> entity.getEvents().trigger("use3");
            case 4 -> entity.getEvents().trigger("use4");
            case 5 -> entity.getEvents().trigger("useReroll");
        }
        return true;
    }

    /**
     * Handles the event when the key for 'pickup' is pressed
     * @return true
     */
    private boolean pickupItem() {
        entity.getEvents().trigger("pickup");
        return true;
    }

    /**
     * Switches all the pets for the necromancer to target their closest Entity
     * @return true
     */
    private boolean petTargetSwitch() {
        return true;
    }

    /**
     * Handles the event when the key for 'purchase' is pressed
     * @return true
     */
    private boolean purchaseItem() {
        entity.getEvents().trigger("purchaseItem");
        return true;
    }

    private boolean bossTeleport() {
        entity.getEvents().trigger("teleportToBoss");
        return true;
    }


    /*
     * All the player actions that need to respond to key down
     */
    private Map<KeyMapping.KeyBinding, Action> getDownActions(){
        Map<KeyMapping.KeyBinding, Action> actionMap = new HashMap<>();

        actionMap.put(WALK_UP,  (i) -> walk(Vector2Utils.UP));
        actionMap.put(WALK_LEFT,  (i) -> walk(Vector2Utils.LEFT));
        actionMap.put(WALK_DOWN,  (i) -> walk(Vector2Utils.DOWN));
        actionMap.put(WALK_RIGHT,  (i) -> walk(Vector2Utils.RIGHT));

        actionMap.put(SHOOT_UP,  (i) -> holdShoot(Vector2Utils.UP));
        actionMap.put(SHOOT_LEFT,  (i) -> holdShoot(Vector2Utils.LEFT));
        actionMap.put(SHOOT_RIGHT,  (i) -> holdShoot(Vector2Utils.RIGHT));
        actionMap.put(SHOOT_DOWN,  (i) -> holdShoot(Vector2Utils.DOWN));

        actionMap.put(MELEE,  (i) -> holdMelee());

        actionMap.put(USE_1, (i) -> useItem(1));
        actionMap.put(USE_2, (i) -> useItem(2));
        actionMap.put(USE_3, (i) -> useItem(3));
        actionMap.put(USE_4, (i) -> useItem(4));

        actionMap.put(ENTER_BOSS, (i) -> bossTeleport());

        actionMap.put(PICK_UP, (i) -> pickupItem());
        actionMap.put(RE_ROLL, (i) -> useItem(5)); //Rerol here
        actionMap.put(PURCHASE_ITEM, (i) -> purchaseItem());
        actionMap.put(NECROMANCER_BINDING, (i) -> petTargetSwitch());
        return actionMap;
    }

    /*
     * All the player actions that need to respond to key up
     */
    private Map<KeyMapping.KeyBinding, Action> getUpActions(){
        Map<KeyMapping.KeyBinding, Action> actionMap = new HashMap<>();

        actionMap.put(WALK_UP,  (i) -> unWalk(Vector2Utils.UP));
        actionMap.put(WALK_LEFT,  (i) -> unWalk(Vector2Utils.LEFT));
        actionMap.put(WALK_DOWN,  (i) -> unWalk(Vector2Utils.DOWN));
        actionMap.put(WALK_RIGHT,  (i) -> unWalk(Vector2Utils.RIGHT));

        actionMap.put(SHOOT_UP,  (i) -> unShoot(Vector2Utils.UP));
        actionMap.put(SHOOT_LEFT,  (i) -> unShoot(Vector2Utils.LEFT));
        actionMap.put(SHOOT_RIGHT,  (i) -> unShoot(Vector2Utils.RIGHT));
        actionMap.put(SHOOT_DOWN,  (i) -> unShoot(Vector2Utils.DOWN));

        actionMap.put(MELEE,  (i) -> unMelee());

        return actionMap;
    }

    /*
     * Merge the keyMapping with the list of actions to produce a final key binding.
     */
    private static Map<Integer, Action> bindKeys(
            KeyMapping keyMapping,
            Map<KeyMapping.KeyBinding, Action> actions) {
        Map<Integer, Action> bindings = new HashMap<>();
        for (var entry : keyMapping.getKeyMap().entrySet()) {
            if (actions.containsKey(entry.getValue())) {
                bindings.put(entry.getKey(), actions.get(entry.getValue()));
            }
        }
        return bindings;
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

    private boolean keyChange(Map<Integer, Action> bindings, int keycode){
        if (!bindings.containsKey(keycode)) {
            return false;
        }
        return bindings.get(keycode).act(keycode);
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyDown(int)
     */
    @Override
    public boolean keyDown(int keycode) {
        return keyChange(downBindings, keycode);
    }

    /**
     * Triggers player events on specific keycodes.
     *
     * @return whether the input was processed
     * @see InputProcessor#keyUp(int)
     */
    @Override
    public boolean keyUp(int keycode) {
        return keyChange(upBindings, keycode);
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
}
