package com.deco2800.game.components.loading;
import static org.mockito.Mockito.verify;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.ui.UIComponent;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(GameExtension.class)
public class LoadingScreenDisplayTest {
    LoadingScreenDisplay loadingScreenDisplay = new LoadingScreenDisplay();
    Entity ui = new Entity();
    @Test
    void testCreate() {
       ui.addComponent(loadingScreenDisplay);

    }

}