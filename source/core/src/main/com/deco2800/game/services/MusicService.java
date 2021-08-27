package com.deco2800.game.services;


import com.badlogic.gdx.audio.Music;

import com.deco2800.game.services.ServiceLocator;

/**
 * Service for playing and controlling the play of game music, this is different to a loadAsset() function as
 *          it doesn't control the loading of the assets, however it controls the playing and where the music plays.
 */
public class MusicService {
    private static final String musicLocation = "";
    // private static final String backgroundMusic = "sounds/MainMenuMusic.mp3";
    // private static final String[] MainMenuMusic = {backgroundMusic};
    private boolean isMusicPlaying;
    private Music music;
    /** Initial constructor class of the music service */
    public MusicService(String musicLocation) {
        music = ServiceLocator.getResourceService().getAsset(musicLocation, Music.class);
        isMusicPlaying = music.isPlaying();
    }

    public void stopMusic() {
        //Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.stop();
    }

    /** Checks whether the music is playing.
     *
     * @return boolean: true - if the music is playing, false - if the music is not playing
     */
    public boolean isMusicPlaying() {
        return isMusicPlaying;
    }


    public void playMusic() {
        //Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(0.3f);
        music.play();
    }


}