package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.AlertBoxService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.areas.BossRoom;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.MainGameArea;

/**
 * A component that manages the boss's health-based dialogue during a battle.
 * It triggers dialogue boxes at specific health thresholds and pauses the game.
 */
public class BossHealthDialogueComponent extends Component {
    private CombatStatsComponent combatStats;
    private final float[] healthThresholds = {0.75f, 0.5f, 0.25f};
    private int currentThresholdIndex = 0;

    /**
     * Initializes the component by getting the CombatStatsComponent from the entity.
     */
    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
    }

    /**
     * Updates the component every frame.
     * Checks if the boss's health has reached a new threshold and triggers dialogue if so.
     */
    @Override
    public void update() {
        if (currentThresholdIndex >= healthThresholds.length) {
            return; // All dialogues have been shown
        }

        float healthPercentage = (float) combatStats.getHealth() / combatStats.getMaxHealth();

        if (healthPercentage <= healthThresholds[currentThresholdIndex]) {
            if (healthThresholds[currentThresholdIndex] == 0.5f) {
                // At 50% health, spawn dog and snake
                spawnAdditionalEnemies();
            }
            showDialogueAndPause();
            currentThresholdIndex++;
        }
    }

    /**
     * Displays a dialogue box with the boss's message and pauses the game.
     * The game resumes when the player closes the dialogue box.
     */
    private void showDialogueAndPause() {
        MainGameScreen.isPaused = true;
        String message = getBossDialogue(currentThresholdIndex);

        ServiceLocator.getAlertBoxService().confirmDialogBox(
                entity,
                message,
                new AlertBoxService.ConfirmationListener() {
                    @Override
                    public void onYes() {
                        MainGameScreen.isPaused = false;
                    }

                    @Override
                    public void onNo() {
                        MainGameScreen.isPaused = false;
                    }
                }
        );
    }

    /**
     * Returns the appropriate boss dialogue based on the current health threshold.
     *
     * @param index The index of the current health threshold.
     * @return A string containing the boss's dialogue.
     */
    private String getBossDialogue(int index) {
        return switch (index) {
            case 0 -> "You've managed to hurt me. But this battle is far from over!";
            case 1 -> "Half of my strength is gone, but my resolve remains unbroken!";
            case 2 -> "I'm on my last legs, but I won't go down without a fight!";
            default -> "You'll never defeat me!";
        };
    }

    /**
     * Spawns additional enemies (dog and snake) near the boss when health reaches 50%.
     */
    private void spawnAdditionalEnemies() {
        GameAreaService gameAreaService = ServiceLocator.getGameAreaService();
        if (gameAreaService.getGameArea() != null) {
            MainGameArea area = (MainGameArea) gameAreaService.getGameArea();
            if (area.getCurrentRoom() instanceof BossRoom bossRoom) {
                bossRoom.spawnOtherAnimals(entity);
            }
        }
    }
}