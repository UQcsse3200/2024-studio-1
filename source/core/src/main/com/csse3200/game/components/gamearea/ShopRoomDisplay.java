package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Timer;
import com.csse3200.game.ui.UIComponent;

public class ShopRoomDisplay extends UIComponent {
    @Override
    protected void draw(SpriteBatch batch) {

    }

    @Override
    public void create() {
        super.create();
        showRoomSpawnMessage();
    }

    public void showRoomSpawnMessage() {
        String text = String.format("SHOP ROOM - Press P to Purchase");
        Label lable = new Label(text, skin, "small");
        Table table = new Table();
        table.center();
        table.setFillParent(true);
        table.add(lable).padTop(5f);
        stage.addActor(table);
        // unrender the label after 3 second of display
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                table.remove(); // Remove the label from the screen
            }
        }, 3);
    }
}
