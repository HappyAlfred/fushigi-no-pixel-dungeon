/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.fushiginopixel.fushiginopixeldungeon.actors.mobs;

import com.fushiginopixel.fushiginopixeldungeon.levels.modes.Mode;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;

public class Bestiary {
	
	public static ArrayList<Class<? extends Mob>> getMobRotation( int depth, Mode mode ){
		ArrayList<Class<? extends Mob>> mobs = mode.standardMobRotation( depth );
		mode.addRareMobs(depth, mobs);
		mode.swapMobAlts(mobs);
		Random.shuffle(mobs);
		return mobs;
	}

	public static ArrayList<Class<? extends Mob>> getStandardMobRotation( int depth, Mode mode  ){
		ArrayList<Class<? extends Mob>> mobs = mode.standardMobRotation( depth );
		return mobs;
	}
	
	//returns a rotation of standard mobs, unshuffled.
	private static ArrayList<Class<? extends Mob>> standardMobRotation( int depth ){
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

	//returns a rotation of ShopGuardian, unshuffled.
	public static ArrayList<Class<? extends Mob>> getGuardRotation(){
		//10x ShopGuards
		return new ArrayList<Class<? extends Mob>>(Arrays.asList(
				ShopGuardian.class, ShopGuardian.class, ShopGuardian.class, ShopGuardian.class, ShopGuardian.class,
				ShopGuardian.class, ShopGuardian.class, ShopGuardian.class, ShopGuardian.class, ShopGuardian.class,
				ShopGuardianFlying.class,
				ShopGuardianQuick.class,
				ShopGuardianSuper.class,
				ShopGuardianRanger.class,
				ShopGuardianScout.class));
	}
	
	//has a chance to add a rarely spawned mobs to the rotation
	public static void addRareMobs( int depth, ArrayList<Class<?extends Mob>> rotation ){
		
		switch (depth){
			
			// Sewers
			default:
				return;
			case 4:
				//if (Random.Float() < 0.01f) rotation.add(Skeleton.class);
				//if (Random.Float() < 0.01f) rotation.add(Thief.class);
				return;
				
			// Prison
			case 11:
				//if (Random.Float() < 0.2f)  rotation.add(Shaman.class);
				return;
			case 13:
				//if (Random.Float() < 0.02f) rotation.add(Bat.class);
				return;
			case 14:
				//if (Random.Float() < 0.02f) rotation.add(Bat.class);
				//if (Random.Float() < 0.01f) rotation.add(Brute.class);
				return;
				
			// Caves
			case 23:
				//if (Random.Float() < 0.02f) rotation.add(Elemental.class);
				return;
			case 24:
				//if (Random.Float() < 0.02f) rotation.add(Elemental.class);
				//if (Random.Float() < 0.01f) rotation.add(Monk.class);
				return;
				
			// City
			case 34:
				//if (Random.Float() < 0.02f) rotation.add(Succubus.class);
				return;
		}
	}
	
	//switches out regular mobs for their alt versions when appropriate
	private static void swapMobAlts(ArrayList<Class<?extends Mob>> rotation){
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

	public static ArrayList<Class<?extends Mob>> spawningMobs(){
		return new ArrayList<Class<? extends Mob>>(Arrays.asList(
				Bat.class, EaterBat.class, VampireBat.class, SpiritBat.class,
				Brute.class,
				Crab.class, SilverCrab.class,
				CurseGirl.class, CurseGirlSister.class,
				DeathEye.class,
				DM450.class,
				Dragon.class, InfernoDragon.class, FlareDragon.class,
				Elemental.class,
				FallenAngel.class, FallenAngelNurse.class, FallenAngelDoctor.class,
				GelCube.class, RedGelCube.class,
				GoblinSapper.class, GoblinFanatics.class,
				Golem.class,
				Gnoll.class, GnollBomber.class, GnollNinja.class, Shaman.class,
				HungryRat.class, InfernoDragon.class, IronScorpio.class,
				Lich.class,
				Monk.class,
				PatrolDog.class, DarkWolf.class,
				PotFairy.class, PotYoukai.class,
				Pumpkin.class, PumpkinKing.class,
				Rat.class, WhiteRat.class, HungryRat.class,
				Scorpio.class, IronScorpio.class, GoldenScorpio.class,
				Skeleton.class, SkeletonArcher.class, Lich.class,
				Slime.class, RedSlime.class,
				Spinner.class, PoisonSpinner.class,
				Swarm.class,
				Thief.class,
				Warlock.class,
				Zombie.class, ZombieSoldier.class, ZombieCaptain.class
				));

	}
}
