package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.components.player.PlayerActions;

public class LightRenderComponent extends TextureRenderComponent {
//    private static final Logger log = LoggerFactory.getLogger(LightRenderComponent.class);
    private final Texture texture;

    public LightRenderComponent(String texturePath) {
        this(new Texture(texturePath));
    }

    public LightRenderComponent(Texture texture) {
        super(texture);
        this.texture = texture;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_SRC_ALPHA);
        Vector2 position = entity.getPosition();
        Vector2 scale = entity.getScale();
        entity.getComponent(PlayerActions.class).getWalkDirection().angleDeg();
        batch.draw(texture,
                position.x - 1,
                position.y,
                scale.x * 3,
                scale.y * 3);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
}
