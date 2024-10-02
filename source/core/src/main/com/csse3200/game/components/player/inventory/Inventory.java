package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.utils.Array;
import com.csse3200.game.entities.Entity;
import java.util.List;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.components.npc.NPCConfigComponent;
import com.csse3200.game.components.npc.attack.MeleeAttackComponent;
import com.csse3200.game.components.tasks.ChargeTask;
import com.csse3200.game.components.tasks.ChaseTask;
import com.csse3200.game.entities.configs.NPCConfigs;
import com.csse3200.game.ai.tasks.AITaskComponent;

import java.util.Optional;

/**
 * A component intended to be used by the player to track their inventory.
 * <p>
 * Currently only stores the gold amount but can be extended for more advanced functionality such as storing items.
 * Can also be used as a more generic component for other entities.
 */
public class Inventory {
    private final InventoryComponent component;
    private final Array<Collectible> items = new Array<>();
    private Array<Entity> pets = new Array<>();

    private Optional<MeleeWeapon> meleeWeapon = Optional.empty();
    private Optional<RangedWeapon> rangedWeapon = Optional.empty();

    /**
     * Construct an inventory for an inventory component
     *
     * @param component the component this inventory is attached to.
     */
    public Inventory(InventoryComponent component) {
        super();
        this.component = component;
    }

    /**
     * Get the entity this inventory is attached to.
     *
     * @return the entity this Inventory is attached to.
     */
    public Entity getEntity() {
        return component.getEntity();
    }

    /**
     * Get the player's currently held melee weapon.
     *
     * @return the melee weapon currently held.
     */
    public Optional<MeleeWeapon> getMelee() {
        return meleeWeapon; // FIXME reset to default
    }

    /**
     * Set the player's currently held melee weapon.
     *
     * @param melee the melee weapon to pickup.
     */
    public void setMelee(MeleeWeapon melee) {
        resetMelee();
        this.meleeWeapon = Optional.of(melee);
    }

    /**
     * Reset the player's currently held melee weapon to the default.
     */
    public void resetMelee() {
        if (this.meleeWeapon.isEmpty()){
            return;
        }

        MeleeWeapon mw = this.meleeWeapon.get();
        this.meleeWeapon = Optional.empty();
        this.component.drop(mw);
    }

    /**
     * Get the player's currently held ranged weapon.
     *
     * @return the ranged weapon currently held.
     */
    public Optional<RangedWeapon> getRanged() {
        return rangedWeapon; // FIXME reset to default
    }

    /**
     * Set the player's currently held ranged weapon.
     *
     * @param ranged the ranged weapon to pickup.
     */
    public void setRanged(RangedWeapon ranged) {
        resetRanged();
        this.rangedWeapon = Optional.of(ranged);
    }

    /**
     * Reset the player's currently held ranged weapon to the default.
     */
    public void resetRanged() {
        if (this.rangedWeapon.isEmpty()){
            return;
        }

        RangedWeapon rw = this.rangedWeapon.get();
        this.rangedWeapon = Optional.empty();
        this.component.drop(rw);
    }

    /**
     * Get the current list of items.
     *
     * @return the current list of items
     */
    public Array<Collectible> getItems() {
        return new Array<>(items);
    }


    /**
     * Add to the list of items.
     *
     * @param item The item to add.
     */
    public void addItem(Collectible item) {
        this.items.add(item);
        getEntity().getEvents().trigger("addToInventory");
    }


    public void removeItem(Collectible item) {
        this.items.removeValue(item, true);
    }

    /**
     * Get the current list of pets.
     *
     * @return current list of pets 
     */
    public Array<Entity> getPets() {
        return new Array<>(pets);
    }

    /**
     * Get the current list of pets.
     *
     * @return current list of pets 
     */
    public boolean petsExist() {
        if(pets.size > 0){
            return true;
        }
        return false;
    }

    /**
     * Add to the list of pets.
     *
     * @param addPet the pet to be added 
     */
    public void addPet(Entity addPet ) {
        this.pets.add(addPet);
    }


    /**
     * Remove from the list of pets
     *
     * @param removePet the pet to be removed  
     */
    public void removePet(Entity removePet) {
        this.pets.removeValue(removePet, true);
    }

    /**
     * Set the target for the pet 
     *
     * @param targets that the pets spawn into a room to attack to  
     */
    public void initialisePetAggro(List<Entity> targets) {
        //For each pet, find the closest enemy and set it as its target  
        for(Entity pet:pets){
            Entity closestEnemy = getClosestEnemy(pet, targets);
            setPetTarget(pet,closestEnemy);
        }
    }
    /**
     * Set the target for the pet 
     *
     * @param targets for the pets 
     */
    public void setPetsAggro(List<Entity> targets) {
        //get the closest enemy to the player make all pets target it  
        Entity player = ServiceLocator.getGameAreaService().getGameArea().getPlayer(); 
        Entity closestEnemy = getClosestEnemy(player, targets);
        for(Entity pet:pets){
            setPetTarget(pet, closestEnemy);
        }
    }
    private void setPetTarget(Entity pet, Entity target){
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
        
    private Entity getClosestEnemy(Entity origin, List<Entity> targets){
        Entity closestEnemy = new Entity(); 
        double distance = 10000000.0;
        int originX = (int) origin.getPosition().x;
        int originY = (int) origin.getPosition().y;
        //make sure animal is alive
        for(Entity enemy: targets){
            int enemyX = (int) enemy.getPosition().x;
            int enemyY = (int) enemy.getPosition().y;
            double enemyDistance = Math.sqrt(Math.pow((originX- enemyX), 2) + Math.pow((originY- enemyY), 2));
            if(enemyDistance < distance){
                closestEnemy = enemy;
                distance = enemyDistance;
            }
        }  
        return closestEnemy;
    }
}
