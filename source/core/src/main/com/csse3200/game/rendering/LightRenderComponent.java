package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LightRenderComponent extends TextureRenderComponent {
    public LightRenderComponent(String texturePath) {
        this(new Texture(texturePath));
    }

    public LightRenderComponent(Texture texture) {
        super(texture);
        //    private static final Logger log = LoggerFactory.getLogger(LightRenderComponent.class);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        batch.setBlendFunction(GL20.GL_DST_COLOR, GL20.GL_SRC_ALPHA);
//        entity.setRotation(entity.getComponent(PlayerActions.class).getWalkDirection().angleDeg());
        super.draw(batch);
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
}
