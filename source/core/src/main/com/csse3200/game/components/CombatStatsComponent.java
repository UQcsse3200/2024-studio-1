package com.csse3200.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

    private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
    private final boolean canBeInvincible;
    private int health;
    private int baseAttack;
    private boolean isInvincible;
    private static final int timeInvincible = 2000;
    private final Timer timer;

    /**
     * A TimerTask class used to remove the entity's invincibility
     * 'timeInvincibile' milliseconds after being hit
     */
    private class removeInvincible extends TimerTask {
        @Override
        public void run() {
            setInvincible(false);
        }
    }

    public CombatStatsComponent(int health, int baseAttack, boolean canBeInvincible) {
        this.canBeInvincible = canBeInvincible;
        setHealth(health);
        setBaseAttack(baseAttack);
        setInvincible(false);
        timer = new Timer();
    }

    public CombatStatsComponent(int health, int baseAttack) {
        this(health, baseAttack, false);
    }

    /**
     * Returns true if the entity's has 0 health, otherwise false.
     *
     * @return is player dead
     */
    public Boolean isDead() {
        return health == 0;
    }

    /**
     * Returns the entity's health.
     *
     * @return entity's health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Sets the entity's health. Health has a minimum bound of 0.
     *
     * @param health health
     */
    public void setHealth(int health) {
        if (health >= 0) {
            this.health = health;
        } else {
            this.health = 0;
        }
        if (entity != null) {
            entity.getEvents().trigger("updateHealth", this.health);
        }
    }

    /**
     * Adds to the player's health. The amount added can be negative.
     *
     * @param health health to add
     */
    public void addHealth(int health) {
        int newHealth = this.health + health;

        if (newHealth >= 100) {
            newHealth = 100;
        }

        setHealth(newHealth);
    }

    /**
     * Returns the entity's base attack damage.
     *
     * @return base attack damage
     */
    public int getBaseAttack() {
        return baseAttack;
    }

    /**
     * Sets the entity's attack damage. Attack damage has a minimum bound of 0.
     *
     * @param attack Attack damage
     */
    public void setBaseAttack(int attack) {
        if (attack >= 0) {
            this.baseAttack = attack;
        } else {
            logger.error("Can not set base attack to a negative attack value");
        }
    }

    public void hit(CombatStatsComponent attacker) {
        if (!getIsInvincible()) {
            int newHealth = getHealth() - attacker.getBaseAttack();
            setHealth(newHealth);
            if (canBeInvincible){
              setInvincible(true);
              CombatStatsComponent.removeInvincible task = new CombatStatsComponent.removeInvincible();
              timer.schedule(task, timeInvincible);
            }
        }
    }

    /**
     * Sets the state of the entity's invincibility
     *
     * @param invincible invincibility state
     */
    public void setInvincible(boolean invincible) {
          isInvincible = invincible;
    }

    /**
     * returns the state of the entity's invincibility
     *
     * @return invincibility state
     */
    public boolean getIsInvincible() {
        return isInvincible;
    }
}


