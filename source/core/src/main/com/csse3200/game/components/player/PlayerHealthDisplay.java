package com.csse3200.game.components.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csse3200.game.components.CombatStatsComponent;
import com.csse3200.game.rendering.RenderComponent;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class PlayerHealthDisplay extends UIComponent{
        private float width = 1.5f;
        private float height = 0.2f;
        private ShapeRenderer shapeRenderer;

        /**
         * Creates reusable ui styles and adds actors to the stage.
         */
        @Override
        public void create() {
            super.create();
            shapeRenderer = new ShapeRenderer();
            entity.getEvents().addListener("updateHealth", this::updatePlayerHealthUI);
        }

        /**
         * Creates actors and positions them on the stage using a table.
         * @see Table for positioning options
         */


        @Override
        public void draw(SpriteBatch batch)  {

        }

        /**
         * Updates the player's health on the ui.
         * @param health player health
         */
        public void updatePlayerHealthUI(int health) {
            //
        }
        public void dispose() {
            super.dispose();
            shapeRenderer.dispose();
        }

}