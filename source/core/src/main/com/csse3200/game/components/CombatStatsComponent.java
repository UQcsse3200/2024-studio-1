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
    private final int maxHealth;
    private int health;
    private int baseAttack;
    private int armor;
    private boolean isInvincible;
    private static final int timeInvincible = 150;
    private final Timer timer;
    private static int buffedAttack;

    public CombatStatsComponent(int health, int baseAttack, boolean canBeInvincible, int armor) {
        this.canBeInvincible = canBeInvincible;
        this.maxHealth = health;
        this.health = health;
        this.baseAttack = baseAttack;
        this.armor = armor;
        setHealth(health);
        setBaseAttack(baseAttack);
        setInvincible(false);
        timer = new Timer();
    }

    public CombatStatsComponent(int health, int baseAttack) {
        this(health, baseAttack, false, 0);
    }

    /**
     * A TimerTask class used to remove the entity's invincibility
     * 'timeInvincible' milliseconds after being hit
     */
    private class InvincibilityRemover extends TimerTask {
        @Override
        public void run() {
            setInvincible(false);
        }
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
        this.health = Math.max(health, 0);
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
        setHealth(this.health + health);
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

    /**

     * Increases the entities base Attack damage
     *
     * @param buffedAttack increased Damage
     */

    public void addAttack(int buffedAttack) {setBaseAttack(baseAttack + buffedAttack);}

    public void increaseArmor(int additionalArmor) {
        armor = Math.max(armor + additionalArmor, 100);
    }

    /**
     * Applies damage to the entity by reducing its health. If health drops to 0, triggers a "died" event.
     *
     * @param damage The amount of damage to apply to the entity.
     */
    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
        entity.getEvents().trigger("healthChanged", health);
        if (health == 0) {
            entity.getEvents().trigger("died");
        }
    }

    /**
     * Returns the entity's maximum health.
     *
     * @return maximum health
     */
    public int getMaxHealth() {
        return maxHealth;
    }

    /**
     * Handles a hit from another entity by reducing the entity's health based on the attacker's base attack value.
     *
     * @param attacker The CombatStatsComponent of the entity attacking this entity.
     */
    public void hit(CombatStatsComponent attacker) {
        if (isInvincible()) {
            return;
        }
     

        float damageReduction = armor / (armor + 233.33f); //max damage reduction is 30% based on max armor(100)
        int newHealth = getHealth() - (int)(attacker.getBaseAttack() * (1 - damageReduction));
        setHealth(newHealth);
        if (canBeInvincible){
            setInvincible(true);
            InvincibilityRemover task = new InvincibilityRemover();
            timer.schedule(task, timeInvincible);
        }
        if (!isInvincible()) {
            if (health <= 0) {
                entity.getEvents().trigger("died");
                entity.getEvents().trigger("checkAllDead");
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
    public boolean isInvincible() {
        return isInvincible;
    }
}
