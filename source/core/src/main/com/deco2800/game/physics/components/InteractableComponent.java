package com.deco2800.game.physics.components;
/**
 * Dummy component to indicate whether an entity is an interactable.
 */
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.ObstacleEntity;

import java.util.ArrayList;

public class InteractableComponent extends Component {

    private InteractableComponent interactableComponent;
    private ArrayList<ObstacleEntity> mappedSubInteractables = new ArrayList<>();

    public void create() {
        interactableComponent = entity.getComponent(InteractableComponent.class);
        super.create();
    }

    public ArrayList<ObstacleEntity> getMapped() {
        return mappedSubInteractables;
    }

    public void addSubInteractable(ObstacleEntity obstacle) {
        this.mappedSubInteractables.add(obstacle);
    }
}

