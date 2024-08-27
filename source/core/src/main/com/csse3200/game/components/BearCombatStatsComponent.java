package com.csse3200.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.csse3200.game.components.Direction;

/**
 * override
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class BearCombatStatsComponent extends CombatStatsComponent{
    private Direction direction;
    
    /**
     * @override
     */
    public BearCombatStatsComponent(int health, int baseAttack, Direction currentDirection) {
        super(health, baseAttack);
        this.direction = currentDirection;
        setHealth(health);
        setBaseAttack(baseAttack);
        setDirection(currentDirection);
    }

    /**
     * @override
     */
    public Direction getDirection(){
        return this.direction;
    }

    /**
     * @override
     */
    public void setDirection(Direction currentDirection){
        this.direction = currentDirection;
    }
}
