package com.fushiginopixel.fushiginopixeldungeon.items.generators;

import com.fushiginopixel.fushiginopixeldungeon.items.Gold;
import com.fushiginopixel.fushiginopixeldungeon.items.generators.Jackpots.Jackpot;

public class NormalGenerator {//extends Generators {

    protected float[] INITIAL_ARTIFACT_PROBS = new float[]{};

    public Jackpot jackpot_gold = new Jackpot(
            new Object[]{
                    Gold.class
            },
            new float[]{
                    1
            }
    );
    public Jackpot jackpot_meleeWeapon;
    public Jackpot jackpot_armor;
    public Jackpot jackpot_rangedWeapon;
    public Jackpot jackpot_potion;
    public Jackpot jackpot_scroll;
    public Jackpot jackpot_pot;
    public Jackpot jackpot_wand;
    public Jackpot jackpot_ring;
    public Jackpot jackpot_artifact;
    public Jackpot jackpot_seed;
    public Jackpot jackpot_food;
    public Jackpot jackpot_bomb;
}
