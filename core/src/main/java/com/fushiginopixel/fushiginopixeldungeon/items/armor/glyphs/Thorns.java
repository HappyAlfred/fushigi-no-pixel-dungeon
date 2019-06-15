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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bleeding;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Thorns extends Armor.Glyph {

	private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0x660022 );

	@Override
	public float proc(Armor armor, Object attacker, Char defender, int damage, EffectType type, int event ) {

		int level = Math.max(0, armor.level());

		/*
		if (attacker != null && attacker instanceof Char && event == Armor.EVENT_SUFFER_ATTACK) {
			if (Random.Int(level / 2 + 50) >= 40 && type.isExistAttachType(EffectType.MELEE)) {
				Char at = (Char) attacker;

				//Buff.affect( attacker, Bleeding.class).set( Math.max( level/2, damage));
				at.damage(Math.max(level, damage), this);

			}
		}
		*/

		if (event == Armor.EVENT_AFTER_DAMAGE && Random.Int(level / 2 + 50) >= 40 && damage > 0//chance
				&& attacker instanceof Char && attacker != defender
				&& type.isExistAttachType(EffectType.MELEE) //only reflect melee damage
				&& (attacker.getClass() == null || !attacker.getClass().isAssignableFrom(getClass()))) {//avoid loop
			Char at = (Char) attacker;
			at.damage( Math.max(level, (int)(damage * 0.5f)), this);

		}

		return 1;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return RED;
	}
}
