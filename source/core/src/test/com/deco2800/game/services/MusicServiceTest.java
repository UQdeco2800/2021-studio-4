package com.deco2800.game.services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class MusicServiceTest {
    @Test
    void playMusic() {
      /*  String[] music1 = new String[]{"sounds/MainMenuMusic.mp3"};


        AssetManager assetManager = spy(AssetManager.class);
        ResourceService resourceService = new ResourceService(assetManager);

        resourceService.loadMusic(music1);
        resourceService.loadAll();
        MusicService musicService = new MusicService("sounds/MainMenuMusic.mp3");
        musicService.playMusic();
        assertTrue(musicService.isMusicPlaying());*/
    }
    @Test
    void loadAllShouldLoadUnloadAssets() {
        String[] music1 = new String[]{"sounds/MainMenuMusic.mp3"};


        AssetManager assetManager = spy(AssetManager.class);
        ResourceService resourceService = new ResourceService(assetManager);

        resourceService.loadMusic(music1);
        resourceService.loadAll();


    }
    @Test
    void stopMusic() {

    }
    @Test
    void isMusicPlaying() {

    }
}
