package com.csse3200.game.components.player.inventory;

public abstract class MeleeWeapon implements Collectible {
    @Override
    public Type getType() {
        return Type.MELEE_WEAPON;
    }

    @Override
    public void pickup(Inventory inventory) {
        inventory.setMelee(this);
        inventory.getEntity().getEvents().addListener("melee", this::attack);

        // Add a Weapon Component
    }

    @Override
    public void drop(Inventory inventory) {
        inventory.resetMelee();
    }

    @Override
    public String getSpecification() {
        return "melee:" + getMeleeSpecification();
    }

    public abstract String getMeleeSpecification();

    /**
     * Swing this weapon
     */
    public abstract void attack();
}
