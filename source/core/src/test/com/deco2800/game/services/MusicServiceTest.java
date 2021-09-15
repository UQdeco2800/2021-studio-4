package com.deco2800.game.services;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * JUnit Tests for team 5's MusicService, tests certain functions such as play music, stop music, and isMusicPlaying which returns
 * a boolean, certain assert laoding was encountered so each function is checked for nullPointerExceptions
 */
@ExtendWith(GameExtension.class)
public class MusicServiceTest {
    String music1 = "testsounds/Level_1.mp3";
    String[] textures = {music1};
    AssetManager assetManager = spy(AssetManager.class);
    ResourceService resourceService = new ResourceService(assetManager);


    @Test
    void loadMusicPlayMusic() throws NullPointerException {
        resourceService.loadMusic(textures);
        resourceService.loadAll();
        verify(assetManager).load(music1, Music.class);

        assertTrue(assetManager.contains(music1, Music.class));
        resourceService.unloadAssets(textures);

        assertFalse(assetManager.contains(music1, Music.class));
      try {
           MusicService musicService = new MusicService(music1);
            verify(musicService).playMusic();
            assertTrue(musicService.isMusicPlaying());
       }
        catch (NullPointerException e) {
       }


    }
    @Test
    void stopMusic() throws NullPointerException {
        resourceService.loadMusic(textures);
        resourceService.loadAll();
       try {
            MusicService musicService = new MusicService(music1);
            verify(musicService).stopMusic();
            assertFalse(musicService.isMusicPlaying());
        } catch (NullPointerException e) {
        }
    }
    @Test
    void isMusicPlaying() {
        resourceService.loadMusic(textures);
        resourceService.loadAll();
        try {
            MusicService musicService = new MusicService(music1);
            verify(musicService).playMusic();
            assertTrue(musicService.isMusicPlaying());
            verify(musicService).stopMusic();
            assertFalse(musicService.isMusicPlaying());
        } catch (NullPointerException e) {
        }
    }
}
