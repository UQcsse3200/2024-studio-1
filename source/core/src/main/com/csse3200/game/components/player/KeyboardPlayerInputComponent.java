package com.csse3200.game.components.player;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.areas.BossRoom;
import com.csse3200.game.areas.ShopRoom;
import com.csse3200.game.areas.EnemyRoom;
import com.csse3200.game.components.player.inventory.InventoryComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.input.InputComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Vector2 directionShooting = new Vector2(0, 0);
    // Timer and task for holding down a shoot button
    private RepeatShoot taskShoot;
    private RepeatMelee taskMelee;
    private static final int inputDelay = 15; // time between 'button held' method calls in milliseconds

    /**
     * TimerTask used to repeatedly shoot in a direction
     */
    private class RepeatShoot extends Timer.Task {
        private final Vector2 directionShooting;

        public RepeatShoot(Vector2 direction) {
            this.directionShooting = direction;
        }

        @Override
        public void run() {
            shoot(this.directionShooting);
        }
    }

    private class RepeatMelee extends Timer.Task {
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
     *
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
     *
     * @param direction The direction to shoot in
     */
    private boolean holdShoot(Vector2 direction) {
        this.directionShooting.add(direction);
        if (this.taskShoot != null) {
            this.taskShoot.cancel();
        }
        if (this.directionShooting.isZero()){
            this.taskShoot.cancel();
            return true;
        }
        Vector2 scaledVct = this.directionShooting.cpy().setLength(1);
        this.taskShoot = new RepeatShoot(scaledVct);
        Timer.schedule(taskShoot, inputDelay / 1000f, inputDelay / 1000f);
        return true;
    }

    private void shoot(Vector2 direction) {
        entity.getEvents().trigger("shoot", direction);
    }

    /**
     * Method used to stop calling the 'shoot' method
     * Called when the player releases the input to shoot (default is arrow keys)
     *
     * @param direction The vector2 direction of the arrow key that was released
     * @return true to indicate that the input has been handled
     */
    private boolean unShoot(Vector2 direction) {
        this.directionShooting.sub((direction));
        if (this.directionShooting.isZero()) {
            this.taskShoot.cancel();
        } else { // reshoot in different direction
            if (this.taskShoot != null) {
                this.taskShoot.cancel();
            }
            Vector2 scaledVct = this.directionShooting.cpy().setLength(1);
            this.taskShoot = new RepeatShoot(scaledVct);
            Timer.schedule(taskShoot, inputDelay / 1000f, inputDelay / 1000f);
        }
        return true;
    }

    private void melee() {
        entity.getEvents().trigger("attackMelee");
    }

    private boolean holdMelee() {
        if (this.taskMelee != null) {
            this.taskMelee.cancel();
        }
        this.taskMelee = new RepeatMelee();
        Timer.schedule(taskMelee, inputDelay / 1000f, inputDelay / 1000f);
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
            case 5 -> entity.getEvents().trigger("use5");
            case 6 -> entity.getEvents().trigger("use6");
            case 7 -> entity.getEvents().trigger("use7");
            case 8 -> entity.getEvents().trigger("useReroll");
        }
        return true;
    }

    /**
     * Handles the event when the key for 'pickup' is pressed
     *
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
        Entity player = ServiceLocator.getGameAreaService().getGameController().getPlayer();
        if (ServiceLocator.getGameAreaService().getGameController().getCurrentRoom() instanceof EnemyRoom room) {
            List<Entity> enemies = room.getEnemies();
            player.getComponent(InventoryComponent.class).getPets().forEach(p -> p.setAggro(enemies));
        }
        return true;
    }

    /**
     * Handles the event when the key for 'purchase' is pressed
     *
     * @return true
     */
    private boolean purchaseItem() {
        entity.getEvents().trigger("purchaseItem");
        return true;
    }

    /**
     *
     * @return true if the key binding is done or if the entity is already in the boss room
     */

    private boolean bossTeleport() {
        if (!(ServiceLocator.getGameAreaService().getGameController().getCurrentRoom() instanceof BossRoom bossRoom)) {
            entity.getEvents().trigger("teleportToBoss");
            // Already in boss room so just do nothing !!
        }
        return true;
    }

    private boolean shopTeleport() {
        if (!(ServiceLocator.getGameAreaService().getGameController().getCurrentRoom() instanceof ShopRoom shoproom)) {
            entity.getEvents().trigger("teleportToShop");
        }
        return true;
    }


    /*
     * All the player actions that need to respond to key down
     */
    private Map<KeyMapping.KeyBinding, Action> getDownActions() {
        Map<KeyMapping.KeyBinding, Action> actionMap = new HashMap<>();

        actionMap.put(WALK_UP, (i) -> walk(Vector2Utils.UP));
        actionMap.put(WALK_LEFT, (i) -> walk(Vector2Utils.LEFT));
        actionMap.put(WALK_DOWN, (i) -> walk(Vector2Utils.DOWN));
        actionMap.put(WALK_RIGHT, (i) -> walk(Vector2Utils.RIGHT));

        actionMap.put(SHOOT_UP, (i) -> holdShoot(Vector2Utils.UP));
        actionMap.put(SHOOT_LEFT, (i) -> holdShoot(Vector2Utils.LEFT));
        actionMap.put(SHOOT_RIGHT, (i) -> holdShoot(Vector2Utils.RIGHT));
        actionMap.put(SHOOT_DOWN, (i) -> holdShoot(Vector2Utils.DOWN));

        actionMap.put(MELEE, (i) -> holdMelee());

        actionMap.put(USE_1, (i) -> useItem(1));
        actionMap.put(USE_2, (i) -> useItem(2));
        actionMap.put(USE_3, (i) -> useItem(3));
        actionMap.put(USE_4, (i) -> useItem(4));
        actionMap.put(USE_5, (i) -> useItem(5));
        actionMap.put(USE_6, (i) -> useItem(6));
        actionMap.put(USE_7, (i) -> useItem(7));

        actionMap.put(ENTER_BOSS, (i) -> bossTeleport());
        actionMap.put(ENTER_SHOP, (i) -> shopTeleport());
        actionMap.put(PICK_UP, (i) -> pickupItem());
        actionMap.put(RE_ROLL, (i) -> useItem(8)); //Rerol here
        actionMap.put(PURCHASE_ITEM, (i) -> purchaseItem());
        actionMap.put(NECROMANCER_BINDING, (i) -> petTargetSwitch());
        return actionMap;
    }

    /*
     * All the player actions that need to respond to key up
     */
    private Map<KeyMapping.KeyBinding, Action> getUpActions() {
        Map<KeyMapping.KeyBinding, Action> actionMap = new HashMap<>();

        actionMap.put(WALK_UP, (i) -> unWalk(Vector2Utils.UP));
        actionMap.put(WALK_LEFT, (i) -> unWalk(Vector2Utils.LEFT));
        actionMap.put(WALK_DOWN, (i) -> unWalk(Vector2Utils.DOWN));
        actionMap.put(WALK_RIGHT, (i) -> unWalk(Vector2Utils.RIGHT));

        actionMap.put(SHOOT_UP, (i) -> unShoot(Vector2Utils.UP));
        actionMap.put(SHOOT_LEFT, (i) -> unShoot(Vector2Utils.LEFT));
        actionMap.put(SHOOT_RIGHT, (i) -> unShoot(Vector2Utils.RIGHT));
        actionMap.put(SHOOT_DOWN, (i) -> unShoot(Vector2Utils.DOWN));

        actionMap.put(MELEE, (i) -> unMelee());

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

    private boolean keyChange(Map<Integer, Action> bindings, int keycode) {
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
                case "LEFT" -> entity.getEvents().trigger("walkLeft");
                case "UP" -> entity.getEvents().trigger("walkUp");
                case "RIGHT" -> entity.getEvents().trigger("walkRight");
                case "DOWN" -> entity.getEvents().trigger("walkDown");
                case "NONE" -> {
                    // Handle no movement or default case
                }
            }
        }
    }
}
