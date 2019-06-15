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

package com.fushiginopixel.fushiginopixeldungeon.journal;

import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.CaneArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.ClothArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.GoldArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.HuntressArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.LeafArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.LeatherArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.LightningConchShell;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.MageArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.MailArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.PlateArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.RogueArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.SamuraiArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.ScaleArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.SilkSuit;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.WarriorArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.WoodenArmor;
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
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Boomerang;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

public enum Catalog {
	
	WEAPONS,
	ARMOR,
	WANDS,
	RINGS,
	ARTIFACTS,
	POTIONS,
	SCROLLS,
	POTS;
	
	private LinkedHashMap<Class<? extends Item>, Boolean> seen = new LinkedHashMap<>();
	
	public Collection<Class<? extends Item>> items(){
		return seen.keySet();
	}
	
	public boolean allSeen(){
		for (Class<?extends Item> item : items()){
			if (!seen.get(item)){
				return false;
			}
		}
		return true;
	}
	
	static {
		WEAPONS.seen.put( WornShortsword.class,             false);
		WEAPONS.seen.put( Sword.class,                      false);
		WEAPONS.seen.put( Longsword.class,                  false);
		WEAPONS.seen.put( Greatsword.class,                 false);
		WEAPONS.seen.put( Knuckles.class,                   false);
		WEAPONS.seen.put( Dagger.class,                     false);
		WEAPONS.seen.put( MagesStaff.class,                 false);
		WEAPONS.seen.put( Boomerang.class,                  false);
		WEAPONS.seen.put( HandAxe.class,                    false);
		WEAPONS.seen.put( Spear.class,                      false);
		WEAPONS.seen.put( Quarterstaff.class,               false);
		WEAPONS.seen.put( Goldsword.class,                  false);
		WEAPONS.seen.put( Mace.class,                       false);
		WEAPONS.seen.put( Scimitar.class,                   false);
		WEAPONS.seen.put( RoundShield.class,                false);
		WEAPONS.seen.put( Mattock.class,                	   false);
		WEAPONS.seen.put( Sai.class,                        false);
		WEAPONS.seen.put( Whip.class,                       false);
		WEAPONS.seen.put( Katana.class,                     false);
		WEAPONS.seen.put( AssassinsBlade.class,             false);
		WEAPONS.seen.put( Crossbow.class,                   false);
		WEAPONS.seen.put( Flail.class,                      false);
		WEAPONS.seen.put( BattleAxe.class,                  false);
		WEAPONS.seen.put( WarHammer.class,                  false);
		WEAPONS.seen.put( Glaive.class,                     false);
		WEAPONS.seen.put( Greataxe.class,                   false);
		WEAPONS.seen.put( Greatshield.class,                false);
		WEAPONS.seen.put( Gauntlet.class,                   false);
		WEAPONS.seen.put( FuhmaKatana.class,                false);
		WEAPONS.seen.put( RunicBlade.class,                 false);
		WEAPONS.seen.put( InfernoFuhmaKatana.class,         false);
		WEAPONS.seen.put( Gradius.class,         				false);
	
		ARMOR.seen.put( ClothArmor.class,                   false);
		ARMOR.seen.put( SilkSuit	.class,                   false);
		ARMOR.seen.put( LeatherArmor.class,                 false);
		ARMOR.seen.put( WoodenArmor.class,                  false);
		ARMOR.seen.put( GoldArmor.class,                    false);
		ARMOR.seen.put( MailArmor.class,                    false);
		ARMOR.seen.put( LeafArmor.class,                    false);
		ARMOR.seen.put( CaneArmor.class,                    false);
		ARMOR.seen.put( ScaleArmor.class,                   false);
		ARMOR.seen.put( SamuraiArmor.class,                 false);
		ARMOR.seen.put( LightningConchShell.class,          false);
		ARMOR.seen.put( PlateArmor.class,                   false);
		//ARMOR.seen.put( WarriorArmor.class,                 false);
		//ARMOR.seen.put( MageArmor.class,                    false);
		//ARMOR.seen.put( RogueArmor.class,                   false);
		//ARMOR.seen.put( HuntressArmor.class,                false);
	
		WANDS.seen.put( WandOfMagicMissile.class,           false);
		WANDS.seen.put( WandOfLightning.class,              false);
		WANDS.seen.put( WandOfDisintegration.class,         false);
		WANDS.seen.put( WandOfFireblast.class,              false);
		WANDS.seen.put( WandOfCorrosion.class,              false);
		WANDS.seen.put( WandOfBlastWave.class,              false);
		//WANDS.seen.put( WandOfLivingEarth.class,          false);
		//WANDS.seen.put( WandOfFrost.class,                  false);
		WANDS.seen.put( WandOfPrismaticLight.class,         false);
		//WANDS.seen.put( WandOfWarding.class,              false);
		//WANDS.seen.put( WandOfTransfusion.class,            false);
		WANDS.seen.put( WandOfHoly.class,            false);
		WANDS.seen.put( WandOfCorruption.class,             false);
		//WANDS.seen.put( WandOfRegrowth.class,               false);
		WANDS.seen.put( WandOfElements.class,            false);
		WANDS.seen.put( WandOfNightmare.class,             false);
		WANDS.seen.put( WandOfKaleidoscope.class,            false);
		WANDS.seen.put( WandOfMagician.class,            false);
	
		RINGS.seen.put( RingOfAccuracy.class,               false);
		//RINGS.seen.put( RingOfEnergy.class,                 false);
		RINGS.seen.put( RingOfElements.class,               false);
		RINGS.seen.put( RingOfEvasion.class,                false);
		RINGS.seen.put( RingOfAlert.class,                  false);
		RINGS.seen.put( RingOfFuror.class,                  false);
		RINGS.seen.put( RingOfHaste.class,                  false);
		RINGS.seen.put( RingOfMight.class,                  false);
		RINGS.seen.put( RingOfSharpshooting.class,          false);
		RINGS.seen.put( RingOfTenacity.class,               false);
		RINGS.seen.put( RingOfWealth.class,                 false);
		RINGS.seen.put( RingOfKnowledge.class,              false);
	
		//ARTIFACTS.seen.put( AlchemistsToolkit.class,      false);
		ARTIFACTS.seen.put( CapeOfThorns.class,             false);
		ARTIFACTS.seen.put( ChaliceOfBlood.class,           false);
		ARTIFACTS.seen.put( CloakOfShadows.class,           false);
		ARTIFACTS.seen.put( DriedRose.class,                false);
		ARTIFACTS.seen.put( EtherealChains.class,           false);
		ARTIFACTS.seen.put( HornOfPlenty.class,             false);
		ARTIFACTS.seen.put( LloydsBeacon.class,             false);
		ARTIFACTS.seen.put( MasterThievesArmband.class,     false);
		ARTIFACTS.seen.put( SandalsOfNature.class,          false);
		ARTIFACTS.seen.put( TalismanOfForesight.class,      false);
		ARTIFACTS.seen.put( TimekeepersHourglass.class,     false);
		//ARTIFACTS.seen.put( UnstableSpellbook.class,        false);
	
		POTIONS.seen.put( PotionOfHealing.class,            false);
		POTIONS.seen.put( PotionOfStrength.class,           false);
		POTIONS.seen.put( PotionOfLiquidFlame.class,        false);
		POTIONS.seen.put( PotionOfFrost.class,              false);
		POTIONS.seen.put( PotionOfToxicGas.class,           false);
		POTIONS.seen.put( PotionOfParalyticGas.class,       false);
		POTIONS.seen.put( PotionOfPurity.class,             false);
		POTIONS.seen.put( PotionOfLevitation.class,         false);
		POTIONS.seen.put( PotionOfMindVision.class,         false);
		POTIONS.seen.put( PotionOfInvisibility.class,       false);
		POTIONS.seen.put( PotionOfExperience.class,         false);
		POTIONS.seen.put( PotionOfMight.class,              false);
		POTIONS.seen.put( PotionOfPanacea.class,         false);
		POTIONS.seen.put( PotionOfTearGas.class,         false);
		POTIONS.seen.put( PotionOfVenom.class,       false);
		POTIONS.seen.put( PotionOfExplode.class,         false);
		POTIONS.seen.put( PotionOfBeverage.class,              false);
	
		SCROLLS.seen.put( ScrollOfIdentify.class,           false);
		SCROLLS.seen.put( ScrollOfUpgrade.class,            false);
		SCROLLS.seen.put( ScrollOfRemoveCurse.class,        false);
		SCROLLS.seen.put( ScrollOfMagicMapping.class,       false);
		SCROLLS.seen.put( ScrollOfTeleportation.class,      false);
		SCROLLS.seen.put( ScrollOfRecharging.class,         false);
		SCROLLS.seen.put( ScrollOfMirrorImage.class,        false);
		SCROLLS.seen.put( ScrollOfTerror.class,             false);
		SCROLLS.seen.put( ScrollOfLullaby.class,            false);
		SCROLLS.seen.put( ScrollOfRage.class,               false);
		SCROLLS.seen.put( ScrollOfPsionicBlast.class,       false);
		SCROLLS.seen.put( ScrollOfMagicalInfusion.class,    false);
		SCROLLS.seen.put( ScrollOfExpand.class,    			false);
		SCROLLS.seen.put( ScrollOfOnigiri.class,    			false);
		SCROLLS.seen.put( ScrollOfExsuction.class,    		false);
		SCROLLS.seen.put( ScrollOfSelfDestruct.class,    		false);
		SCROLLS.seen.put( ScrollOfAffection.class,    		false);
		SCROLLS.seen.put( ScrollOfEscape.class,    			false);


		POTS.seen.put( PotOfIdentify.class,    				false);
		POTS.seen.put( PotOfTransmutation.class,    			false);
		POTS.seen.put( PotOfRestructure.class, 			   	false);
		POTS.seen.put( PotOfTradeGold.class,    				false);
		POTS.seen.put( PotOfFusion.class,    					false);
		POTS.seen.put( PotOfFreeze.class, 	   				false);
        POTS.seen.put( PotOfStorge.class, 	   				false);
		POTS.seen.put( PotOfAlchemy.class, 	   				false);
		POTS.seen.put( PotOfDispel.class, 	   				false);
		POTS.seen.put( PotOfCannon.class, 	   				false);
	}
	
