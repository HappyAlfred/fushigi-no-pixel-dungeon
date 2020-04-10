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

package com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Dragon;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.InfernoDragon;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Bundle;

import java.util.HashSet;

public class Brimstone extends Armor.Glyph {

	private static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing( 0xFF4400 );

	/*public static final HashSet<Class> RESISTS = new HashSet<>();
	static {
		RESISTS.add( Dragon.class );
		RESISTS.add( InfernoDragon.class );
	}*/

	public static final HashSet<EffectType> RESISTSTYPE = new HashSet<>();
	static {
		RESISTSTYPE.add( new EffectType(0,EffectType.FIRE) );
	}

	@Override
	public float procBeforeDamage(Armor armor, Object attacker, Char defender, int damage, EffectType type ) {
		if(EffectType.isExistType(type, RESISTSTYPE)){
			return 0.5f;
		}
		return 1;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return ORANGE;
	}

	public static class BrimstoneShield extends Buff {

		private int shieldAdded;
		private int lastShield = -1;

		@Override
		public boolean act() {
			Hero hero = (Hero)target;

			//make sure any shielding lost through combat is accounted for
			if (lastShield != -1 && lastShield > hero.SHLD)
				shieldAdded = Math.max(0, shieldAdded - (lastShield - hero.SHLD));

			lastShield = hero.SHLD;

			if (hero.belongings.armor == null || !hero.belongings.armor.hasGlyph(Brimstone.class)) {
				hero.SHLD -= shieldAdded;
				detach();
				return true;
			}

			int level = hero.belongings.armor.level();

			if (hero.buff(Burning.class) != null){
				//max shielding equal to the armors level (this does mean no shield at lvl 0)
				if (hero.SHLD < level) {
					shieldAdded++;
					hero.SHLD++;
					lastShield++;

					//generates 0.2 + 0.1*lvl shield per turn
					spend( 10f / (2f + level));
				} else {

					//if shield is maxed, don't wait longer than 1 turn to try again
					spend( Math.min( TICK, 10f / (2f + level)));
				}

			} else if (hero.buff(Burning.class) == null){
				if (shieldAdded > 0 && hero.SHLD > 0){
					shieldAdded--;
					hero.SHLD--;
					lastShield--;

					//shield decays at a rate of 1 per turn.
					spend(TICK);
				} else {
					detach();
				}
			}

			return true;
		}

		public void startDecay(){
			//sets the buff to start acting next turn. Invoked by Burning when it expires.
			spend(-cooldown()+2);
		}

		private static String ADDED = "added";
		private static String LAST  = "last";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put( ADDED, shieldAdded );
			bundle.put( LAST, lastShield );
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			shieldAdded = bundle.getInt( ADDED );
			lastShield = bundle.getInt( LAST );
		}
	}

}
