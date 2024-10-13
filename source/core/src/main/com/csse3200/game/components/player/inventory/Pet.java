package com.csse3200.game.components.player.inventory;

import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.npc.NPCConfigComponent;
import com.csse3200.game.components.npc.attack.MeleeAttackComponent;
import com.csse3200.game.components.tasks.ChargeTask;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.services.ServiceLocator;

import java.util.List;

/**
 * A kind of collectible that grants a pet, a follower who will assist you in combat.
 */
public abstract class Pet implements Collectible {
    protected Entity delegate;

    @Override
    public Type getType() {
        return Type.PET;
    }

    @Override
    public String getSpecification() {
        return "pet:" + getPetSpecification();
    }

    @Override
    public void pickup(Inventory inventory) {
        inventory.getContainer(Pet.class).add(this);
        this.delegate = spawn(inventory.getEntity());
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.getContainer(Pet.class).remove(this);
        ServiceLocator.getEntityService().markEntityForRemoval(this.delegate);
        this.delegate = null;
    }

    /**
     * Get the specification of this pet.
     *
     * @return the specification.
     */
    protected abstract String getPetSpecification();

    /**
     * Spawn the pet to assist a player in combat.
     *
     * @param entity the owner of the new pet to spawn.
     * @return the new pet.
     */
    protected abstract Entity spawn(Entity entity);

    /**
     * Set the target for the pet
     *
     * @param targets that the pets spawn into a room to attack to
     */
    public void initAggro(List<Entity> targets) {
        if (delegate == null) {
            return;
        }
        setPetTarget(delegate, getClosestEnemy(delegate, targets));
    }

    /**
     * Set the target for the pet
     *
     * @param targets for the pets
     */
    public void setAggro(List<Entity> targets) {
        Entity player = ServiceLocator.getGameAreaService().getGameArea().getPlayer();
        Entity closestEnemy = getClosestEnemy(player, targets);
        setPetTarget(delegate, closestEnemy);
    }

    private void setPetTarget(Entity pet, Entity target) {
        NPCConfigs.NPCConfig config = pet.getComponent(NPCConfigComponent.class).config;
        NPCConfigs.NPCConfig.TaskConfig tasks = config.tasks;

        MeleeAttackComponent meleeAttack = pet.getComponent(MeleeAttackComponent.class);

        // Add chase task
        if (tasks.chase != null) {
            pet.getComponent(AITaskComponent.class).addTask(new ChaseTask(target, tasks.chase));
        }
        // Add charge task
        if (tasks.charge != null) {
            pet.getComponent(AITaskComponent.class).addTask(new ChargeTask(target, tasks.charge));
        }

        if (meleeAttack != null) {
            meleeAttack.updateTarget(target);
        }
        pet.getComponent(AITaskComponent.class).update();
    }

    private Entity getClosestEnemy(Entity origin, List<Entity> targets) {
        Entity closestEnemy = new Entity();
        double distance = 10000000.0;
        //make sure animal is alive
        for (Entity enemy : targets) {

            double enemyDistance = origin.getPosition().sub(enemy.getPosition()).len();
            if (enemyDistance < distance) {
                closestEnemy = enemy;
                distance = enemyDistance;
            }
        }
        return closestEnemy;
    }
}
