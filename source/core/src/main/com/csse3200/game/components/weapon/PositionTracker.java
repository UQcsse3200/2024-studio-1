package com.csse3200.game.components.weapon;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.components.player.PlayerConfigComponent;
import com.csse3200.game.components.player.inventory.weapons.*;
import com.csse3200.game.entities.Entity;

import java.util.logging.Logger;

/**
 * Component to track the player's position and set it to the weapon entity.
 */
public class PositionTracker extends Component {

    protected static final Logger logger = Logger.getLogger(PositionTracker.class.getName());
    private Entity player; // The player entity

    private Vector2 offset; // The offset of the weapon entity from the player entity

    /**
     * Update the position of the weapon entity to the player's position.
     */
    @Override
    public void update() {
        // No action by default.
        if (player != null) {
            Vector2 updatedPosition = new Vector2(player.getPosition().x + offset.x,
                    player.getPosition().y + offset.y);
            this.getEntity().setPosition(updatedPosition);
        }
    }

    /**
     * Get the player entity
     *
     * @return the player entity
     */
    public Entity getPlayer() {
        return player;
    }

    /**
     * Set the player entity
     *
     * @param player the player entity
     */
    private void setPlayer(Entity player) {
        this.player = player;
    }

    /**
     * Connect this component to the player who is using the weapon to get the position
     *
     * @param player the player entity
     */
    public void connectPlayer(Entity player) {
        boolean isMelee = false;
        try {
            isMelee = getEntity().getComponent(NameComponent.class).getName().equals("Melee");
        } catch (NullPointerException e) {
            logger.warning("The position tracker is not connected to a weapon entity");
        }

        setPlayer(player);
        if (player == null || player.getComponent(NameComponent.class) == null) {
            offset = new Vector2(0, 0);
            return;
        }
        // Specify the offset for each character model
        switch (player.getComponent(PlayerConfigComponent.class).getPlayerConfig().name) {
            case "Player 2":
                offset = isMelee ? new Vector2(- 0.1f, - 0.3f) : new Vector2(0.2f, 0.5f);
                break;
            case "player 3":
                offset = isMelee ? new Vector2(- 0.1f, - 0.3f) : new Vector2(0.15f, 0.45f);
                break;
            case "player 4":
                offset = isMelee ? new Vector2(- 0.1f, - 0.3f) : new Vector2(0.2f, 0.5f);
                break;
            case "bear":
                offset = isMelee ? new Vector2(- 0.2f, - 0.2f) : new Vector2(0.2f, 0.35f);
                break;
            default:
                offset = isMelee ? new Vector2(-0.5f, -0.5f) : new Vector2(0.2f, 0f);
        }
    }

    /**
     * Remove player from this weapon
     */
    public void disconnectPlayer() {
        setPlayer(null);
    }

    /**
     * Get the copy of offset of the weapon entity from the player entity
     * @return the offset of the weapon entity from the player entity
     */
    public Vector2 getOffset() {
        Vector2 offset = new Vector2(this.offset);
        return offset;
    }
}
