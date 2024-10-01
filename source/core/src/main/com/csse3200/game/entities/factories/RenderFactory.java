package com.csse3200.game.entities.factories;

import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.NameComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ServiceLocator;

public class RenderFactory {

  public static Entity createCamera(String compName) {
    return new Entity()
            .addComponent(new CameraComponent())
            .addComponent(new NameComponent(compName));
  }

  public static Renderer createRenderer() {
    Entity camera = createCamera("camera");
    Entity secondaryCamera = createCamera("SecondaryCamera");
    ServiceLocator.getEntityService().register(camera);
    ServiceLocator.getEntityService().register(secondaryCamera);
    CameraComponent camComponent = camera.getComponent(CameraComponent.class);
    CameraComponent secComponent = secondaryCamera.getComponent(CameraComponent.class);

    return new Renderer(camComponent, secComponent);
  }

  private RenderFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
