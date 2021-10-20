package com.deco2800.game.services;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * Testing Team 5's Music Directory
 * First test checks whether the directory is intalised for all tests using hashCode,
 * this is only an initialisation test
 * Secound Tests checks whether the music directory can be used in the LevelManager Switch function:
 */
public class MusicDirectoryTests {
    MusicServiceDirectory musicServiceDirectory = new MusicServiceDirectory();
    String player_collision = musicServiceDirectory.player_collision;
    @Test
    void MusicServiceDirectory() {
        assertEquals(musicServiceDirectory.player_collision.hashCode(), player_collision.hashCode());
    }

    /**
     *
     * Not Initialising levelGameArea yet
     */
    @Test
    void PlayingMusicTest() {
      //  levelGameArea.create();

    }
}
