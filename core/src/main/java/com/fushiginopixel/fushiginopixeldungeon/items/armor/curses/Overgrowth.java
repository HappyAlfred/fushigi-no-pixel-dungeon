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

package com.fushiginopixel.fushiginopixeldungeon.items.armor.curses;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.LeafParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.plants.BlandfruitBush;
import com.fushiginopixel.fushiginopixeldungeon.plants.Plant;
import com.fushiginopixel.fushiginopixeldungeon.plants.Starflower;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Overgrowth extends Armor.Glyph {

	{
		curse = true;
	}
	
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
	
	@Override
	public float proc(Armor armor, Object attacker, Char defender, int damage, EffectType type, int event ) {

		if (event == Armor.EVENT_SUFFER_ATTACK) {
			if (Random.Int(20) == 0) {

				Plant.Seed s;
				do {
					s = (Plant.Seed) Generator.random(Generator.Category.SEED);
				} while (s instanceof BlandfruitBush.Seed || s instanceof Starflower.Seed);

				Plant p = s.couch(defender.pos, null);

				p.activate();
				CellEmitter.get(defender.pos).burst(LeafParticle.LEVEL_SPECIFIC, 10);

			}
		}
		
		return 1;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}
}
