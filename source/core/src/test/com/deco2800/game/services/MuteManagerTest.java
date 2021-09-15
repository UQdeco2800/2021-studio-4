package com.deco2800.game.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing Team 5's Mute Manager
 * Checking whether Mute is set to true or false
 */
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