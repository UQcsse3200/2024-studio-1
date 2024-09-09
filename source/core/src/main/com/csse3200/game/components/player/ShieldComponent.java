package com.csse3200.game.components.player;

import com.csse3200.game.components.Component;

/**
 * This class represents a component of an entity that can have a shield activated or deactivated.
 * This component is used to manage the shield state (active or inactive) for entities that can
 * use shields.
 */
public class ShieldComponent extends Component {

    private boolean active = false;

    /**
     * Checks if the shield is active.
     * @return true if the shield is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Activates the shield. Sets the shield state to active.
     */
    public void activateShield() {
        active = true;
    }

    /**
     * Deactivates the shield. Sets the shield state to inactive.
     */
    public void deactivateShield() {
        active = false;
    }
}
