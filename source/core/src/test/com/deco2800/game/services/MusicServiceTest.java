package com.deco2800.game.services;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MusicServiceTest {
    @Test
    void playMusic() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadMusic(new String[]{"sounds/MainMenuMusic.mp3"});
        ServiceLocator.getResourceService().loadAll();
        MusicService musicService = new MusicService("sounds/MainMenuMusic.mp3");
        musicService.playMusic();
        assertTrue(musicService.isMusicPlaying());
    }
}
