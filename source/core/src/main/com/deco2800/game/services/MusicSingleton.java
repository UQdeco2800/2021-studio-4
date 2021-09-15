package com.deco2800.game.services;

import com.badlogic.gdx.audio.Music;

/**
 * A Singleton class that does the same job as MusicService class however the difference is that
 * there will be only one instance of this class throughout the program.
 * Service for playing and controlling the play of game music
 */
public class MusicSingleton {

    private static MusicSingleton music = null;
    private boolean is_playing = false;

    /** Constructor class of the musicSingleton */
    private MusicSingleton()
    {

    }

    /**
     *  Returns an instance of the MusicSingleton.
     *  If an instance of the MusicSingleton does not already exit, it creates one otherwise returns
     *  the existing one.
     * @return MusicSingleton
     */
    public static MusicSingleton getInstance()
    {
        if (music == null) {
            music = new MusicSingleton();
        }
        return music;

    }

    /**
     * Play this music in the loop.
     * Required: a string that contains the location of the music file.
     */
    public void playMusicSingleton(String filename) {
        MuteManager mute = MuteManager.getInstance();
        /* If the mute button is off, play the music otherwise do nothing. */
        if (mute.getMute() == false) {
            Music music = ServiceLocator.getResourceService().getAsset(filename, Music.class);
            music.setLooping(true);
            music.setVolume(0.3f);
            music.play();
        }
           /* if (is_playing == false) {
                is_playing = true;
                music.setLooping(true);
                music.setVolume(0.3f);

                music.play();
            }
            //music.setPosition(3.0f);
            //System.out.println(music.getPosition());*/

    }

    /**
     * Stop this music.
     * Required: a string that contains the location of the music file.
     */
    public void pauseMusicSingleton(String filename) {
        Music music = ServiceLocator.getResourceService().getAsset(filename, Music.class);
        music.pause();
        /*if (is_playing == false) {
                music.setLooping(true);
                music.setVolume(0.3f);
                is_playing = true;
                is_muted = false;
                music.play();
            } else {
                music.pause();
                is_playing = false;
                is_muted = true;
            }*/

    }

}