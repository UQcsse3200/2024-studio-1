package com.csse3200.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Component for an NPC that can be directed to face a certain direction.
 */
public class DirectionalNPCComponent extends Component {
    private final Boolean directional;
    private String direction;
    private static final Logger logger = LoggerFactory.getLogger(DirectionalNPCComponent.class);

    /**
     * Create a new DirectionalNPCComponent.
     * @param isDirectional Whether the NPC animations are directable.
     */
    public DirectionalNPCComponent(Boolean isDirectional){
        if (isDirectional == null) {
            this.directional = false;
        } else {
            this.directional = isDirectional;
            this.direction = "left";
        }
    }

    /**
     * Get the directional flag.
     * @return Whether the NPC animations are directable.
     */
    public Boolean isDirectable(){
        return this.directional;
    }

    /**
     * Set the direction of the NPC.
     * @param direction The direction to face - "left" or "right".
     */
    public void setDirection(String direction) {
        this.direction = direction;
        logger.debug("Direction set to {}", direction);
    }

    /**
     * Get the direction of the NPC.
     * @return The direction the NPC is facing.
     */
    public String getDirection() {
        return this.direction;
    }
}
