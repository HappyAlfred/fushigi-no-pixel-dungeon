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

package com.fushiginopixel.fushiginopixeldungeon.actors.hero;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Challenges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.items.Amulet;
import com.fushiginopixel.fushiginopixeldungeon.items.BrokenSeal;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.TomeOfMastery;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.ClothArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.CloakOfShadows;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.PotionBandolier;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.ScrollHolder;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.VelvetPouch;
import com.fushiginopixel.fushiginopixeldungeon.items.food.Food;
import com.fushiginopixel.fushiginopixeldungeon.items.food.SpecialOnigiri;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfHealing;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfMindVision;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfMagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Dagger;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Knuckles;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MagesStaff;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.WornShortsword;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Boomerang;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.ThrowingStone;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.watabou.utils.Bundle;

public enum HeroClass {

	WARRIOR( "warrior", HeroSubClass.BERSERKER, HeroSubClass.GLADIATOR ),
	MAGE( "mage", HeroSubClass.BATTLEMAGE, HeroSubClass.WARLOCK ),
	ROGUE( "rogue", HeroSubClass.ASSASSIN, HeroSubClass.FREERUNNER ),
	HUNTRESS( "huntress", HeroSubClass.WARDEN, HeroSubClass.SNIPER ),
	FUURAI( "fuurai");

	private String title;
	private HeroSubClass[] subClasses;

	HeroClass( String title, HeroSubClass...subClasses ) {
		this.title = title;
		this.subClasses = subClasses;
	}

	HeroClass( String title) {
		this.title = title;
		this.subClasses = null;
	}

	public void initHero( Hero hero ) {

		hero.heroClass = this;

		initCommon( hero );

		switch (this) {
			case WARRIOR:
				initWarrior( hero );
				break;

			case MAGE:
				initMage( hero );
				break;

			case ROGUE:
				initRogue( hero );
				break;

			case HUNTRESS:
				initHuntress( hero );
				break;

			case FUURAI:
				initFuurai( hero );
				break;
		}
		
	}

	private static void initCommon( Hero hero ) {
		Item i = new ClothArmor().identify();
		if (!Challenges.isItemBlocked(i)) hero.belongings.armor = (ClothArmor)i;

		i = new Food();
		if (!Challenges.isItemBlocked(i)) i.collect();

		if (Dungeon.isChallenged(Challenges.NO_FOOD)){
			new SpecialOnigiri().collect();
		}

		if((hero.heroClass == HUNTRESS && Badges.isUnlocked(Badges.Badge.VICTORY_HUNTRESS))||
				(hero.heroClass == WARRIOR && Badges.isUnlocked(Badges.Badge.VICTORY_WARRIOR))||
				(hero.heroClass == ROGUE && Badges.isUnlocked(Badges.Badge.VICTORY_ROGUE))||
				(hero.heroClass == MAGE && Badges.isUnlocked(Badges.Badge.VICTORY_MAGE)) ||
				(hero.heroClass == FUURAI && Badges.isUnlocked(Badges.Badge.VICTORY_FUURAI))
				)
		{
			i = new TomeOfMastery();
			if (!Challenges.isItemBlocked(i)) i.collect();
		}

	}

