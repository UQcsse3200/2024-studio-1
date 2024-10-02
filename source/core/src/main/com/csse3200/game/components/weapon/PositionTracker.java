package com.csse3200.game.components.weapon;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.entities.Entity;

/**
 * Component to track the player's position and set it to the weapon entity.
 */
public class PositionTracker extends Component {
    private Entity player; // The player entity

    private Vector2 offset; // The offset of the weapon entity from the player entity

    /**
     * Update the position of the weapon entity to the player's position.
     */
    @Override
    public void update() {
        // No action by default.
        if (player != null) {
            Vector2 updatedPosition = new Vector2(player.getPosition().x + offset.x, player.getPosition().y + offset.y);
            this.getEntity().setPosition(updatedPosition);
        }
    }

    /**
     * Get the player entity
     * @return the player entity
     */
    public Entity getPlayer() {
        return player;
    }

    /**
     * Set the player entity
     * @param player the player entity
     */
    private void setPlayer(Entity player) {
        this.player = player;
    }

    /**
     * Connect this component to the player who is using the weapon to get the position
     * @param player the player entity
     */
    public void connectPlayer(Entity player) {
        setPlayer(player);
        switch (player.getComponent(NameComponent.class).getName()) {
            case "Player":
                offset = new Vector2(1, 1);
                break;
            default:
                offset = new Vector2(0, 0);
        }
    }

    /**
     * Remove player from this weapon
     */
    public void disconnectPlayer() {
        setPlayer(null);
    }
}
