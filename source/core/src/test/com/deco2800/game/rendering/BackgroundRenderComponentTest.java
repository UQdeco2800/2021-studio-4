package com.deco2800.game.rendering;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing the class BackgroundRenderComponent
 * (NOTE: some functionality not full implemented for sprint 2)
 */
public class BackgroundRenderComponentTest {
    private String texturePath;
    BackgroundRenderComponent backgroundRenderComponent = new BackgroundRenderComponent(texturePath);
    @Test
    void createBackground() {
       // verify(backgroundRenderComponent).create();
    }
    @Test
    void getLayer() {
        assertEquals(backgroundRenderComponent.getLayer(), 0);
    }
    @Test
    void getZIndex() {
        assertEquals(backgroundRenderComponent.getLayer(), 0);
    }
}
