package com.fushiginopixel.fushiginopixeldungeon.levels.modes;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Bestiary;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Dragon;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.PotFairy;
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
import com.fushiginopixel.fushiginopixeldungeon.levels.RegularLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SewerBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SewerLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SewerMidBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.SurfaceLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.standard.StandardRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.DisintegrationTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.GrimTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.Trap;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class SuperFushigiMode extends Mode {

    public Level newLevel(int depth){
        Level level = null;
        if (depth == 0) {
            level = new SurfaceLevel();
        }else if(depth > 0 && depth < maxDepth()){
            level = Level.randomLevel();
        }else if(depth == maxDepth()){
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
        expAdjust.put(PotFairy.class,        2f);
    }

    public static HashMap<Class<? extends Mob>, Float> rateAdjust = new HashMap<>();
    static{
        rateAdjust.put(Dragon.class,        0.5f);
        rateAdjust.put(PotFairy.class,        0.5f);
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

        int aveExp = averageLevel;
        boolean reduce = false;
        while(true) {
            cmobs.clear();
            exps.clear();
            for (Class<? extends Mob> m : mobs) {
                Mob mob = null;
                try {
                    mob = m.newInstance();
                } catch (Exception e) {
                    Fushiginopixeldungeon.reportException(e);
                }
                int exp = mob.EXP;
                float adjust = 1f;
                for (Class c : expAdjust.keySet()) {
                    if (c.isAssignableFrom(m)) {
                        adjust = expAdjust.get(c);
                    }
                }
                if (adjust != 1f) {
                    exp *= adjust;
                }
                if (mob != null && exp >= aveExp - expLimit && exp <= aveExp + expLimit) {
                    cmobs.add(m);
                    exps.add(exp);
                }
            }
            if(cmobs.isEmpty()){
                reduce = true;
            }
            if(reduce && cmobs.size() < 3 && aveExp > 0) {
                aveExp--;
            }else{
                break;
            }
        }

        if(!cmobs.isEmpty()) {
            for (int i = 0; i < 20; i++) {
                Class<? extends Mob> m = Random.element(cmobs);
                int dif = Math.abs(exps.get(cmobs.indexOf(m)) - aveExp);
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

    @Override
    public boolean setLevelTrap(RegularLevel.LevelTraps levelTrap){
        levelTrap.trapClasses = Trap.traps();
        /*
				AlarmTrap.class, BurningTrap.class, ShockingTrap.class, OozeTrap.class, TeleportationTrap.class, ChillingTrap.class, GrippingTrap.class,
				WeakeningTrap.class, SummoningTrap.class, CursingTrap.class, FlockTrap.class,
				ToxicTrap.class, ConfusionTrap.class, CorrosiveGas.class,
				GuardianTrap.class, BlazingTrap.class, StormTrap.class, MucusTrap.class, WarpingTrap.class, FrostTrap.class, FlashingTrap.class,
				PitfallTrap.class, ExplosiveTrap.class, MultiplicationTrap.class, RockfallTrap.class,
				LogTrap.class, PoisonDartTrap.class, DisintegrationTrap.class, WornDartTrap.class,
				DisarmingTrap.class, DistortionTrap.class, GrimTrap.class

                稀有度1：绵羊陷阱、警报陷阱、传送陷阱、损坏飞镖陷阱、召唤陷阱
                稀有度2：电击陷阱、泥浆陷阱、捕猎陷阱、冰霜陷阱、毒气陷阱、落石陷阱
                稀有度3：火焰陷阱、虚弱陷阱、诅咒陷阱、毒镖陷阱、守卫陷阱、眩晕气体陷阱、增殖陷阱
                稀有度4：扭曲陷阱、落穴陷阱、闪光陷阱、黏胶陷阱、暴雪陷阱、酸蚀气体陷阱、原木陷阱
                稀有度5：爆炎陷阱、爆炸陷阱、雷暴陷阱、缴械陷阱、解离陷阱
                稀有度6：即死陷阱、重构陷阱
         */
        ArrayList<Float> rarity = new ArrayList<>();
        Collections.addAll(rarity,1f, 2f, 3f, 4f, 5f, 6f);
        for(Float i: rarity){
            float j = i;
            //216, 125, 64, 27, 8, 1
            i = (7 - i) * (7 - i) * (7 - i);
            //441, 325, 239, 177, 133, 101
            i += ((float)Dungeon.depth) / maxDepth() * (5 - j/2) * 50;
            rarity.set((int)j - 1 , i);
        }

        float[] chances = new float[]{
                1, 3, 2, 2, 1, 2, 2,
                3, 1, 3, 1,
                2, 3, 4,
                3, 5, 5, 4, 4, 4, 4,
                4, 5, 3, 2,
                4, 3, 5, 1,
                5, 6, 6
        };

        for(int i = 0; i < chances.length; i++){
            chances[i] = rarity.get((int) chances[i] - 1);
        }
        //rarity
        levelTrap.trapChances = chances;
        return true;
    }

    public static final HashSet<Class<? extends Trap>> VISIBLE = new HashSet<>();
    static {
        VISIBLE.add(GrimTrap.class);
        VISIBLE.add(DisintegrationTrap.class);
    }

    @Override
    public boolean setTrap(Class<? extends Trap> trapClasses[], float[] trapChances, Level l, int trapPos){
        try {
            Trap trap = trapClasses[Random.chances( trapChances )].newInstance().hide();
            for (Class c : VISIBLE){
                if (!c.isAssignableFrom(trap.getClass()) && Random.Int(4) == 0){
                    trap.alwaysVisible = false;
                    trap.hide();
                }
            }
            l.setTrap( trap, trapPos );
            //some traps will not be hidden
            l.map[trapPos] = trap.visible ? Terrain.TRAP : Terrain.SECRET_TRAP;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
