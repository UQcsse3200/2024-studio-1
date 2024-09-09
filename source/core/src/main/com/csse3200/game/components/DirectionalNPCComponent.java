package com.csse3200.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Component to check if the sprites follow a direction or
 * are just undirected
 */
public class DirectionalNPCComponent extends Component {
    private final Boolean directional;
    private String direction;
    private static final Logger logger = LoggerFactory.getLogger(DirectionalNPCComponent.class);


    public DirectionalNPCComponent(Boolean isDirectional){
        this.directional = isDirectional;
        this.direction = "left";
    }
    public Boolean isDirectable(){
        return this.directional;
    }

    public void setDirection(String direction) {
        this.direction = direction;
        logger.debug("Direction set to {}", direction);
    }

    public String getDirection() {
        return this.direction;
    }
}
