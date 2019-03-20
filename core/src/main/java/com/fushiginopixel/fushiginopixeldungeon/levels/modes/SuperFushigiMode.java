package com.fushiginopixel.fushiginopixeldungeon.levels.modes;

import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Bestiary;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Dragon;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.levels.CavesBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.CavesLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.CavesMidBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.CityBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.CityLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.DeadEndLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.HallsBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.HallsLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.LastLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.LastShopLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.PrisonBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.PrisonLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.PrisonMidBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SewerBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SewerLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SewerMidBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SurfaceLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class SuperFushigiMode extends Mode {

    public Level newLevel(int depth){
        Level level = null;
        if (depth == 0) {
            level = new SurfaceLevel();
        }else if(depth > 0 && depth < 100){
            level = Level.randomLevel();
        }else if(depth == 100){
            level = new LastLevel();
        }

        if(level == null){
            level = new DeadEndLevel();
            Statistics.deepestFloor--;
        }
        return level;
    }

    public static HashMap<Class<? extends Mob>, Float> expAdjust = new HashMap<>();
    static{
        expAdjust.put(Dragon.class,        0.8f);
    }

    public static HashMap<Class<? extends Mob>, Float> rateAdjust = new HashMap<>();
    static{
        rateAdjust.put(Dragon.class,        0.5f);
    }

    @Override
    public int maxDepth() {
        return 100;
    }

    public static final int expLimit = 1;

    @Override
    public ArrayList<Class<? extends Mob>> standardMobRotation(int depth) {
        int averageLevel = depth/2 + 1;
        ArrayList<Class<? extends Mob>> mobs = (ArrayList<Class<? extends Mob>>) Bestiary.spawningMobs().clone();
        ArrayList<Class<? extends Mob>> cmobs = new ArrayList();
        ArrayList<Integer> exps = new ArrayList();
        ArrayList<Class<? extends Mob>> mobsForSpawn = new ArrayList<>();

        for(Class<? extends Mob> m :mobs){
            Mob mob = null;
            try{
                mob = m.newInstance();
            }catch (Exception e){
                Fushiginopixeldungeon.reportException(e);
            }
            int exp = mob.EXP;
            float adjust = 1f;
            for (Class c : expAdjust.keySet()){
                if (c.isAssignableFrom(m)){
                    adjust = expAdjust.get(c);
                }
            }
            if(adjust != 1f){
                exp *= adjust;
            }
            if(mob != null && exp >= averageLevel - expLimit && exp <= averageLevel + expLimit){
                cmobs.add(m);
                exps.add(exp);
            }
        }

        if(!cmobs.isEmpty()) {
            for (int i = 0; i < 20; i++) {
                Class<? extends Mob> m = Random.element(cmobs);
                int dif = Math.abs(exps.get(cmobs.indexOf(m)) - averageLevel);
                float exp = ((expLimit + 2f) - dif) / (expLimit + 2);
                float adjust = 1f;
                for (Class c : rateAdjust.keySet()) {
                    if (c.isAssignableFrom(m)) {
                        adjust = rateAdjust.get(c);
                    }
                }

                if (Random.Float(1) < adjust * exp) {
                    mobsForSpawn.add(m);
                } else {
                    i--;
                }
            }
        }

        return mobsForSpawn;
    }

    @Override
    public boolean bossLevel( int depth ) {
        return depth % 100 == 0;
    }

    @Override
    public boolean enabledGotFromWarehouse() {
        return false;
    }

    @Override
    public float standardShopChance(){
        return 0.1f;
    }
}
