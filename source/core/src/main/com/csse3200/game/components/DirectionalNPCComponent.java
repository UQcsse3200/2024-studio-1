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

    public DirectionalNPCComponent(Boolean isDirectional){
        this.directional = isDirectional;
    }
    public Boolean isDirectable(){
        return this.directional;
    }
}
