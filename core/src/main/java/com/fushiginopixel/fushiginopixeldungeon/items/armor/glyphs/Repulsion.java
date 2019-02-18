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
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfBlastWave;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Repulsion extends Armor.Glyph {

	private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF );
	
	@Override
	public float proc( Armor armor, Char attacker, Char defender, int damage , EffectType type ) {

		int level = Math.max( 0, armor.level() );

		if (Random.Int( level + 10 ) >= 8 && type.isExistAttachType(EffectType.MELEE)){
			/*
			int oppositeHero = attacker.pos + (attacker.pos - defender.pos);
			Ballistica trajectory = new Ballistica(attacker.pos, oppositeHero, Ballistica.MAGIC_BOLT);
			WandOfBlastWave.throwChar(attacker, trajectory, 2);
			*/

			WandOfBlastWave.knockBack(defender,attacker,2);
		}
		
		return 1;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return WHITE;
	}
}
