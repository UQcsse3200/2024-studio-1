package com.csse3200.game.components.npc;

import com.csse3200.game.components.Component;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.services.AlertBoxService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.screens.MainGameScreen;

public class BossHealthDialogueComponent extends Component {
    private CombatStatsComponent combatStats;
    private final float[] healthThresholds = {0.75f, 0.5f, 0.25f};
    private int currentThresholdIndex = 0;

    @Override
    public void create() {
        combatStats = entity.getComponent(CombatStatsComponent.class);
    }

    @Override
    public void update() {
        if (currentThresholdIndex >= healthThresholds.length) {
            return; // All dialogues have been shown
        }

        float healthPercentage = (float) combatStats.getHealth() / combatStats.getMaxHealth();

        if (healthPercentage <= healthThresholds[currentThresholdIndex]) {
            showDialogueAndPause();
            currentThresholdIndex++;
        }
    }

    private void showDialogueAndPause() {
        MainGameScreen.isPaused = true;
        String message = getBossDialogue(currentThresholdIndex);

        ServiceLocator.getAlertBoxService().confirmDialog(
                "Boss Battle",
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

    private String getBossDialogue(int index) {
        return switch (index) {
            case 0 -> "You've managed to hurt me. But this battle is far from over!";
            case 1 -> "Half of my strength is gone, but my resolve remains unbroken!";
            case 2 -> "I'm on my last legs, but I won't go down without a fight!";
            default -> "You'll never defeat me!";
        };
    }
}
