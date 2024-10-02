package com.csse3200.game.components.screendisplay;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.achievementsscreen.AchievementsListComponent;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.ui.UIComponent;

import static com.csse3200.game.services.ServiceLocator.getResourceService;

/**
 * Displays achievement names/images or message saying there are no achievements.
 */
public class AchievementsScreenDisplay extends UIComponent {

    private Table rootTable;
    private AchievementsListComponent achievementsList;
    private boolean hasAchievement;

    @Override
    public void create() {
        super.create();
        achievementsList = entity.getComponent(AchievementsListComponent.class);
        hasAchievement = achievementsList.getNumAchievements() != 0;
        loadTextures();
        addActors();
    }

    private void loadTextures() {
        if (!hasAchievement) {
            return;
        }
        ResourceService resourceService = getResourceService();
        String[] textures = achievementsList.getAchievements().values().toArray(String[]::new);
        resourceService.loadTextures(textures);
        resourceService.loadAll();
    }

    private void addActors() {

        rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.defaults().pad(10f);

        if (hasAchievement) {
            Label title = new Label("Achievements", skin, "title");
            rootTable.add(title).colspan(2);
            achievementsList.getAchievements().forEach((name, imageName) -> {
                rootTable.row();
                Image image = new Image(new Texture(imageName));
                rootTable.add(image);
                Label nameLabel = new Label(name, skin);
                rootTable.add(nameLabel);
            });
        } else {
            rootTable.add(new Label("You don't have any achievements yet. Come back later!", skin));
        }

        stage.addActor(rootTable);
    }

    @Override
    public void dispose() {
        rootTable.clear();
        super.dispose();
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // draw handled by stage
    }
}
