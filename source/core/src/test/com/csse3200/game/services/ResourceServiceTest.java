package com.csse3200.game.services;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.csse3200.game.extensions.GameExtension;
import com.csse3200.game.files.UserSettings.Settings;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
class ResourceServiceTest {

  private static final String TEST_SOUND = "test/files/sound1.ogg";

  @Test
  void loadAllShouldLoadUnloadAssets() {
    String texture1 = "test/files/tree.png";
    String texture2 = "test/files/missing.png";
    String texture3 = "test/files/heart.png";
    String[] textures = {texture1, texture2, texture3};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextures(textures);
    resourceService.loadAll();

    verify(assetManager).load(texture1, Texture.class);
    verify(assetManager).load(texture2, Texture.class);
    verify(assetManager).load(texture3, Texture.class);

    assertTrue(assetManager.contains(texture1, Texture.class));
    assertFalse(assetManager.contains(texture2, Texture.class));
    assertTrue(assetManager.contains(texture3, Texture.class));

    resourceService.unloadAssets(textures);

    assertFalse(assetManager.contains(texture1, Texture.class));
    assertFalse(assetManager.contains(texture2, Texture.class));
    assertFalse(assetManager.contains(texture3, Texture.class));
  }

  @Test
  void loadForMillisShouldLoadAssets() {
    String texture1 = "test/files/tree.png";
    String texture2 = "test/files/missing.png";
    String texture3 = "test/files/heart.png";
    String[] textures = {texture1, texture2, texture3};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextures(textures);
    while (!resourceService.loadForMillis(1)) {
      ;
    }

    verify(assetManager).load(texture1, Texture.class);
    verify(assetManager).load(texture2, Texture.class);
    verify(assetManager).load(texture3, Texture.class);

    assertTrue(assetManager.contains(texture1, Texture.class));
    assertFalse(assetManager.contains(texture2, Texture.class));
    assertTrue(assetManager.contains(texture3, Texture.class));
  }

  @Test
  void shouldContainAndGetAssets() {
    String texture1 = "test/files/tree.png";
    String texture2 = "test/files/missing.png";
    String[] textures = {texture1, texture2};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextures(textures);
    resourceService.loadAll();

    assertTrue(resourceService.containsAsset(texture1, Texture.class));
    assertFalse(resourceService.containsAsset(texture2, Texture.class));

    verify(assetManager).contains(texture1, Texture.class);
    verify(assetManager).contains(texture2, Texture.class);

    assertNotNull(resourceService.getAsset(texture1, Texture.class));
  }

  @Test
  void shouldLoadTextures() {
    String asset1 = "test/files/tree.png";
    String asset2 = "test/files/heart.png";
    String[] textures = {asset1, asset2};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextures(textures);
    verify(assetManager).load(asset1, Texture.class);
    verify(assetManager).load(asset2, Texture.class);
  }

  @Test
  void shouldLoadTextureAtlases() {
    String asset1 = "test/files/test.atlas";
    String asset2 = "test/files/test2.atlas";
    String[] textures = {asset1, asset2};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadTextureAtlases(textures);
    verify(assetManager).load(asset1, TextureAtlas.class);
    verify(assetManager).load(asset2, TextureAtlas.class);
  }

  @Test
  void shouldLoadSounds() {
    String asset1 = "test/files/sound1.ogg";
    String asset2 = "test/files/sound2.ogg";
    String[] textures = {asset1, asset2};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadSounds(textures);
    verify(assetManager).load(asset1, Sound.class);
    verify(assetManager).load(asset2, Sound.class);
  }

  @Test
  void shouldLoadMusic() {
    String asset1 = "test/files/sound1.ogg";
    String asset2 = "test/files/sound2.ogg";
    String[] textures = {asset1, asset2};

    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);

    resourceService.loadMusic(textures);
    verify(assetManager).load(asset1, Music.class);
    verify(assetManager).load(asset2, Music.class);
  }

  private void loadTestSound(ResourceService resourceService) {
    resourceService.loadSounds(new String[]{TEST_SOUND});
    resourceService.loadAll();
  }

  private Settings getSettingsCopy(boolean mute) {
    Settings settings = new Settings();
    settings.mute = mute;
    return settings;
  }

  @Test
  void shouldPlaySound() {
    ResourceService resourceService = spy(new ResourceService(getSettingsCopy(false)));
    loadTestSound(resourceService);
    resourceService.playSound(TEST_SOUND);
    verify(resourceService).getAsset(TEST_SOUND, Sound.class);
  }

  @Test
  void noSoundWhenMuted() {
    ResourceService resourceService = spy(new ResourceService(getSettingsCopy(true)));
    loadTestSound(resourceService);
    resourceService.playSound(TEST_SOUND);
    verify(resourceService, never()).getAsset(TEST_SOUND, Sound.class);
  }

  @Test
  void musicPlays() {
    Music music = mock(Music.class);
    ResourceService resourceService = spy(new ResourceService(getSettingsCopy(false)));
    when(resourceService.containsAsset(TEST_SOUND, Music.class)).thenReturn(true);
    doReturn(music).when(resourceService).getAsset(TEST_SOUND, Music.class);

    resourceService.playMusic(TEST_SOUND, true);
    verify(resourceService).getAsset(TEST_SOUND, Music.class);
    verify(music).setLooping(true);
    verify(music).play();
  }

  @Test
  void musicStops() {
    Music music = mock(Music.class);
    ResourceService resourceService = spy(new ResourceService(getSettingsCopy(false)));
    when(resourceService.containsAsset(TEST_SOUND, Music.class)).thenReturn(true);
    doReturn(music).when(resourceService).getAsset(TEST_SOUND, Music.class);

    resourceService.playMusic(TEST_SOUND, true);
    resourceService.stopCurrentMusic();
    verify(music).stop();
  }
}
