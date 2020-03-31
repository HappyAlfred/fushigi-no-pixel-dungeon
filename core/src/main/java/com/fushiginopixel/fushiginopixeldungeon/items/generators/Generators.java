package com.fushiginopixel.fushiginopixeldungeon.items.generators;

import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.items.Gold;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.Artifact;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bombs;
import com.fushiginopixel.fushiginopixeldungeon.items.food.Food;
import com.fushiginopixel.fushiginopixeldungeon.items.generators.Jackpots.Jackpot;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.Scroll;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MeleeWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.MissileWeapon;
import com.fushiginopixel.fushiginopixeldungeon.plants.Plant;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class Generators {

    private static Class[] orderClasses = new Class[]{
            MeleeWeapon.class,
            Armor.class,
            MissileWeapon.class,
            Potion.class,
            Scroll.class,
            Pot.class,
            Wand.class,
            Ring.class,
            Artifact.class,
            Plant.Seed.class,
            Food.class,
            Bombs.class,
            Gold.class
    };

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

    public Jackpot jackpot_random = new Jackpot(
            new Object[] {
                jackpot_gold,
                jackpot_meleeWeapon,
                jackpot_armor,
                jackpot_rangedWeapon,
                jackpot_potion,
                jackpot_scroll,
                jackpot_pot,
                jackpot_wand,
                jackpot_ring,
                jackpot_artifact,
                jackpot_food,
                jackpot_bomb
            },
            new float[]{
                    20, 6, 4, 3, 20, 20, 10, 3, 1, 1, 15, 1
            }
    );


    public static int order( Item item ) {
        for (int i=0; i < orderClasses.length; i++) {
            if (orderClasses[i].isInstance( item )) {
                return i;
            }
        }

        return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
    }

    protected HashMap mergeToMap(Object[] prizes, float[] probs){
        HashMap<Object,Float> categoryProbs = new LinkedHashMap<>();
        for(int i=0; i < prizes.length; i++){
            if(i < probs.length){
                categoryProbs.put(prizes[i], probs[i]);
            }else{
                categoryProbs.put(prizes[i], 0f);
            }
        }
        return categoryProbs;
    }

    public Object singleRandom(Jackpot jackpot) {
        try {

            if(jackpot == jackpot_armor
                    || jackpot == jackpot_meleeWeapon
                    || jackpot == jackpot_rangedWeapon){
                return randomItemWithRarity(jackpot, 0);
            }else if(jackpot == jackpot_artifact){
                Item item = randomArtifact();
                //if we're out of artifacts, return a ring instead.
                return item != null ? item : random(jackpot_ring);
            }else{

                HashMap<Object,Float> categoryProbs = mergeToMap(jackpot.prizes, jackpot.probs);
                Object ob = Random.chances( categoryProbs );
                if(ob instanceof Class){
                    return ((Item)((Class) ob).newInstance()).random();
                }else{
                    return ob;
                }
            }

        } catch (Exception e) {

            Fushiginopixeldungeon.reportException(e);
            return null;

        }
    }

    public Item random(Jackpot jackpot) {
        Object drop;
        do {
            drop = singleRandom(jackpot);
            if(drop instanceof Jackpot){
                jackpot = (Jackpot) drop;
            }else{
                break;
            }
        }while(true);
        if(drop instanceof Item) {
            return (Item) drop;
        }else return null;
    }

    public abstract Item random();

    public static Item random( Class<? extends Item> cl ) {
        try {

            return ((Item)cl.newInstance()).random();

        } catch (Exception e) {

            Fushiginopixeldungeon.reportException(e);
            return null;

        }
    }


    public abstract Item randomItemWithRarity(Jackpot jackpot, int rarity);

    //enforces uniqueness of artifacts throughout a run.
    public Artifact randomArtifact() {

        try {
            Jackpot cat = jackpot_artifact;
            int i = Random.chances( cat.probs );

            //if no artifacts are left, return null
            if (i == -1){
                return null;
            }

            Class<?extends Artifact> art = (Class<? extends Artifact>) cat.prizes[i];

            if (removeArtifact(art)) {
                Artifact artifact = art.newInstance();

                artifact.random();

                return artifact;
            } else {
                return null;
            }

        } catch (Exception e) {
            Fushiginopixeldungeon.reportException(e);
            return null;
        }
    }


    //resets artifact probabilities, for new dungeons
    public void initArtifacts() {
        jackpot_artifact.probs = INITIAL_ARTIFACT_PROBS.clone();
        spawnedArtifacts = new ArrayList<>();
    }

    private static ArrayList<Class<?extends Artifact>> spawnedArtifacts = new ArrayList<>();

    public boolean removeArtifact(Class<?extends Artifact> artifact) {
        if (spawnedArtifacts.contains(artifact))
            return false;

        Jackpot cat = jackpot_artifact;
        for (int i = 0; i < cat.prizes.length; i++)
            if (cat.prizes[i].equals(artifact)) {
                if (cat.probs[i] == 1){
                    cat.probs[i] = 0;
                    spawnedArtifacts.add(artifact);
                    return true;
                } else
                    return false;
            }

        return false;
    }

    private static final String SPAWNED_ARTIFACTS = "spawned_artifacts";

    public void storeInBundle(Bundle bundle) {
        bundle.put( SPAWNED_ARTIFACTS, spawnedArtifacts.toArray(new Class[0]));
    }

    public void restoreFromBundle(Bundle bundle) {

        initArtifacts();
        if (bundle.contains(SPAWNED_ARTIFACTS)){
            for ( Class<?extends Artifact> artifact : bundle.getClassArray(SPAWNED_ARTIFACTS) ){
                removeArtifact(artifact);
            }
            //pre-0.6.1 saves
        } /*else if (bundle.contains("artifacts")) {
            String[] names = bundle.getStringArray("artifacts");
            Category cat = Category.ARTIFACT;

            for (String artifact : names)
                for (int i = 0; i < cat.classes.length; i++)
                    if (cat.classes[i].getSimpleName().equals(artifact))
                        cat.probs[i] = 0;
        }*/
    }
}
