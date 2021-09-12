package com.deco2800.game.components.statuseffects;

public class StatusEffectOperation {
    private int type, boost, statOriginal;
    private StatusEffectEnum statusEffectEnum;

    public StatusEffectOperation(int type, int boost, int statOriginal, StatusEffectEnum statusEffectEnum) {
        this.type = type;
        this.boost = boost;
        this.statOriginal = statOriginal;
        this.statusEffectEnum = statusEffectEnum;
    }

    public int statChange() {
        return statusEffectEnum.statChange(type, boost, statOriginal);
    }
}
