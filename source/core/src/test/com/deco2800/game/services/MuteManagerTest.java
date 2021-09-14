package com.deco2800.game.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MuteManagerTest {
    MuteManager mute = MuteManager.getInstance();

    @Test
    void setMuteTest(){
        mute.setMute(true);
        assertTrue(mute.getMute());
        mute.setMute(false);
        assertFalse(mute.getMute());
    }

}