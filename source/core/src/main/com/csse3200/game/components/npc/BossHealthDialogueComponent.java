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
    private Random random = new Random();

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
                        "Ouch! That tickled... I mean, hurt! You're in for it now!",
                        "Is that all you've got? My grandma hits harder!",
                        "Oh no, I'm at 75% health! ...Said no boss ever!"
                },
                {
                        "Half my health gone? Time to get serious... right after this coffee break!",
                        "You're halfway there! Too bad I have a second health bar... Kidding! Or am I?",
                        "50% health? Perfectly balanced, as all things should be!"
                },
                {
                        "I'm not sweating, it's just boss condensation!",
                        "25% health? I've had worse paper cuts!",
                        "Is it hot in here, or is it just my impending defeat?"
                }
        };

        return dialogueOptions[index][random.nextInt(dialogueOptions[index].length)];
    }

    private void spawnRandomAdditionalEnemies() {
        GameAreaService gameAreaService = ServiceLocator.getGameAreaService();
        if (gameAreaService.getGameArea() instanceof MainGameArea area) {
            if (area.getCurrentRoom() instanceof BossRoom bossRoom) {
                String[] possibleEnemies = {"Dog", "Snake", "rat", "bear", "bat", "dino", "minotaur", "dragon"};
                String enemy1 = possibleEnemies[random.nextInt(possibleEnemies.length)];
                String enemy2 = possibleEnemies[random.nextInt(possibleEnemies.length)];
                bossRoom.spawnRandomEnemies(enemy1, enemy2);
            }
        }
    }
}