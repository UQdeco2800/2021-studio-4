package com.deco2800.game.services;


import com.badlogic.gdx.audio.Music;

import com.deco2800.game.services.ServiceLocator;


public class MusicService {
    private static final String backgroundMusic = "sounds/MainMenuMusic.mp3";
    private static final String[] MainMenuMusic = {backgroundMusic};
    private boolean musicPlaying;

    public MusicService() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        musicPlaying = music.isPlaying();
    }

    public void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }

    public void stopMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.stop();
    }

    public boolean getmusicPlaying() {
        return musicPlaying;
    }
}
