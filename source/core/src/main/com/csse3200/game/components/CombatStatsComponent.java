package com.csse3200.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int health;
  private int speed;
  private int baseAttack;
  private int attackPerSecond;
  private int burnAttack;
  private int burnDuration;

  public CombatStatsComponent(int health,
                              int baseAttack,
                              int speed,
                              int attackPerSecond,
                              int burnAttack,
                              int burnDuration) {
    setHealth(health);
    setBaseAttack(baseAttack);
    setSpeed(speed);

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
   * Sets the entity's movement speed. Movement speed has a minimum bound of 0.
   *
   * @param speed Movement speed
   */
  public void setSpeed(int speed) {
    if (health >= 0) {
      this.health = health;
    } else {
      this.health = 0;
    }
  }

  /**
   * Sets the entity's attack per second.
   * Number of attack per second has a minimum bound of 0.
   *
   * @param attackPerSecond Number of attack per second
   */
  public void setAttackPerSecond(int attackPerSecond) {
    if (attackPerSecond >= 0) {
      this.attackPerSecond = attackPerSecond;
    } else {
      this.attackPerSecond = 0;
    }
  }

  /**
   * Sets the entity's burn damage.
   * Burn damage has a minimum bound of 0.
   *
   * @param burnAttack Burn damage per second
   */
  public void setBurnAttack(int burnAttack) {
    if (burnAttack >= 0) {
      this.burnAttack = burnAttack;
    } else {
      this.burnAttack = 0;
    }
  }

  /**
   * Sets the entity's burn duration.
   * Burn damage has a minimum bound of 0.
   *
   * @param burnDuration Burn duration (in second)
   */
  public void setBurnDuration(int burnDuration) {
    if (burnDuration >= 0) {
      this.burnDuration = burnDuration;
    } else {
      this.burnDuration = 0;
    }
  }

  public void hit(CombatStatsComponent attacker) {
    int newHealth = getHealth() - attacker.getBaseAttack();
    setHealth(newHealth);
  }
}
