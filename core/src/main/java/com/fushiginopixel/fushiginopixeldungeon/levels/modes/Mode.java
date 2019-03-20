package com.fushiginopixel.fushiginopixeldungeon.levels.modes;

import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.standard.StandardRoom;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public abstract class Mode implements Bundlable {

    public abstract Level newLevel(int depth);
    public abstract ArrayList<Class<? extends Mob>> standardMobRotation(int depth);
    public abstract boolean bossLevel( int depth );
    //has a chance to add a rarely spawned mobs to the rotation
    public void addRareMobs( int depth, ArrayList<Class<?extends Mob>> rotation ){
    }

    public float standardShopChance(){
        return 0;
    }

    public abstract int maxDepth();

    //switches out regular mobs for their alt versions when appropriate
    public void swapMobAlts(ArrayList<Class<?extends Mob>> rotation){

    }

    public final boolean isNormalMode(){
        return this instanceof NormalMode;
    }

    public StandardRoom createStandardRoom(){
        try {
            return StandardRoom.randomStandardRoom();
        }catch (Exception e){
            Fushiginopixeldungeon.reportException(e);
            return null;
        }
    }

    public ConnectionRoom createConnectionRoom(){
        try {
            return ConnectionRoom.rooms.get(Random.chances(new float[]{10, 10, 10, 10, 10, 10})).newInstance();
        }catch (Exception e){
            Fushiginopixeldungeon.reportException(e);
            return null;
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {

    }

    @Override
    public void storeInBundle(Bundle bundle) {

    }

    public boolean enabledGotFromWarehouse() {
        return true;
    }


    public String name(){
        return Messages.get(this, "name");
    }

    public String desc(){
        return Messages.get(this, "desc");
    }
}
