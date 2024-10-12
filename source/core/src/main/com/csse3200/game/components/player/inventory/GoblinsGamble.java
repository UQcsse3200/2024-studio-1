package com.csse3200.game.components.player.inventory;

import com.badlogic.gdx.graphics.Texture;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.utils.RandomNumberGenerator;

/**
 * GoblinsGamble is a class that represents a gamble item in the game.
 * When the player picks up this item, they have a chance to either gain or lose coins.
 * The outcome is determined by a random generator with a 40% chance of gaining coins
 * and a 60% chance of losing coins.
 */
public class GoblinsGamble extends BuffItem {

    private static final int WIN_AMOUNT = 8;
    private static final int LOSE_AMOUNT = 5;

    /**
     * Applies the effect of the gamble to the given entity.
     *
     * @param entity The entity on which the effect is applied.
     */
    @Override
    public void effect(Entity entity) {
        gamblePlayersCoins(entity);
    }

    /**
     * Performs the gamble action where the player has a 40% chance of gaining coins
     * and a 60% chance of losing coins. The coin count is updated accordingly in the
     * entity's CoinsComponent.
     *
     * @param entity The entity that is participating in the gamble.
     */
    public void gamblePlayersCoins(Entity entity) {
        CoinsComponent coins = entity.getComponent(CoinsComponent.class);
        int randomInt = generateGambleOutcome();
        if (randomInt == 1) {
            coins.addCoins(WIN_AMOUNT);
        } else {
            coins.spend(Math.min(coins.getCoins(), LOSE_AMOUNT));
        }
    }

    /**
     * Generates a random outcome for the gamble. The method uses a random number
     * generator to simulate a 40% chance of winning and a 60% chance of losing.
     *
     * @return 1 if the player wins the gamble, 2 if the player loses.
     */
    private int generateGambleOutcome() {
        RandomNumberGenerator random = ServiceLocator.getRandomService().getRandomNumberGenerator(GoblinsGamble.class);
        double roll = random.getRandomDouble(0.0, 1.0);
        return (roll <= 0.4) ? 1 : 2; // 40% chance to win
    }

    /**
     * Returns the buff specification as a string.
     *
     * @return The specification of the Goblin's Gamble item.
     */
    @Override
    public String getBuffSpecification() {
        return "goblinsgamble";
    }

    /**
     * Returns the name of the item.
     *
     * @return The name "Goblin's Gamble".
     */
    @Override
    public String getName() {
        return "Goblin's Gamble";
    }

    /**
     * Returns the icon representing the Goblin's Gamble item
     * @return a texture object for the goblin icon
     */
    @Override
    public Texture getIcon() {
        return new Texture("images/items/goblin_statue.png");
    }

    @Override
    public void drop(Inventory inventory) {

    }
}