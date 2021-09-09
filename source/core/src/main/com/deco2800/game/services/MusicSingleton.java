package com.deco2800.game.services;

import com.badlogic.gdx.audio.Music;

public class MusicSingleton {

    private static MusicSingleton music = null;
    private boolean is_playing = false;


    private MusicSingleton()
    {

    }


    public static MusicSingleton getInstance()
    {
        if (music == null) {
            music = new MusicSingleton();
        }
        return music;

    }
    public void playMusicSingleton(String filename) {
        MuteManager mute = MuteManager.getInstance();
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