package com.deco2800.game.services;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(GameExtension.class)
public class MusicServiceTest {
    @Test
    void playMusic() throws NullPointerException {
     /*   String[] musicArray = new String[]{"testsounds/Level_1.mp3"};
        String music = "testsounds/Level_1.mp3";
        try {
            AssetManager assetManager = spy(AssetManager.class);
            ResourceService resourceService = new ResourceService(assetManager);
            resourceService.loadMusic(musicArray);
            while (!resourceService.loadForMillis(10)) {
                // This could be upgraded to a loading screen
                //logger.info("Loading... {}%", resourceService.getProgress());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }*/


    }
    @Test
    void loadMusic() throws NullPointerException {
        String music1 = "testsounds/Level_1.mp3";
        String[] textures = {music1};
        AssetManager assetManager = spy(AssetManager.class);
        ResourceService resourceService = new ResourceService(assetManager);
        resourceService.loadMusic(textures);
        resourceService.loadAll();
//        ServiceLocator.getResourceService().getAsset(music1, Music.class);

        verify(assetManager).load(music1, Music.class);

        assertTrue(assetManager.contains(music1, Music.class));
        resourceService.unloadAssets(textures);

        assertFalse(assetManager.contains(music1, Music.class));
       // assertFalse(assetManager.contains(texture2, Texture.class));
        //assertFalse(assetManager.contains(texture3, Texture.class));
       // try {
       //     MusicService musicService = new MusicService(music1);
       //     verify(musicService).playMusic();
       //     assertTrue(musicService.isMusicPlaying());
      //  }
       // catch (NullPointerException e) {
       //
      //  }


    }
    @Test
    void stopMusic() {

    }
    @Test
    void isMusicPlaying() {

    }
}
