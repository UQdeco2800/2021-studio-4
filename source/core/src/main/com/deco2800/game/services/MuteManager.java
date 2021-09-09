package com.deco2800.game.services;

public class MuteManager {
    private static MuteManager mute = null;
    private boolean is_muted = false;

    private MuteManager()
    {

    }

    public static MuteManager getInstance()
    {
        if (mute == null) {
            mute = new MuteManager();
        }
        return mute;

    }

    public void setMute (boolean muted) {
            is_muted = muted;
    }
    public boolean getMute () {
        return is_muted;
    }
}
