package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.components.player.PlayerConfigComponent;
import com.csse3200.game.options.GameOptions;
import com.csse3200.game.services.AlertBoxService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.BossRoom;

import java.security.Provider;
import java.util.Random;
import com.csse3200.game.areas.GameController;

/**
 * Component responsible for managing boss dialogue and behavior based on health thresholds.
 * This component triggers dialogue and spawns additional enemies at specific health percentages.
 */
public class BossHealthDialogueComponent extends Component {
    private final float[] healthThresholds = {0.75f, 0.5f, 0.25f};
    private final Random random = new Random();
    private CombatStatsComponent combatStats;
    private int currentThresholdIndex = 0;

    /**
     * Initializes the component by getting the CombatStatsComponent of the entity.
     */
    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
    }

    /**
     * Updates the component's state. Checks if the boss's health has reached a new threshold
     * and triggers appropriate actions if so.
     */
    @Override
    public void update() {
        if (currentThresholdIndex >= healthThresholds.length) {
            return;
        }

        float healthPercentage = (float) combatStats.getHealth() / combatStats.getMaxHealth();

        if (healthPercentage <= healthThresholds[currentThresholdIndex]) {
            if (healthThresholds[currentThresholdIndex] == 0.5f) {
                spawnRandomAdditionalEnemies();
            }
            showRandomDialogueAndPause();
            currentThresholdIndex++;
        }
    }

    /**
     * Displays a random dialogue message and pauses the game.
     * The game resumes when the player confirms the dialogue.
     */
    private void showRandomDialogueAndPause() {
        MainGameScreen.isPaused = true;
        String message = getRandomBossDialogue(currentThresholdIndex);

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
     * Retrieves a random dialogue message based on the current health threshold.
     *
     * @param index The index of the current health threshold.
     * @return A randomly selected dialogue message.
     */
    private String getRandomBossDialogue(int index) {
        String[][] dialogueOptions = {
                {
                        "Your looks hurt more than your attacks.",
                        "If I had a nickel for every time you missed… I'd have a lot of nickels.",
                        "You fight like someone who reads the tutorial.",
                        "This is almost as painful as my last breakup."
                },
                {
                        "I'm here because my therapist said I need to work on my anger issues.",
                        "I should be charging you for this entertainment.",
                        "I've seen bread with more fight in it!",
                        "This is my cardio for the day."
                },
                {
                        "Just wait till I tell my mom about this—oh wait, I don't have one!",
                        "Is this a boss battle or an awkward first date?",
                        "I'll send you a postcard from the respawn screen.",
                        "You fight like a soggy biscuit."
                }
        };

        return dialogueOptions[index][random.nextInt(dialogueOptions[index].length)];
    }

    /**
     * Spawns a random number of additional enemies in the boss room.
     * This method is called when the boss's health reaches 50%.
     */
    private void spawnRandomAdditionalEnemies() {
        GameAreaService gameAreaService = ServiceLocator.getGameAreaService();
        if (gameAreaService.getGameArea() != null) {
            GameController area = (GameController) gameAreaService.getGameController();
            if (area.getCurrentRoom() instanceof BossRoom bossRoom) {
                String[] possibleEnemies = {"Dog", "Snake", "rat", "bear", "bat", "dino", "minotaur", "dragon"};
                int numberOfEnemies; // Number of enemies to spawn based on difficulty

                if (ServiceLocator.getGameAreaService().getGameController().getPlayer().getComponent(PlayerConfigComponent.class).getPlayerConfig().difficulty == GameOptions.Difficulty.EASY) {
                    numberOfEnemies = random.nextInt(2) + 1;
                } else if (ServiceLocator.getGameAreaService().getGameController().getPlayer().getComponent(PlayerConfigComponent.class).getPlayerConfig().difficulty == GameOptions.Difficulty.MEDIUM) {
                    numberOfEnemies = random.nextInt(3) + 2;
                } else if (ServiceLocator.getGameAreaService().getGameController().getPlayer().getComponent(PlayerConfigComponent.class).getPlayerConfig().difficulty == GameOptions.Difficulty.HARD) {
                    numberOfEnemies = random.nextInt(4) + 3;
                } else {
                    numberOfEnemies = random.nextInt(2) + 1;
                }
                for (int i = 0; i < numberOfEnemies; i++) {
                    String enemy = possibleEnemies[random.nextInt(possibleEnemies.length)];
                    bossRoom.spawnRandomEnemies(enemy);
                }
            }
        }
    }
}
