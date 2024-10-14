package com.csse3200.game.components;

import com.csse3200.game.entities.Entity;

import com.csse3200.game.components.player.ShieldComponent;

import com.csse3200.game.files.FileLoader;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.RandomNumberGenerator;
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
    private int maxHealth;
    private int health;
    private int baseAttack;
    private int armor;
    private int buff;
    private boolean critAbility;
    private double critChance;
    private boolean canCauseBleed;
    private int bleedDamage;
    private boolean isInvincible;
    // change requested by character team
    private static final int timeInvincible = 2000;
    private final Timer timerIFrames; 
    private static final int timeFlash = 250;
    private final Timer timerFlashSprite;
    private CombatStatsComponent.flashSprite flashTask;

    private String lastAttackName;
    private String filePath = "configs/LastAttack.json";

    public CombatStatsComponent(int health, int maxHealth, int baseAttack, boolean canBeInvincible, int armor, int buff, boolean canCrit, double critChance) {
        this.canBeInvincible = canBeInvincible;
        this.maxHealth = health;
        this.health = health;
        this.baseAttack = baseAttack;
        this.armor = armor;
        this.buff = buff;
        this.critAbility = canCrit;
        this.critChance = critChance;
        this.canCauseBleed = false;
        this.bleedDamage = 0;
        setHealth(health);
        setBaseAttack(baseAttack);
        setInvincible(false);
        this.timerIFrames = new Timer();
        this.timerFlashSprite = new Timer();
    }

    public CombatStatsComponent(int health, int baseAttack, boolean canBeInvincible, int armor, int buff) {
        this.canBeInvincible = canBeInvincible;
        this.maxHealth = health;
        this.health = health;
        this.baseAttack = baseAttack;
        this.armor = armor;
        this.buff = buff;
        this.critAbility = false;
        this.critChance = 0.0;
        this.canCauseBleed = false;
        this.bleedDamage = 0;
        setHealth(health);
        setBaseAttack(baseAttack);
        setInvincible(false);
        this.timerIFrames = new Timer();
        this.timerFlashSprite = new Timer();
    }

    public CombatStatsComponent(int health, int baseAttack, boolean neverDies){
        this(health, baseAttack, false, 0, 0);
        setInvincible(neverDies);
    }

    public CombatStatsComponent(int health, int baseAttack) {
        this(health, baseAttack, false, 0, 0);
    }

    /**
     * A TimerTask class used to remove the entity's invincibility
     * 'timeInvincible' milliseconds after being hit
     */
    private class InvincibilityRemover extends TimerTask {
        @Override
        public void run() {
            flashTask.cancel();
            setInvincible(false);
            entity.getComponent(AnimationRenderComponent.class).setOpacity(1f);
        }
    }

    /**
     * A TimerTask used to alternate the visibility of the entity during their IFrames
     */
    private class flashSprite extends TimerTask {
        private boolean invisible = false;
        @Override
        public void run() {
            if (this.invisible){
                entity.getComponent(AnimationRenderComponent.class).setOpacity(0);
            } else {
                entity.getComponent(AnimationRenderComponent.class).setOpacity(1f);
            }
            this.invisible = !this.invisible;
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
    public void addAttack(int buffedAttack) {
        buff = (buff + buffedAttack);
    }

    /**
     * gets the total extra damage from buff
     * @return buff value
     */
    public int getDamageBuff() {
        return buff;
    }

    /**
     * gets max damage cap
     * @return int of maximum damage
     */
    public int getMaxDamage() {
        return 200;
    }

    /**
     * Set the damage buff of the entity
     * @param damage the new buff damage
     */
    public void setBuff(int damage) {
        this.buff = damage;
    }

    /**
     * increases total armor to reduce additional damage
     * @param additionalArmor increases total armor
     */
    public void increaseArmor(int additionalArmor) {
        armor = Math.min(armor + additionalArmor, 100);
    }

    /**
     * Gets the current armor of the entity
     * @return the entities armor value
     */
    public int getArmor() {
        return armor;
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
     * Sets the entity's maximum health
     *
     * @param newMaxHealth updated maximum health
     */
    public void setMaxHealth(int newMaxHealth) {
        if (newMaxHealth > 0){
            this.maxHealth = newMaxHealth;
        }
    }


    /**
     * Handles a hit from another entity by reducing the entity's health based on the attacker's base attack value.
     * Gives them invincibility frames if they can have any
     *
     * @param attacker The CombatStatsComponent of the entity attacking this entity.
     */
    public void hit(CombatStatsComponent attacker) {

        if (getIsInvincible()) {
            return;
        }
        if (isDead()){
            return;
        }
        ShieldComponent shield = entity.getComponent(ShieldComponent.class);
        if (shield != null && shield.isActive()) {
            entity.getEvents().trigger("hit");
            return;
        }

        if (getCanBeInvincible()) { // Only player currently
            float damageReduction = armor / (armor + 233.33f); //max damage reduction is 30% based on max armor(100)
            int newHealth = getHealth() - (int) ((attacker.getBaseAttack() + attacker.buff) * (1 - damageReduction));
            setHealth(newHealth);

            if (attacker.getEntity() == null
                    || attacker.getEntity().getName().equals("Unknown Entity")) {
                lastAttackName = "Unknown";
            }
            else {
                lastAttackName = attacker.getEntity().getName();
            }

            FileLoader.writeClass(lastAttackName, filePath, FileLoader.Location.EXTERNAL);
            //ServiceLocator.getResourceService().playSound("sounds/gethit.ogg");
            //ServiceLocator.getResourceService().playSound("sounds/hit2.ogg");
            //ServiceLocator.getResourceService().playSound("sounds/hit3.ogg");
            entity.getEvents().trigger("playerHit");
            if (isDead()){ return; }
            setInvincible(true);
            InvincibilityRemover task = new InvincibilityRemover();
            timerIFrames.schedule(task, timeInvincible);
            flashTask = new CombatStatsComponent.flashSprite();
            timerFlashSprite.scheduleAtFixedRate(flashTask, 0, timeFlash);
        } else {
            Entity player = ServiceLocator.getGameAreaService().getGameArea().getPlayer();
            int damage;
            if (player != null) {
                CombatStatsComponent playerStats = player.getComponent(CombatStatsComponent.class);
                damage = attacker.getBaseAttack() + playerStats.buff;
                if (playerStats.getCanCrit()) {
                    damage = applyCrit(damage, playerStats.getCritChance());
                }
                if(playerStats.getCanCauseBleed()) {
                    triggerBleedEffect(entity, playerStats.getBleedDamage());
                }
            } else {
                damage = attacker.getBaseAttack();
            }
            int newHealth = getHealth() - damage;
            setHealth(newHealth);
            //add animationcontroller
            if (health <= 0) {
                entity.getEvents().trigger("death");
                entity.getEvents().trigger("died");
                entity.getEvents().trigger("checkAnimalsDead");
                entity.getEvents().trigger("dummyDestroyed");
            }
        }
    }


    /**
     *Returns if the entity can be invincible
     *
     * @return boolean can be Invincible
     */
    public boolean getCanBeInvincible() {
        return this.canBeInvincible;
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

    /**
     * Returns a boolean value based on if the entity can crit or not
     * @return true if the entity can crit, false otherwise
     */
    public boolean getCanCrit() {
        return critAbility;
    }

    /**
     * Returns the entities crit chance
     * @return the crit chance value
     */
    public double getCritChance() {
        return critChance;
    }

    /**
     * Update the entity's ability to perform critical hits
     */
    public void updateCritAbility() {
        this.critAbility = true;
    }

    /**
     * Update the critChance of the entity
     */
    public void updateCritChance(double critValue) {
        this.critChance = Math.min(1.0, this.critChance + critValue);
    }

    /**
     * Apply critical hit based on chance
     * @return the modified damage
     */
    public int applyCrit(int damage, double critChance) {
        int newDamage = damage;
        RandomNumberGenerator rng = ServiceLocator.getRandomService().getRandomNumberGenerator(CombatStatsComponent.class);
        double randomDouble = rng.getRandomDouble(0.0, 1.0);
        if (randomDouble <= critChance) {
            newDamage *= 2;
        }
        return newDamage;
    }

    public boolean getCanCauseBleed() {
        return canCauseBleed;
    }
    public int getBleedDamage() {
        return bleedDamage;
    }

    public void updateBleedStatus() {
        this.canCauseBleed = true;
    }

    public void updateBleedDamage(int bleedDamage) {
        this.bleedDamage += bleedDamage;
    }

    /**
     * Applies the bleed effect to the enemy. Reduces 5% of the enemy's health per
     * second for 5 seconds. The damage stops if the enemy's health reaches 0.
     *
     * @param enemy The enemy entity to apply the bleed effect to.
     */
    private void triggerBleedEffect(Entity enemy, int bleedDamage) {
        CombatStatsComponent enemyStats = enemy.getComponent(CombatStatsComponent.class);

        if (enemyStats != null) {
            // Schedule bleed damage over 5 seconds
            com.badlogic.gdx.utils.Timer.schedule(new com.badlogic.gdx.utils.Timer.Task() {
                @Override
                public void run() {
                    if (enemyStats.getHealth() > 0) {
                        float bleedAmount = enemyStats.getHealth() * (bleedDamage / 100f);
                        enemyStats.setHealth((int) (enemyStats.getHealth() - bleedAmount));
                    } else {
                        // Stop applying bleed effect if the enemy is dead
                        cancel();
                    }
                }
            }, 1, 1, 3); // Reduce health for 3 seconds
        }
    }
}
