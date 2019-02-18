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

package com.fushiginopixel.fushiginopixeldungeon.items.armor;

import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.BrokenSeal;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Balance;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

abstract public class ClassArmor extends Armor {

	private static final String AC_SPECIAL = "SPECIAL";
	
	{
		levelKnown = true;
		cursedKnown = true;
		defaultAction = AC_SPECIAL;

		bones = false;
		tier = 6;
	}

	private int armorTier;
	
	public static ClassArmor upgrade ( Hero owner, Armor armor ) {
		
		ClassArmor classArmor = null;
		
		switch (owner.heroClass) {
		case WARRIOR:
			classArmor = new WarriorArmor();
			/*BrokenSeal seal = armor.checkSeal();
			if (seal != null) {
				classArmor.affixSeal(seal);
			}*/
			break;
		case ROGUE:
			classArmor = new RogueArmor();
			break;
		case MAGE:
			classArmor = new MageArmor();
			break;
		case HUNTRESS:
			classArmor = new HuntressArmor();
			break;
		}

		classArmor.LIMIT = armor.LIMIT;
		classArmor.fusion(armor);
		classArmor.armorTier = armor.tier;
		//classArmor.augment = armor.augment;
		classArmor.identify();
		
		return classArmor;
	}

	private static final String ARMOR_TIER	= "armortier";
	private static final String GLYPH_LIMIT	= "glyph_limit";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( ARMOR_TIER, armorTier );
		bundle.put( GLYPH_LIMIT, LIMIT );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		armorTier = bundle.getInt( ARMOR_TIER );
		LIMIT = bundle.getInt( GLYPH_LIMIT );
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (hero.HP >= 3 && isEquipped( hero )) {
			actions.add( AC_SPECIAL );
		}
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {

		super.execute( hero, action );

		if (action.equals(AC_SPECIAL)) {
			
			if (hero.HP < 3) {
				GLog.w( Messages.get(this, "low_hp") );
			} else if (!isEquipped( hero )) {
				GLog.w( Messages.get(this, "not_equipped") );
			} else {
				curUser = hero;
				Invisibility.dispel();
				doSpecial();
			}
			
		}
	}

	abstract public void doSpecial();

	/*@Override
	public int STRReq(int lvl) {
		lvl = Math.max(0, lvl);

		//strength req decreases at +1,+3,+6,+10,etc.
		return (8 + Math.round(armorTier * 2)) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}*/

	@Override
	public int DRMax(int lvl){
		int max = armorTier * (2 + lvl);// + augment.defenseFactor(lvl);
		if (hasGlyph(Balance.class)) max = (max + lvl)/2;
		if (lvl > max){
			return ((lvl - max)+1)/2;
		} else {
			return max;
		}
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int price() {
		return 0;
	}

}
