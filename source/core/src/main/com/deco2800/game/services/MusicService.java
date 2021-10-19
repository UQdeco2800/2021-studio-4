package com.deco2800.game.services;

import com.badlogic.gdx.audio.Music;

/**
 * Service for playing and controlling the play of game music, this is different
 * to a loadAsset() function as
 * it doesn't control the loading of the assets,
 * however it controls the playing and where the music plays.
 */
public class MusicService {
    private static final String musicLocation = "";
    private boolean isMusicPlaying;
    private Music music;

    /** Initial constructor class of the music service */
    public MusicService(String musicLocation) {
        music = ServiceLocator.getResourceService().getAsset(musicLocation, Music.class);
        isMusicPlaying = music.isPlaying();
    }

    /**
     * Stop this music.
     */
    public void stopMusic() {
        music.stop();
    }

    /**
     *  Checks whether this music is playing.
     * @return boolean: true - if the music is playing, false - if the music is not playing
     */
    public boolean isMusicPlaying() {
        return isMusicPlaying;
    }

    /**
     * Get the playback position of this music in seconds.
     * @return the playback position.
     */
    public float getTime() {
        return music.getPosition();
    }


    /**
     * Play this music with the given volume and loop value.
     * If loop is true, the music is played in the loop.
     * Required: an integer between 0 and 1
     */
    public void playSong(boolean loop, float vol) {
        MuteManager mute = MuteManager.getInstance();
        /* If the mute button is off, play the music otherwise do nothing. */
        if (!mute.getMute()) {
            music.setLooping(loop);
            music.setVolume(vol);
            music.play();
        }
    }


}