	public Badges.Badge masteryBadge() {
		switch (this) {
			case WARRIOR:
				return Badges.Badge.MASTERY_WARRIOR;
			case MAGE:
				return Badges.Badge.MASTERY_MAGE;
			case ROGUE:
				return Badges.Badge.MASTERY_ROGUE;
			case HUNTRESS:
				return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}

	private static void initWarrior( Hero hero ) {
		(hero.belongings.weapon = new WornShortsword()).identify();
		ThrowingStone stones = new ThrowingStone();
		stones.identify().quantity(3).collect();
		Dungeon.quickslot.setSlot(0, stones, stones.defaultAction);

		if (hero.belongings.armor != null){
			BrokenSeal seal = new BrokenSeal();
			seal.level(1);
			hero.belongings.armor.affixSeal(seal);
		}
		
		new PotionBandolier().collect();
		Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
		new PotionOfHealing().identify();
	}

	private static void initMage( Hero hero ) {
		MagesStaff staff;
		
		staff = new MagesStaff(((Wand)new WandOfMagicMissile().identify()));

		(hero.belongings.weapon = staff).identify();
		hero.belongings.weapon.activate(hero);

		Dungeon.quickslot.setSlot(0, staff, staff.defaultAction);

		new ScrollHolder().collect();
		Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
		new ScrollOfUpgrade().identify();
	}

	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();

		CloakOfShadows cloak = new CloakOfShadows();
		(hero.belongings.misc1 = cloak).identify();
		hero.belongings.misc1.activate( hero );

		ThrowingKnife knives = new ThrowingKnife();
		knives.quantity(3).collect();

		Dungeon.quickslot.setSlot(0, cloak, cloak.defaultAction);
		Dungeon.quickslot.setSlot(1, knives, knives.defaultAction);

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		new ScrollOfMagicMapping().identify();
	}

	private static void initHuntress( Hero hero ) {

		(hero.belongings.weapon = new Knuckles()).identify();
		Boomerang boomerang = new Boomerang();
		boomerang.identify().collect();

		Dungeon.quickslot.setSlot(0, boomerang, boomerang.defaultAction);

		new VelvetPouch().collect();
		Dungeon.LimitedDrops.VELVET_POUCH.drop();
		new PotionOfMindVision().identify();
	}

	private static void initFuurai( Hero hero ) {
		hero.belongings.armor = null;

	}
	
	public String title() {
		return Messages.get(HeroClass.class, title);
	}
	
	public HeroSubClass[] subClasses() {
		return subClasses;
	}
	
	public String spritesheet() {
		
		switch (this) {
		case WARRIOR:
			return Assets.WARRIOR;
		case MAGE:
			return Assets.MAGE;
		case ROGUE:
			return Assets.ROGUE;
		case HUNTRESS:
			return Assets.HUNTRESS;
		case FUURAI:
			return Assets.FUURAI;
		}
		
		return null;
	}
	
	public String[] perks() {
		
		switch (this) {
		case WARRIOR:
			return new String[]{
					Messages.get(HeroClass.class, "warrior_perk1"),
					Messages.get(HeroClass.class, "warrior_perk2"),
					Messages.get(HeroClass.class, "warrior_perk3"),
					Messages.get(HeroClass.class, "warrior_perk4"),
					Messages.get(HeroClass.class, "warrior_perk5"),
			};
		case MAGE:
			return new String[]{
					Messages.get(HeroClass.class, "mage_perk1"),
					Messages.get(HeroClass.class, "mage_perk2"),
					Messages.get(HeroClass.class, "mage_perk3"),
					Messages.get(HeroClass.class, "mage_perk4"),
					Messages.get(HeroClass.class, "mage_perk5"),
			};
		case ROGUE:
			return new String[]{
					Messages.get(HeroClass.class, "rogue_perk1"),
					Messages.get(HeroClass.class, "rogue_perk2"),
					Messages.get(HeroClass.class, "rogue_perk3"),
					Messages.get(HeroClass.class, "rogue_perk4"),
					Messages.get(HeroClass.class, "rogue_perk5"),
			};
		case HUNTRESS:
			return new String[]{
					Messages.get(HeroClass.class, "huntress_perk1"),
					Messages.get(HeroClass.class, "huntress_perk2"),
					Messages.get(HeroClass.class, "huntress_perk3"),
					Messages.get(HeroClass.class, "huntress_perk4"),
					Messages.get(HeroClass.class, "huntress_perk5"),
			};

		case FUURAI:
		return new String[]{
				Messages.get(HeroClass.class, "fuurai_perk1"),
				Messages.get(HeroClass.class, "fuurai_perk2"),
				Messages.get(HeroClass.class, "fuurai_perk3"),
				Messages.get(HeroClass.class, "fuurai_perk4"),
				Messages.get(HeroClass.class, "fuurai_perk5"),
		};
	}
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : ROGUE;
	}
}
