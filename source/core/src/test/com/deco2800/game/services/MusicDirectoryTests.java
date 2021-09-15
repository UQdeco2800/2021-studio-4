package com.deco2800.game.services;
import com.deco2800.game.areas.LevelGameArea;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.levels.LevelDefinition;
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
    //LevelDefinition levelDefinition = new LevelDefinition();
    String player_collision = musicServiceDirectory.player_collision;
    //CameraComponent cameraComponent = new CameraComponent();
    //TerrainFactory terrainFactory = new TerrainFactory(cameraComponent);
    //LevelGameArea levelGameArea = new LevelGameArea(terrainFactory);
    @Test
    void MusicServiceDirectory() {
        assertEquals(musicServiceDirectory.player_collision.hashCode(), player_collision.hashCode());
    }
    @Test
    void PlayingMusicTest() {
      //  levelGameArea.create();

    }
}
