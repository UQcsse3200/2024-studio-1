package com.csse3200.game.components.screendisplay;

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.csse3200.game.rendering.AnimationRenderComponent;

/**
 * This class is sued to control the animation rendering for each
 * player on the Select Screen.
 */
public class PlayerSelectAnimation extends Actor {
    private final AnimationRenderComponent animator;
    private final PlayerAnimationType player;

    public enum PlayerAnimationType {
        Player1("idle", 0.2f, PlayMode.LOOP),
        Player2("Walk_right", 0.2f, PlayMode.LOOP),
        Player3("Run_right", 0.2f, PlayMode.LOOP_PINGPONG),
        Bear("idle_right", 0.5f, PlayMode.LOOP),
        Player4("Attack1_right", 0.2f, PlayMode.LOOP_RANDOM);

        public final String animationName;
        public final float animationDuration;
        public final PlayMode animationPlayMode;

        PlayerAnimationType(String animationName, float animationDuration, PlayMode animationPlayMode) {
            this.animationName = animationName;
            this.animationDuration = animationDuration;
            this.animationPlayMode = animationPlayMode;
        }

        // Assign player number to the texture atlas
        static PlayerAnimationType createFromAtlas(String textureAtlas) {
            return switch (textureAtlas) {
                case "images/player/player.atlas" -> Player1;
                case "images/player/homeless1.atlas" -> Player2;
                case "images/npc/bear/bear.atlas" -> Bear;
                case "images/player/homeless2.atlas" -> Player3;
                case "images/player/homeless3.atlas" -> Player4;
                default -> throw new IllegalArgumentException(
                        "Unknown texture atlas: " + textureAtlas);
            };
            // necromancer uses normal player atlas
        }
    }

    public PlayerSelectAnimation(AnimationRenderComponent animator, String textureAtlas) {
        this.animator = animator;
        this.player = PlayerAnimationType.createFromAtlas(textureAtlas);
        initializeAnimations();
    }

    // Add texture region relative to each player
    private void initializeAnimations() {
        animator.addAnimation(
                player.animationName, player.animationDuration, player.animationPlayMode);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        animator.update(delta); // Update the animator state
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Set the batch color for transparency
        batch.setColor(1f, 1f, 1f, parentAlpha);

        // Get the current frame from the animator
        TextureRegion currentFrame = animator.getCurrentFrame();

        // Calculate width, height to draw at
        float cellRatio = getHeight() / getWidth();
        float regionRatio = (float) currentFrame.getRegionHeight() / currentFrame.getRegionWidth();
        float width, height;
        if (cellRatio > regionRatio) {
            width = getWidth();
            height = width * regionRatio;
        } else {
            height = getHeight();
            width = height / regionRatio;
        }
        float x = getX() + (getWidth() - width) / 2;
        float y = getY() + (getHeight() - height) / 2;

        batch.draw(currentFrame, x, y, width, height);

        // Reset batch color
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public void startAnimation() {
        animator.startAnimation(player.animationName);
    }
}