	public static LinkedHashMap<Catalog, Badges.Badge> catalogBadges = new LinkedHashMap<>();
	static {
		catalogBadges.put(WEAPONS, Badges.Badge.ALL_WEAPONS_IDENTIFIED);
		catalogBadges.put(ARMOR, Badges.Badge.ALL_ARMOR_IDENTIFIED);
		catalogBadges.put(WANDS, Badges.Badge.ALL_WANDS_IDENTIFIED);
		catalogBadges.put(RINGS, Badges.Badge.ALL_RINGS_IDENTIFIED);
		catalogBadges.put(ARTIFACTS, Badges.Badge.ALL_ARTIFACTS_IDENTIFIED);
		catalogBadges.put(POTIONS, Badges.Badge.ALL_POTIONS_IDENTIFIED);
		catalogBadges.put(SCROLLS, Badges.Badge.ALL_SCROLLS_IDENTIFIED);
	}
	
	public static boolean isSeen(Class<? extends Item> itemClass){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(itemClass)) {
				return cat.seen.get(itemClass);
			}
		}
		return false;
	}
	
	public static void setSeen(Class<? extends Item> itemClass){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(itemClass) && !cat.seen.get(itemClass)) {
				cat.seen.put(itemClass, true);
				Journal.saveNeeded = true;
			}
		}
		Badges.validateItemsIdentified();
	}

	public static void setNoSeen(Class<? extends Item> itemClass){
		for (Catalog cat : values()) {
			if (cat.seen.containsKey(itemClass) && cat.seen.get(itemClass)) {
				cat.seen.put(itemClass, false);
				Journal.saveNeeded = true;
			}
		}
	}
	
	private static final String CATALOGS = "catalogs";
	
	public static void store( Bundle bundle ){
		
		Badges.loadGlobal();
		
		ArrayList<String> seen = new ArrayList<>();
		
		//if we have identified all items of a set, we use the badge to keep track instead.
		if (!Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED)) {
			for (Catalog cat : values()) {
				if (!Badges.isUnlocked(catalogBadges.get(cat))) {
					for (Class<? extends Item> item : cat.items()) {
						if (cat.seen.get(item)) seen.add(item.getSimpleName());
					}
				}
			}
		}
		
		bundle.put( CATALOGS, seen.toArray(new String[0]) );
		
	}
	
	public static void restore( Bundle bundle ){
		
		Badges.loadGlobal();
		
		//logic for if we have all badges
		if (Badges.isUnlocked(Badges.Badge.ALL_ITEMS_IDENTIFIED)){
			for ( Catalog cat : values()){
				for (Class<? extends Item> item : cat.items()){
					cat.seen.put(item, true);
				}
			}
			return;
		}
		
		//catalog-specific badge logic
		for (Catalog cat : values()){
			if (Badges.isUnlocked(catalogBadges.get(cat))){
				for (Class<? extends Item> item : cat.items()){
					cat.seen.put(item, true);
				}
			}
		}
		
		//general save/load
		if (bundle.contains(CATALOGS)) {
			List<String> seen = Arrays.asList(bundle.getStringArray(CATALOGS));
			
			//pre-0.6.3 saves
			//TODO should adjust this to tie into the bundling system's class array
			if (seen.contains("WandOfVenom")){
				WANDS.seen.put(WandOfCorrosion.class, true);
			}
			
			for (Catalog cat : values()) {
				for (Class<? extends Item> item : cat.items()) {
					if (seen.contains(item.getSimpleName())) {
						cat.seen.put(item, true);
					}
				}
			}
		}
	}
	
}
