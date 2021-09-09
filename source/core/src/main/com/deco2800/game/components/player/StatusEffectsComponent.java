package com.deco2800.game.components.player;

import com.deco2800.game.components.Component;
import com.deco2800.game.components.statuseffects.StatusEffectEnum;

import java.util.ArrayList;

/** This component is intended to track the status effects that the player has picked up during their run. */
public class StatusEffectsComponent extends Component {
    private ArrayList<String> statusEffects; // List of status effects.
    // private StatusEffect statusEffects;

    public StatusEffectsComponent() {
        /** Create a new array list for the status effects. */
        statusEffects = new ArrayList<String>();
    }

    /** Return the list of current status effects */
    public ArrayList<String> getStatusEffects() {
        return statusEffects;
    }

    /** Add status effect to the list of current status effects */
    public void addStatusEffect(StatusEffectEnum statusEffect) {
        statusEffects.add(statusEffect.getType());
    }

    /** Removes the status effect from the list of current status effects */
    public void removeStatusEffect(StatusEffectEnum statusEffect) {
        statusEffects.remove(statusEffect.getType());
    }
}
