package com.csse3200.game.components.player.inventory;

import com.csse3200.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScoreComponent extends Component {

    private Inventory inventory;
    private int gold;
    private static final Logger logger = LoggerFactory.getLogger(InventoryComponent.class);

    public ScoreComponent(Inventory inventory) {
        super();
        this.inventory = inventory;
        this.gold = 0;
        inventory.getEntity().getEvents().addListener("collectCoin",this::addGold);
    }

    public int getEnemyAttack(String call) {
        String[] parts = call.split(":");
        return Integer.parseInt(parts[1]);
    }

    public int getGold() {
        return this.gold;
    }

    /**
     * Returns if the player has a certain amount of gold.
     * @param gold required amount of gold
     * @return player has greater than or equal to the required amount of gold
     */
    public Boolean hasGold(int gold) {
        return this.gold >= gold;
    }

    /**
     * Sets the player's gold. Gold has a minimum bound of 0.
     *
     * @param gold gold
     */
    public void setGold(int gold) {
        this.gold = Math.max(gold, 0);
    }

    /**
     * Adds to the player's gold. The amount added can be negative.
     */
    public void addGold() {
        setGold(this.gold + 3);
        logger.info("added gold, Total Gold is: " + getGold());
    }
}
