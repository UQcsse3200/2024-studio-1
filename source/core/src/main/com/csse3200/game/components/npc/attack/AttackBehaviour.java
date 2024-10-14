package com.csse3200.game.components.npc.attack;

import com.csse3200.game.entities.Entity;

/**
 * Interface for attack behaviours for attack components to use.
 */
public interface AttackBehaviour {

    /**
     * Perform an attack on the target entity.
     */
    void performAttack();

    /**
     * Check if the attacker can attack the target.
     * @param attacker The entity performing the attack.
     * @param target The entity being attacked.
     * @return True if the attacker can attack the target, false otherwise.
     */
    boolean canAttack(Entity attacker, Entity target);
}