package com.fushiginopixel.fushiginopixeldungeon.actors;

public class EffectResistance {

    public EffectType effect;
    public float multiplier;

    public EffectResistance(EffectType effect, float multiplier){
        this.effect = effect;
        this.multiplier = multiplier;
    }
}
