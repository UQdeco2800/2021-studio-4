package com.deco2800.game.physics.components;
/*
  Dummy component to indicate whether an entity is an interactable.
 */
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.ObstacleEntity;

import java.util.ArrayList;

public class InteractableComponent extends Component {

    private final ArrayList<ObstacleEntity> mappedSubInteractables = new ArrayList<>();

    /**
     * Create a new Interactable component.
     */
    public void create() {
        super.create();
    }

    /**
     * Get the mapped sub-interactable entities.
     * @return Mapped sub-interactable entities
     */
    public ArrayList<ObstacleEntity> getMapped() {
        return mappedSubInteractables;
    }

    /**
     * Add a new sub-interactable entity to the mapping.
     * @param obstacle Sub-interactable entity
     */
    public void addSubInteractable(ObstacleEntity obstacle) {
        this.mappedSubInteractables.add(obstacle);
    }
}

