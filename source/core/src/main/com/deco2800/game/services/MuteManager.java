package com.deco2800.game.services;

/**
 * Service for controlling the mute button
 * This is a Singleton class, so we always have only one instance of this class.
 */
public class MuteManager {
    private static MuteManager mute = null;

    /* Mode of this MuteManager (activated/deactivated). The initial value is false.*/
    private boolean is_muted = false;

    /** Initial constructor class of the mute manager */
    private MuteManager() {}

    /**
     *  Returns an instance of the MuteManager.
     *  If an instance of the MuteManager does not already exit, it creates one otherwise returns
     *  the existing one.
     * @return MuteManager
     */
    public static MuteManager getInstance()
    {
        if (mute == null) {
            mute = new MuteManager();
        }
        return mute;

    }

    /**
     * Set the mode of this MuteManager to a boolean value.
     * It sets this instance to active/deactivate mode.
     * Required:  a boolean value.
     */
    public void setMute (boolean muted) {
            is_muted = muted;
    }

    /**
     * Get this MuteManager's mode
     * @return  a boolean value.
     */
    public boolean getMute () {
        return is_muted;
    }
}
