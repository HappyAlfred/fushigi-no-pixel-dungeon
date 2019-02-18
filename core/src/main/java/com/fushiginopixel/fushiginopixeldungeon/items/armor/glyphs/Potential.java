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
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.EnergyParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor.Glyph;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite.Glowing;

public class Potential extends Glyph {
	
	private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF, 0.6f );
	
	@Override
	public float proc( Armor armor, Char attacker, Char defender, int damage , EffectType type ) {

		int level = Math.max( 0, armor.level() );
		
		if (defender instanceof Hero) {
			int wands = ((Hero) defender).belongings.charge(0.1f + level*0.05f);
			if (wands > 0) {
				defender.sprite.centerEmitter().burst(EnergyParticle.FACTORY, wands * (level + 2));
			}
		}
		
		return 1;
	}

	@Override
	public Glowing glowing() {
		return WHITE;
	}
}
