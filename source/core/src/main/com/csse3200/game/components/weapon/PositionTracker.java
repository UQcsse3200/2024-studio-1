package com.csse3200.game.components.weapon;

import com.csse3200.game.components.Component;
import com.csse3200.game.entities.Entity;

/**
 * Component to track the player's position and set it to the weapon entity.
 */
public class PositionTracker extends Component {
    private Entity player;

    @Override
    public void update() {
        // No action by default.
        if (player != null) {
            this.getEntity().setPosition(this.player.getPosition());
        }
    }

    private void setPlayer(Entity player) {
        this.player = player;
    }

    /**
     * Connect this component to the player who is using the weapon to get the position
     * @param player the player entity
     */
    public void connectPlayer(Entity player) {
        setPlayer(player);
    }

    /**
     * Remove player from this weapon
     */
    public void disconnectPlayer() {
        setPlayer(null);
    }
}
