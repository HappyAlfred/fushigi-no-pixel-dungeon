package com.fushiginopixel.fushiginopixeldungeon.levels.modes;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Acidic;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Bandit;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Bat;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Brute;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Crab;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.CurseGirlSister;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.DM450;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.DarkWolf;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.DeathEye;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Dragon;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.EaterBat;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Elemental;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.FallenAngel;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.FallenAngelNurse;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.GelCube;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Gnoll;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.GnollBomber;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.GnollNinja;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.GoblinBlaster;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.GoblinFanatics;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.GoblinSapper;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.GoldenScorpio;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Golem;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Guard;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.HungryRat;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.InfernoDragon;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.IronScorpio;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Lich;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Monk;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.PatrolDog;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.PoisonSpinner;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Pumpkin;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.PumpkinKing;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Rat;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.RedGelCube;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.RedSlime;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Scorpio;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Senior;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Shaman;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Shielded;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.SilverCrab;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Skeleton;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.SkeletonArcher;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Succubus;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Swarm;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Thief;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.VampireBat;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Warlock;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.WhiteRat;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Zombie;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.ZombieCaptain;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.ZombieSoldier;
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
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.connection.ConnectionRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.standard.StandardRoom;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class NormalMode extends Mode {

    public Level newLevel(int depth){
        Level level = null;
        if (depth == 0)
            level = new SurfaceLevel();
        else if (depth >= 1 && depth <= 9 && depth != 5)
            level = new SewerLevel();
        else if (depth == 5)
            level = new SewerMidBossLevel();
        else if (depth == 10)
            level = new SewerBossLevel();
        else if (depth >= 11 && depth <= 19 && depth != 15)
            level = new PrisonLevel();
        else if (depth == 15)
            level = new PrisonMidBossLevel();
        else if (depth == 20)
            level = new PrisonBossLevel();
        else if (depth >= 21 && depth <= 29 && depth != 25)
            level = new CavesLevel();
        else if (depth == 25)
            level = new CavesMidBossLevel();
        else if (depth == 30)
            level = new CavesBossLevel();
        else if (depth >= 31 && depth <= 39)
            level = new CityLevel();
        else if (depth == 40)
            level = new CityBossLevel();
        else if (depth == 41)
            level = new LastShopLevel();
        else if (depth >= 42 && depth <= 49)
            level = new HallsLevel();
        else if (depth == 50)
            level = new HallsBossLevel();
        else if (depth == 51)
            level = new LastLevel();
        else {
            level = new DeadEndLevel();
            Statistics.deepestFloor--;
        }
        return level;
    }

    @Override
    public ArrayList<Class<? extends Mob>> standardMobRotation(int depth) {
        switch(depth){

            // Sewers
            case 1:
                //10x rat
                return new ArrayList<Class<? extends Mob>>(Arrays.asList(
                        Rat.class, Rat.class, Rat.class, Rat.class, Rat.class,
                        Rat.class, Rat.class, Rat.class, Rat.class, WhiteRat.class));
            case 2:
                //3x rat, 3x gnoll
                return  new ArrayList<Class<? extends Mob>>(Arrays.asList(Rat.class, Rat.class, Rat.class,
                        WhiteRat.class, WhiteRat.class,
                        Bat.class,Bat.class));
            case 3:
                //2x rat, 4x gnoll, 1x crab, 1x swarm
                return new ArrayList<>(Arrays.asList(Rat.class, Rat.class,
                        WhiteRat.class, WhiteRat.class, WhiteRat.class,
                        Bat.class, Bat.class, Bat.class,
                        Gnoll.class));
            case 4:
                //1x rat, 2x gnoll, 3x crab, 1x swarm, 1x bomber
                return new ArrayList<>(Arrays.asList(Rat.class,
                        WhiteRat.class,
                        Bat.class, Bat.class,
                        Gnoll.class, Gnoll.class, Gnoll.class,
                        Zombie.class));
            case 6:
                //1x rat, 2x gnoll, 3x crab, 1x swarm, 1x bomber
                return new ArrayList<>(Arrays.asList(
                        Gnoll.class, Gnoll.class, Gnoll.class,
                        Zombie.class, Zombie.class,
                        Swarm.class,
                        HungryRat.class, HungryRat.class));
            case 7:
                //1x rat, 2x gnoll, 3x crab, 1x swarm, 1x bomber
                return new ArrayList<>(Arrays.asList(
                        Gnoll.class,
                        Zombie.class, Zombie.class,
                        Swarm.class,
                        HungryRat.class, HungryRat.class, HungryRat.class,
                        GelCube.class));
            case 8:
                //1x rat, 2x gnoll, 3x crab, 1x swarm, 1x bomber
                return new ArrayList<>(Arrays.asList(
                        Zombie.class,
                        Swarm.class,
                        HungryRat.class, HungryRat.class, HungryRat.class, HungryRat.class,
                        Crab.class,
                        GelCube.class));
            case 9:
                //1x rat, 2x gnoll, 3x crab, 1x swarm, 1x bomber
                return new ArrayList<>(Arrays.asList(
                        Swarm.class,
                        HungryRat.class, HungryRat.class,
                        GelCube.class, GelCube.class,
                        Crab.class, Crab.class,
                        GnollBomber.class));

            // Prison
            case 11:
                //3x skeleton, 1x thief, 1x swarm, 2x bomber
                return new ArrayList<>(Arrays.asList(
                        Crab.class, Crab.class, Crab.class,
                        Thief.class,
                        Skeleton.class, Skeleton.class,
                        GnollBomber.class, GnollBomber.class));
            case 12:
                //3x skeleton, 1x thief, 1x shaman, 1x guard, 2x bomber
                return new ArrayList<>(Arrays.asList(
                        Thief.class,
                        Pumpkin.class,Pumpkin.class,
                        GnollBomber.class, GnollBomber.class,
                        Skeleton.class, Skeleton.class, Skeleton.class));
            case 13:
                //2x skeleton, 1x thief, 2x shaman, 2x guard, 1x archer
                return new ArrayList<>(Arrays.asList(Skeleton.class, Skeleton.class,
                        Thief.class,
                        Pumpkin.class, Pumpkin.class,
                        GnollBomber.class,
                        PatrolDog.class,
                        Skeleton.class, Skeleton.class, Skeleton.class));
            case 14:
                //1x skeleton, 1x thief, 2x shaman, 3x guard, 2x archer
                return new ArrayList<>(Arrays.asList(
                        Skeleton.class, Skeleton.class,
                        Pumpkin.class, Pumpkin.class,
                        PatrolDog.class, PatrolDog.class, PatrolDog.class,
                        SkeletonArcher.class));
            case 16:
                //1x skeleton, 1x thief, 2x shaman, 3x guard, 2x archer
                return new ArrayList<>(Arrays.asList(
                        Pumpkin.class,
                        PatrolDog.class,PatrolDog.class,PatrolDog.class,
                        SkeletonArcher.class,SkeletonArcher.class,SkeletonArcher.class,
                        GoblinSapper.class));
            case 17:
                //1x skeleton, 1x thief, 2x shaman, 3x guard, 2x archer
                return new ArrayList<>(Arrays.asList(Guard.class,
                        PatrolDog.class,PatrolDog.class,PatrolDog.class,
                        SkeletonArcher.class,SkeletonArcher.class,
                        GoblinSapper.class,
                        Guard.class,Guard.class));
            case 18:
                //1x skeleton, 1x thief, 2x shaman, 3x guard, 2x archer
                return new ArrayList<>(Arrays.asList(
                        GoblinSapper.class, GoblinSapper.class,
                        SkeletonArcher.class, SkeletonArcher.class,
                        FallenAngel.class,
                        Guard.class, Guard.class, Guard.class));
            case 19:
                //1x skeleton, 1x thief, 2x shaman, 3x guard, 2x archer
                return new ArrayList<>(Arrays.asList(
                        GoblinSapper.class,
                        Guard.class, Guard.class, Guard.class,
                        FallenAngel.class, FallenAngel.class,
                        EaterBat.class,
                        GnollNinja.class));

            // Caves
            case 21:
                //5x bat, 1x brute, 2x archer
                return new ArrayList<>(Arrays.asList(
                        EaterBat.class, EaterBat.class, EaterBat.class, EaterBat.class,
                        GnollNinja.class,
                        PoisonSpinner.class,
                        Dragon.class,
                        FallenAngel.class));
            case 22:
                //5x bat, 5x brute, 1x spinner, 1x archer
                return new ArrayList<>(Arrays.asList(
                        EaterBat.class, EaterBat.class, EaterBat.class,
                        Dragon.class,
                        PoisonSpinner.class, PoisonSpinner.class,
                        GnollNinja.class,
                        FallenAngel.class));
            case 23:
                //1x bat, 3x brute, 1x shaman, 1x spinner, 1x silvercrab
                return new ArrayList<>(Arrays.asList(
                        EaterBat.class, EaterBat.class,
                        PoisonSpinner.class, PoisonSpinner.class, PoisonSpinner.class,
                        GnollNinja.class,
                        CurseGirlSister.class,
                        Dragon.class));
            case 24:
                //1x bat, 3x brute, 1x shaman, 4x spinner, 2x silvercrab
                return new ArrayList<>(Arrays.asList(
                        GnollNinja.class, GnollNinja.class,
                        PoisonSpinner.class, PoisonSpinner.class, PoisonSpinner.class,
                        Dragon.class,
                        CurseGirlSister.class,
                        ZombieSoldier.class));
            case 26:
                //1x bat, 3x brute, 1x shaman, 4x spinner, 2x silvercrab
                return new ArrayList<>(Arrays.asList(
                        ZombieSoldier.class, ZombieSoldier.class, ZombieSoldier.class,
                        Dragon.class,
                        SilverCrab.class,
                        CurseGirlSister.class,
                        Brute.class, Brute.class));
            case 27:
                //1x bat, 3x brute, 1x shaman, 4x spinner, 2x silvercrab
                return new ArrayList<>(Arrays.asList(
                        CurseGirlSister.class,CurseGirlSister.class,
                        ZombieSoldier.class, ZombieSoldier.class,
                        SilverCrab.class,
                        Brute.class, Brute.class,
                        RedSlime.class));
            case 28:
                //1x bat, 3x brute, 1x shaman, 4x spinner, 2x silvercrab
                return new ArrayList<>(Arrays.asList(
                        ZombieSoldier.class,
                        SilverCrab.class, SilverCrab.class,
                        Brute.class,Brute.class, Brute.class,
                        RedSlime.class,
                        IronScorpio.class
                ));
            case 29:
                //1x bat, 3x brute, 1x shaman, 4x spinner, 2x silvercrab
                return new ArrayList<>(Arrays.asList(
                        Elemental.class,
                        Brute.class, Brute.class,
                        IronScorpio.class,
                        RedSlime.class,
                        SilverCrab.class, SilverCrab.class, SilverCrab.class));

            // City
            case 31:
                //5x elemental, 5x warlock, 1x monk, 2x silvercrab
                return new ArrayList<>(Arrays.asList(
                        Elemental.class, Elemental.class, Elemental.class, Elemental.class,
                        IronScorpio.class, IronScorpio.class,
                        Monk.class,
                        Shaman.class));
            case 32:
                //2x elemental, 2x warlock, 2x monk, 1x silvercrab
                return new ArrayList<>(Arrays.asList(
                        Elemental.class, Elemental.class, Elemental.class,
                        IronScorpio.class,
                        Monk.class, Monk.class,
                        Shaman.class,
                        PumpkinKing.class));
            case 33:
                //1x elemental, 1x warlock, 2x monk, 1x golem
                return new ArrayList<>(Arrays.asList(
                        Elemental.class,
                        Shaman.class,
                        Monk.class, Monk.class, Monk.class,
                        PumpkinKing.class, PumpkinKing.class,
                        RedGelCube.class));
            case 34:
                //1x elemental, 1x warlock, 2x monk, 3x golem
                return new ArrayList<>(Arrays.asList(
                        Monk.class,
                        Shaman.class, Shaman.class,
                        PumpkinKing.class, PumpkinKing.class,
                        RedGelCube.class,
                        Golem.class, Golem.class));
            case 35:
                //1x elemental, 1x warlock, 2x monk, 3x golem
                return new ArrayList<>(Arrays.asList(
                        GnollBomber.class, GnollBomber.class,
                        GnollNinja.class, GnollNinja.class,
                        GoblinSapper.class, GoblinSapper.class,
                        DM450.class, DM450.class));
            case 36:
                //1x elemental, 1x warlock, 2x monk, 3x golem
                return new ArrayList<>(Arrays.asList(
                        PumpkinKing.class,
                        RedGelCube.class,
                        Warlock.class,
                        DM450.class, DM450.class,
                        Golem.class, Golem.class, Golem.class));
            case 37:
                //1x elemental, 1x warlock, 2x monk, 3x golem
                return new ArrayList<>(Arrays.asList(
                        DM450.class,DM450.class,
                        RedGelCube.class,
                        Warlock.class, Warlock.class,
                        Golem.class, Golem.class, Golem.class,
                        DarkWolf.class));
            case 38:
                //1x elemental, 1x warlock, 2x monk, 3x golem
                return new ArrayList<>(Arrays.asList(
                        DM450.class,
                        Golem.class, Golem.class,
                        Warlock.class,
                        Lich.class,
                        DarkWolf.class, DarkWolf.class, DarkWolf.class));
            case 39:
                //1x elemental, 1x warlock, 2x monk, 3x golem
                return new ArrayList<>(Arrays.asList(
                        DM450.class,
                        Warlock.class, Warlock.class,
                        Lich.class,
                        DarkWolf.class, DarkWolf.class, DarkWolf.class,
                        GoblinFanatics.class));

            // Halls
            case 42:
                //3x succubus, 3x evil eye
                return new ArrayList<>(Arrays.asList(
                        Succubus.class, Succubus.class, Succubus.class,
                        GoblinFanatics.class, GoblinFanatics.class,
                        Lich.class,
                        VampireBat.class, VampireBat.class));
            case 43:
                //2x succubus, 4x evil eye, 2x scorpio
                return new ArrayList<>(Arrays.asList(
                        Succubus.class, Succubus.class,
                        Lich.class,
                        GoblinFanatics.class,
                        VampireBat.class, VampireBat.class, VampireBat.class,
                        ZombieCaptain.class));
            case 44:
                //1x succubus, 2x evil eye, 3x scorpio
                return new ArrayList<>(Arrays.asList(
                        GoblinFanatics.class,
                        Succubus.class, Succubus.class,
                        VampireBat.class, VampireBat.class,
                        FallenAngelNurse.class,
                        ZombieCaptain.class, ZombieCaptain.class));
            case 45:
                //1x succubus, 2x evil eye, 3x scorpio
                return new ArrayList<>(Arrays.asList(
                        RedSlime.class, RedSlime.class,
                        IronScorpio.class, IronScorpio.class,
                        CurseGirlSister.class, CurseGirlSister.class,
                        RedGelCube.class, RedGelCube.class,
                        GoldenScorpio.class));
            case 46:
                //1x succubus, 2x evil eye, 3x scorpio
                return new ArrayList<>(Arrays.asList(
                        ZombieCaptain.class, ZombieCaptain.class, ZombieCaptain.class,
                        FallenAngelNurse.class, FallenAngelNurse.class,
                        DeathEye.class, DeathEye.class,
                        GoldenScorpio.class));
            case 47:
                //1x succubus, 2x evil eye, 3x scorpio
                return new ArrayList<>(Arrays.asList(
                        FallenAngelNurse.class,
                        GoldenScorpio.class, GoldenScorpio.class,
                        DeathEye.class, DeathEye.class, DeathEye.class, DeathEye.class,
                        InfernoDragon.class));
            case 48:
                //1x succubus, 2x evil eye, 3x scorpio
                return new ArrayList<>(Arrays.asList(
                        FallenAngelNurse.class,
                        DeathEye.class, DeathEye.class, DeathEye.class, DeathEye.class,
                        GoldenScorpio.class, GoldenScorpio.class,
                        InfernoDragon.class));
            case 49:
                //1x succubus, 2x evil eye, 3x scorpio
                return new ArrayList<>(Arrays.asList(
                        DeathEye.class, DeathEye.class, DeathEye.class, DeathEye.class,
                        InfernoDragon.class));
            default:
                return new ArrayList<>();
        }
    }

    //switches out regular mobs for their alt versions when appropriate
    public void swapMobAlts(ArrayList<Class<?extends Mob>> rotation){
        for (int i = 0; i < rotation.size(); i++){
            if (Random.Int( 50 ) == 0) {
                Class<? extends Mob> cl = rotation.get(i);
                if (cl == Rat.class) {
                    cl = WhiteRat.class;
                } else if (cl == Thief.class) {
                    cl = Bandit.class;
                } else if (cl == Brute.class) {
                    cl = Shielded.class;
                } else if (cl == Monk.class) {
                    cl = Senior.class;
                } else if (cl == Scorpio.class) {
                    cl = Acidic.class;
                }
                rotation.set(i, cl);
            }
        }
    }

    @Override
    public StandardRoom createStandardRoom(){
        try {
            return StandardRoom.rooms.get(Random.chances(StandardRoom.chances[Dungeon.depth])).newInstance();
        }catch (Exception e){
            Fushiginopixeldungeon.reportException(e);
            return null;
        }
    }

    @Override
    public ConnectionRoom createConnectionRoom(){
        try {
            return ConnectionRoom.rooms.get(Random.chances(ConnectionRoom.chances[Dungeon.depth])).newInstance();
        }catch (Exception e){
            Fushiginopixeldungeon.reportException(e);
            return null;
        }
    }

    @Override
    public int maxDepth() {
        return 51;
    }

    @Override
    public boolean bossLevel( int depth ) {
        return depth % 5 == 0;
    }
}
