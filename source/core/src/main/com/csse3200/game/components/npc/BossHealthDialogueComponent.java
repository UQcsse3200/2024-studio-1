package com.csse3200.game.components.npc;

import com.csse3200.game.areas.MainGameArea;
import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.AlertBoxService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.screens.MainGameScreen;
import com.csse3200.game.areas.GameAreaService;
import com.csse3200.game.areas.BossRoom;
import java.util.Random;

public class BossHealthDialogueComponent extends Component {
    private CombatStatsComponent combatStats;
    private final float[] healthThresholds = {0.75f, 0.5f, 0.25f};
    private int currentThresholdIndex = 0;
    private final Random random = new Random();

    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
    }

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

    private String getRandomBossDialogue(int index) {
        String[][] dialogueOptions = {
                {
                        "Your looks hurt more than your attacks.",
                        "If I had a nickel for every time you missed… I’d have a lot of nickels.",
                        "You fight like someone who reads the tutorial.",
                        "This is almost as painful as my last breakup."
                },
                {
                        "I'm here because my therapist said I need to work on my anger issues.",
                        "I should be charging you for this entertainment.",
                        "I’ve seen bread with more fight in it!",
                        "This is my cardio for the day."
                },
                {
                        "Just wait till I tell my mom about this—oh wait, I don't have one!",
                        "Is this a boss battle or an awkward first date?",
                        "I’ll send you a postcard from the respawn screen.",
                        "You fight like a soggy biscuit."
                }
        };

        return dialogueOptions[index][random.nextInt(dialogueOptions[index].length)];
    }

    private void spawnRandomAdditionalEnemies() {
        GameAreaService gameAreaService = ServiceLocator.getGameAreaService();
        if (gameAreaService.getGameArea() instanceof MainGameArea area) {
            if (area.getCurrentRoom() instanceof BossRoom bossRoom) {
                String[] possibleEnemies = {"Dog", "Snake", "rat", "bear", "bat", "dino", "minotaur", "dragon"};
                int numberOfEnemies = random.nextInt(3) + 2; // Random number of enemies between 2 and 5

                for (int i = 0; i < numberOfEnemies; i++) {
                    String enemy = possibleEnemies[random.nextInt(possibleEnemies.length)];
                    bossRoom.spawnRandomEnemies(enemy);
                }
            }
        }
    }
}