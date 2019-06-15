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

package com.fushiginopixel.fushiginopixeldungeon.items;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.CaneArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.ClothArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.GoldArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.LeafArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.LeatherArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.LightningConchShell;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.MailArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.PlateArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.SamuraiArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.ScaleArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.SilkSuit;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.WoodenArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.AlchemistsToolkit;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.Artifact;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.CapeOfThorns;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.ChaliceOfBlood;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.CloakOfShadows;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.DriedRose;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.EtherealChains;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.HornOfPlenty;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.LloydsBeacon;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.MasterThievesArmband;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.SandalsOfNature;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.TalismanOfForesight;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.TimekeepersHourglass;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.UnstableSpellbook;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bomb;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bombs;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.CannonBall;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Firework;
import com.fushiginopixel.fushiginopixeldungeon.items.food.Food;
import com.fushiginopixel.fushiginopixeldungeon.items.food.MysteryMeat;
import com.fushiginopixel.fushiginopixeldungeon.items.food.BigOnigiri;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfBeverage;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfExperience;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfExplode;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfFrost;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfHealing;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfInvisibility;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfLevitation;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfLiquidFlame;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfMight;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfMindVision;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfPanacea;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfParalyticGas;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfPurity;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfStrength;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfTearGas;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfVenom;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.PotOfAlchemy;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.PotOfCannon;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.PotOfDispel;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.PotOfTradeGold;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.PotOfFreeze;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.PotOfFusion;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.PotOfIdentify;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.PotOfRestructure;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.PotOfStorge;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.PotOfTransmutation;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfAccuracy;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfElements;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfEnergy;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfEvasion;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfAlert;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfFuror;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfHaste;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfKnowledge;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfMight;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfSharpshooting;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfTenacity;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfWealth;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.Scroll;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfAffection;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfExpand;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfExsuction;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfIdentify;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfLullaby;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfMagicalInfusion;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfOnigiri;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfRage;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfRecharging;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfSelfDestruct;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfTerror;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.specialscrolls.ScrollOfEscape;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfBlastWave;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfCorrosion;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfCorruption;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfDisintegration;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfElements;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfFireblast;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfFrost;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfHoly;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfKaleidoscope;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfLightning;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfMagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfMagician;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfNightmare;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfPrismaticLight;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfTransfusion;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.AssassinsBlade;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.BattleAxe;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Crossbow;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Dagger;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Flail;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.FuhmaKatana;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Gauntlet;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Glaive;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Goldsword;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Gradius;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Greataxe;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Greatshield;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Greatsword;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.HandAxe;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.InfernoFuhmaKatana;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Katana;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Knuckles;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Longsword;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Mace;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MagesStaff;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Mattock;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MeleeWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Quarterstaff;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.RoundShield;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.RunicBlade;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Sai;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Scimitar;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Spear;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Sword;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.WarHammer;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Whip;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.WornShortsword;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Bolas;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.FishingSpear;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Javelin;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.MissileWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Shuriken;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.TestingShuriken;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.ThrowingHammer;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.ThrowingSpear;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.ThrowingStone;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Tomahawk;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Trident;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.darts.Dart;
import com.fushiginopixel.fushiginopixeldungeon.plants.BlandfruitBush;
import com.fushiginopixel.fushiginopixeldungeon.plants.Blindweed;
import com.fushiginopixel.fushiginopixeldungeon.plants.Dreamfoil;
import com.fushiginopixel.fushiginopixeldungeon.plants.Earthroot;
import com.fushiginopixel.fushiginopixeldungeon.plants.Fadeleaf;
import com.fushiginopixel.fushiginopixeldungeon.plants.Firebloom;
import com.fushiginopixel.fushiginopixeldungeon.plants.Icecap;
import com.fushiginopixel.fushiginopixeldungeon.plants.Plant;
import com.fushiginopixel.fushiginopixeldungeon.plants.Rotberry;
import com.fushiginopixel.fushiginopixeldungeon.plants.Sorrowmoss;
import com.fushiginopixel.fushiginopixeldungeon.plants.Starflower;
import com.fushiginopixel.fushiginopixeldungeon.plants.Stormvine;
import com.fushiginopixel.fushiginopixeldungeon.plants.Sungrass;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {

	public enum Category {
		WEAPON	( 6,    MeleeWeapon.class),
		WEP_T1	( 0,    MeleeWeapon.class),
		WEP_T2	( 0,    MeleeWeapon.class),
		WEP_T3	( 0,    MeleeWeapon.class),
		WEP_T4	( 0,    MeleeWeapon.class),
		WEP_T5	( 0,    MeleeWeapon.class),
		
		ARMOR	( 4,    Armor.class ),
		ARM_T1	( 0,    Armor.class),
		ARM_T2	( 0,    Armor.class),
		ARM_T3	( 0,    Armor.class),
		ARM_T4	( 0,    Armor.class),
		ARM_T5	( 0,    Armor.class),
		
		MISSILE ( 3,    MissileWeapon.class ),
		MIS_T1  ( 0,    MissileWeapon.class ),
		MIS_T2  ( 0,    MissileWeapon.class ),
		MIS_T3  ( 0,    MissileWeapon.class ),
		MIS_T4  ( 0,    MissileWeapon.class ),
		MIS_T5  ( 0,    MissileWeapon.class ),
		
		POTION	( 20,   Potion.class ),
		SCROLL	( 20,   Scroll.class ),
		POT	( 10,   Pot.class ),
		
		WAND	( 3,    Wand.class ),
		RING	( 1,    Ring.class ),
		ARTIFACT( 1,    Artifact.class),
		
		SEED	( 0,    Plant.Seed.class ),
		
		FOOD	( 15,    Food.class ),

		BOMBS(1, 	Bombs.class),
		
		GOLD	( 20,   Gold.class );
		
		public Class<?>[] classes;
		public float[] probs;
		
		public float prob;
		public Class<? extends Item> superClass;
		
		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}
		
		private static final float[] INITIAL_ARTIFACT_PROBS = new float[]{ 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 1, 0, 1};
		
		static {
			GOLD.classes = new Class<?>[]{
					Gold.class };
			GOLD.probs = new float[]{ 1 };
			
			SCROLL.classes = new Class<?>[]{
					ScrollOfIdentify.class,
					ScrollOfTeleportation.class,
					ScrollOfRemoveCurse.class,
					ScrollOfUpgrade.class,
					ScrollOfRecharging.class,
					ScrollOfMagicMapping.class,
					ScrollOfRage.class,
					ScrollOfTerror.class,
					ScrollOfLullaby.class,
					ScrollOfMagicalInfusion.class,
					ScrollOfPsionicBlast.class,
					ScrollOfMirrorImage.class,

					ScrollOfExpand.class,
					ScrollOfOnigiri.class,
					ScrollOfExsuction.class,
					ScrollOfSelfDestruct.class,
					ScrollOfAffection.class,

					ScrollOfEscape.class};
			SCROLL.probs = new float[]{ 25, 10, 20, 30, 15, 15, 12, 8, 8, 0, 8, 10,  15, 8, 8, 8, 8  ,8 };
			
			POTION.classes = new Class<?>[]{
					PotionOfHealing.class,
					PotionOfExperience.class,
					PotionOfToxicGas.class,
					PotionOfParalyticGas.class,
					PotionOfLiquidFlame.class,
					PotionOfLevitation.class,
					PotionOfStrength.class,
					PotionOfMindVision.class,
					PotionOfPurity.class,
					PotionOfInvisibility.class,
					PotionOfMight.class,
					PotionOfFrost.class,

					PotionOfPanacea.class,
					PotionOfTearGas.class,
					PotionOfVenom.class,
					PotionOfExplode.class,
					PotionOfBeverage.class };
			POTION.probs = new float[]{ 45, 10, 15, 10, 15, 10, 10, 20, 12, 10, 0, 10, 	15, 10, 15, 10, 10 };

			POT.classes = new Class<?>[]{
					PotOfIdentify.class,
					PotOfTransmutation.class,
					PotOfRestructure.class,
					PotOfTradeGold.class,
					PotOfFusion.class,
					PotOfFreeze.class,
					PotOfStorge.class,
					PotOfAlchemy.class,
                    PotOfDispel.class,
					PotOfCannon.class,
			};
			POT.probs = new float[]{ 20, 10, 10, 10, 20, 10, 20, 10, 10, 10};
			
			//TODO: add last ones when implemented
			WAND.classes = new Class<?>[]{
					WandOfMagicMissile.class,
					WandOfLightning.class,
					WandOfDisintegration.class,
					WandOfFireblast.class,
					WandOfCorrosion.class,
					WandOfBlastWave.class,
					//WandOfLivingEarth.class,
					//WandOfFrost.class,
					WandOfPrismaticLight.class,
					//WandOfWarding.class,
					//WandOfTransfusion.class,
					WandOfHoly.class,
					WandOfCorruption.class,
					//WandOfRegrowth.class

					WandOfElements.class,
					WandOfNightmare.class,
					WandOfKaleidoscope.class,
					WandOfMagician.class,
					};
			WAND.probs = new float[]{ 5, 4, 4, 4, 4, 3, /*3, 3,*/ 3, /*3, 3,*/ 3/*, 3*/, 4, 4, 4, 4, 4 };
			
			//see generator.randomWeapon
			WEAPON.classes = new Class<?>[]{};
			WEAPON.probs = new float[]{};
			
			WEP_T1.classes = new Class<?>[]{
					WornShortsword.class,
					Sword.class,
					Longsword.class,
					Greatsword.class,
					Knuckles.class,
					Dagger.class,
					MagesStaff.class
			};
			WEP_T1.probs = new float[]{15, 30, 15, 7, 15, 15, 0 };
			
			WEP_T2.classes = new Class<?>[]{
					HandAxe.class,
					Spear.class,
					Quarterstaff.class,
					Goldsword.class,
					Mace.class,
					Scimitar.class,
					RoundShield.class
			};
			WEP_T2.probs = new float[]{ 6, 6, 5, 3, 5, 5, 4 };
			
			WEP_T3.classes = new Class<?>[]{
					Mattock.class,
					Sai.class,
					Whip.class,
					Katana.class,
					Flail.class,
					AssassinsBlade.class,
					Crossbow.class
			};
			WEP_T3.probs = new float[]{  3, 4, 4, 4, 6, 5, 6 };
			
			WEP_T4.classes = new Class<?>[]{
					BattleAxe.class,
					WarHammer.class,
					Glaive.class,
					Greataxe.class,
					Greatshield.class,
					Gauntlet.class,
					FuhmaKatana.class
			};
			WEP_T4.probs = new float[]{ 6, 5, 5, 4, 4, 4 ,3};
			
			WEP_T5.classes = new Class<?>[]{
					RunicBlade.class,
					InfernoFuhmaKatana.class,
					Gradius.class
			};
			WEP_T5.probs = new float[]{ 6, 6, 2 };
			
			//see Generator.randomArmor
			/*ARMOR.classes = new Class<?>[]{
					ClothArmor.class,
					LeatherArmor.class,
					MailArmor.class,
					ScaleArmor.class,
					PlateArmor.class };
			ARMOR.probs = new float[]{ 0, 0, 0, 0, 0 };*/

			ARMOR.classes = new Class<?>[]{};
			ARMOR.probs = new float[]{};

			ARM_T1.classes = new Class<?>[]{
				ClothArmor.class,
                SilkSuit.class
			};
			ARM_T1.probs = new float[]{6 ,5};

			ARM_T2.classes = new Class<?>[]{
					LeatherArmor.class,
					WoodenArmor.class,
					GoldArmor.class
			};
			ARM_T2.probs = new float[]{6 ,5 ,3};

			ARM_T3.classes = new Class<?>[]{
					MailArmor.class,
                    LeafArmor.class,
					CaneArmor.class
			};
			ARM_T3.probs = new float[]{6, 5, 3};

			ARM_T4.classes = new Class<?>[]{
					ScaleArmor.class,
					SamuraiArmor.class,
					LightningConchShell.class
			};
			ARM_T4.probs = new float[]{6, 3, 2};

			ARM_T5.classes = new Class<?>[]{
					PlateArmor.class,
			};
			ARM_T5.probs = new float[]{6};
			
			//see Generator.randomMissile
			MISSILE.classes = new Class<?>[]{};
			MISSILE.probs = new float[]{};
			
			MIS_T1.classes = new Class<?>[]{
					Dart.class,
					ThrowingKnife.class,
					ThrowingStone.class
			};
			MIS_T1.probs = new float[]{ 1, 1 };
			
			MIS_T2.classes = new Class<?>[]{
					FishingSpear.class,
					Shuriken.class
			};
			MIS_T2.probs = new float[]{ 4, 3 };
			
			MIS_T3.classes = new Class<?>[]{
					ThrowingSpear.class,
					Bolas.class,
					TestingShuriken.class
			};
			MIS_T3.probs = new float[]{ 4, 3 ,1};
			
			MIS_T4.classes = new Class<?>[]{
					Javelin.class,
					Tomahawk.class
			};
			MIS_T4.probs = new float[]{ 4, 3 };
			
			MIS_T5.classes = new Class<?>[]{
					Trident.class,
					ThrowingHammer.class
			};
			MIS_T5.probs = new float[]{ 4, 3 };
			
			FOOD.classes = new Class<?>[]{
					Food.class,
					BigOnigiri.class,
					MysteryMeat.class };
			FOOD.probs = new float[]{ 4, 1, 0 };

			BOMBS.classes = new Class<?>[]{
					Firework.class,
					Bomb.class,
					CannonBall.class };
			BOMBS.probs = new float[]{ 2, 1, 2};

			RING.classes = new Class<?>[]{
					RingOfAccuracy.class,
					RingOfEvasion.class,
					RingOfElements.class,
					RingOfAlert.class,
					RingOfFuror.class,
					RingOfHaste.class,
					//RingOfEnergy.class,
					RingOfMight.class,
					RingOfSharpshooting.class,
					RingOfTenacity.class,
					RingOfWealth.class,
					RingOfKnowledge.class};
			RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, /*1,*/ 1, 1, 1, 1, 1 };
			
			ARTIFACT.classes = new Class<?>[]{
					CapeOfThorns.class,
					ChaliceOfBlood.class,
					CloakOfShadows.class,
					HornOfPlenty.class,
					MasterThievesArmband.class,
					SandalsOfNature.class,
					TalismanOfForesight.class,
					TimekeepersHourglass.class,
					UnstableSpellbook.class,
					AlchemistsToolkit.class, //currently removed from drop tables, pending rework.
					DriedRose.class,
					LloydsBeacon.class,
					EtherealChains.class
			};
			ARTIFACT.probs = INITIAL_ARTIFACT_PROBS.clone();
			
			SEED.classes = new Class<?>[]{
					Firebloom.Seed.class,
					Icecap.Seed.class,
					Sorrowmoss.Seed.class,
					Blindweed.Seed.class,
					Sungrass.Seed.class,
					Earthroot.Seed.class,
					Fadeleaf.Seed.class,
					Rotberry.Seed.class,
					BlandfruitBush.Seed.class,
					Dreamfoil.Seed.class,
					Stormvine.Seed.class,
					Starflower.Seed.class};
			SEED.probs = new float[]{ 10, 10, 10, 10, 10, 10, 10, 0, 2, 10, 10, 1 };
		}
	}

	private static final float[][] floorSetTierProbs = new float[][] {
			{50, 30, 15, 5,  0},
			{30, 40, 20, 10, 0},
			{20, 35, 30, 15, 0},
			{20,  25, 34, 20, 1},
			{20,  20,  30, 25, 5}
			/*{0, 70, 20,  8,  2},
			{0, 25, 50, 20,  5},
			{0, 10, 40, 40, 10},
			{0,  5, 20, 50, 25},
			{0,  2,  8, 20, 70}*/
	};
	
	private static HashMap<Category,Float> categoryProbs = new LinkedHashMap<>();
	
	public static void reset() {
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}

	public static Category randomCategory() {
		Category cat = Random.chances( categoryProbs );
		if (cat == null){
			reset();
			cat = Random.chances( categoryProbs );
		}
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return cat;
	}
	
	public static Item random() {
		Category cat = Random.chances( categoryProbs );
		if (cat == null){
			reset();
			cat = Random.chances( categoryProbs );
		}
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return random( cat );
	}
	
	public static Item random( Category cat ) {
		try {
			
			switch (cat) {
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			case MISSILE:
				return randomMissile();
			case ARTIFACT:
				Item item = randomArtifact();
				//if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
			default:
				return ((Item)cat.classes[Random.chances( cat.probs )].newInstance()).random();
			}
			
		} catch (Exception e) {

			Fushiginopixeldungeon.reportException(e);
			return null;
			
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		try {
			
			return ((Item)cl.newInstance()).random();
			
		} catch (Exception e) {

			Fushiginopixeldungeon.reportException(e);
			return null;
			
		}
	}

	public static final Category[] armTiers = new Category[]{
			Category.ARM_T1,
			Category.ARM_T2,
			Category.ARM_T3,
			Category.ARM_T4,
			Category.ARM_T5
	};

	public static Armor randomArmor(){
		return randomArmor(Dungeon.depth / 10);
	}
	
	public static Armor randomArmor(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		try {
			//Armor a = (Armor)Category.ARMOR.classes[Random.chances(floorSetTierProbs[floorSet])].newInstance();
			Category c = armTiers[Random.chances(floorSetTierProbs[floorSet])];
			Armor a = (Armor) c.classes[Random.chances(c.probs)].newInstance();
			a.random();
			return a;
		} catch (Exception e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}
	}

	public static final Category[] wepTiers = new Category[]{
			Category.WEP_T1,
			Category.WEP_T2,
			Category.WEP_T3,
			Category.WEP_T4,
			Category.WEP_T5
	};

	public static MeleeWeapon randomWeapon(){
		return randomWeapon(Dungeon.depth / 10);
	}
	
	public static MeleeWeapon randomWeapon(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);

		try {
			Category c = wepTiers[Random.chances(floorSetTierProbs[floorSet])];
			MeleeWeapon w = (MeleeWeapon)c.classes[Random.chances(c.probs)].newInstance();
			w.random();
			return w;
		} catch (Exception e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}
	}
	
	public static final Category[] misTiers = new Category[]{
			Category.MIS_T1,
			Category.MIS_T2,
			Category.MIS_T3,
			Category.MIS_T4,
			Category.MIS_T5
	};
	
	public static MissileWeapon randomMissile(){
		return randomMissile(Dungeon.depth / 10);
	}
	
	public static MissileWeapon randomMissile(int floorSet) {
		
		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		try {
			Category c = misTiers[Random.chances(floorSetTierProbs[floorSet])];
			MissileWeapon w = (MissileWeapon)c.classes[Random.chances(c.probs)].newInstance();
			w.random();
			return w;
		} catch (Exception e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		try {
			Category cat = Category.ARTIFACT;
			int i = Random.chances( cat.probs );

			//if no artifacts are left, return null
			if (i == -1){
				return null;
			}
			
			Class<?extends Artifact> art = (Class<? extends Artifact>) cat.classes[i];

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

	public static boolean removeArtifact(Class<?extends Artifact> artifact) {
		if (spawnedArtifacts.contains(artifact))
			return false;

		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++)
			if (cat.classes[i].equals(artifact)) {
				if (cat.probs[i] == 1){
					cat.probs[i] = 0;
					spawnedArtifacts.add(artifact);
					return true;
				} else
					return false;
			}

		return false;
	}

	//resets artifact probabilities, for new dungeons
	public static void initArtifacts() {
		Category.ARTIFACT.probs = Category.INITIAL_ARTIFACT_PROBS.clone();
		spawnedArtifacts = new ArrayList<>();
	}

	private static ArrayList<Class<?extends Artifact>> spawnedArtifacts = new ArrayList<>();
	
	private static final String GENERAL_PROBS = "general_probs";
	private static final String SPAWNED_ARTIFACTS = "spawned_artifacts";
	
	public static void storeInBundle(Bundle bundle) {
		Float[] genProbs = categoryProbs.values().toArray(new Float[0]);
		float[] storeProbs = new float[genProbs.length];
		for (int i = 0; i < storeProbs.length; i++){
			storeProbs[i] = genProbs[i];
		}
		bundle.put( GENERAL_PROBS, storeProbs);
		
		bundle.put( SPAWNED_ARTIFACTS, spawnedArtifacts.toArray(new Class[0]));
	}

	public static void restoreFromBundle(Bundle bundle) {
		if (bundle.contains(GENERAL_PROBS)){
			float[] probs = bundle.getFloatArray(GENERAL_PROBS);
			for (int i = 0; i < probs.length; i++){
				categoryProbs.put(Category.values()[i], probs[i]);
			}
		} else {
			reset();
		}
		
		initArtifacts();
		if (bundle.contains(SPAWNED_ARTIFACTS)){
			for ( Class<?extends Artifact> artifact : bundle.getClassArray(SPAWNED_ARTIFACTS) ){
				removeArtifact(artifact);
			}
		//pre-0.6.1 saves
		} else if (bundle.contains("artifacts")) {
			String[] names = bundle.getStringArray("artifacts");
			Category cat = Category.ARTIFACT;

			for (String artifact : names)
				for (int i = 0; i < cat.classes.length; i++)
					if (cat.classes[i].getSimpleName().equals(artifact))
						cat.probs[i] = 0;
		}
	}
}
