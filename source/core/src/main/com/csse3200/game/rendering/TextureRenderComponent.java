package com.csse3200.game.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.services.ServiceLocator;

/** Render a static texture. */
public class TextureRenderComponent extends RenderComponent {
  private Texture texture;

  /**
   * @param texturePath Internal path of static texture to render.
   *                    Will be scaled to the entity's scale.
   */
  public TextureRenderComponent(String texturePath) {
    this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
  }
//...
  /** @param texture Static texture to render. Will be scaled to the entity's scale. */
  public TextureRenderComponent(Texture texture) {
    this.texture = texture;
  }

  /** Scale the entity to a width of 1 and a height matching the texture's ratio */
  public void scaleEntity() {
    entity.setScale(1f, (float) texture.getHeight() / texture.getWidth());
  }

  @Override
  protected void draw(SpriteBatch batch) {
    Vector2 position = entity.getPosition();
    Vector2 scale = entity.getScale();
    batch.draw(texture, position.x, position.y, scale.x, scale.y);
  }

  /**
   * Get the texture being rendered.
   * @return texture
   */
  public Texture getTexture() {
    return texture;
  }
  /**
   * Set a new texture for rendering and scale the entity based on the new texture's dimensions.
   *
   * @param texturePath Internal path of the new texture to render.
   */
  public void setTexture(String texturePath) {
    // Dispose of the old texture to avoid memory leaks
    if (texture != null) {
      texture.dispose();
    }
    // Set the new texture
    this.texture = ServiceLocator.getResourceService().getAsset(texturePath, Texture.class);

    // Rescale the entity based on the new texture dimensions
    scaleEntity();
  }
}
