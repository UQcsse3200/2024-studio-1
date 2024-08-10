package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.services.ServiceLocator;

/** Render a static texture. */
public class TextureRenderComponent extends RenderComponent {
  private final Texture texture;
  private  Vector2 relativeScale = new Vector2(1, 1);
  private  float relativeRotation = 0;
  private  Vector2 relativePosition = new Vector2(0, 0);

  /**
   * @param texturePath Internal path of static texture to render.
   *                    Will be scaled to the entity's scale.
   */
  public TextureRenderComponent(String texturePath) {
    this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
  }

  /** @param texture Static texture to render. Will be scaled to the entity's scale. */
  public TextureRenderComponent(Texture texture) {
    this.texture = texture;
  }

  /** Scale the entity to a width of 1 and a height matching the texture's ratio */
  public void scaleEntity() {
    entity.setScale(1f, (float) texture.getHeight() / texture.getWidth());
  }

  private Vector2 piecewiseMultiply(Vector2 v1, Vector2 v2) {
    return new Vector2(v1.x * v2.x, v1.y * v2.y);
  }

  @Override
  protected void draw(SpriteBatch batch) {
    Vector2 position = entity.getPosition().add(relativePosition);
    Vector2 scale = piecewiseMultiply(entity.getScale(), relativeScale);
    float rotation = entity.getRotation() + relativeRotation;
    int width = texture.getWidth();
    int height = texture.getHeight();

//    batch.draw(texture, position.x, position.y, scale.x, scale.y);
    batch.draw(texture,
            position.x, position.y,
            1.5f, 0.1f, // FIXME these should be parameterised
            scale.x, scale.y,
            1, 1,
            rotation,
            0, 0,
            width, height,
            false, false);
  }

    public Vector2 getRelativeScale() {
        return relativeScale;
    }

    public void setRelativeScale(Vector2 relativeScale) {
        this.relativeScale = relativeScale;
    }

    public float getRelativeRotation() {
        return relativeRotation;
    }

    public void setRelativeRotation(float relativeRotation) {
        this.relativeRotation = relativeRotation;
    }

    public Vector2 getRelativePosition() {
        return relativePosition;
    }

    public void setRelativePosition(Vector2 relativePosition) {
        this.relativePosition = relativePosition;
    }
}
