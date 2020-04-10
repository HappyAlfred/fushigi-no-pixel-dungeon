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

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Frost;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.FlameParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SnowParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor.Glyph;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class AntiEntropy extends Glyph {

	{
		curse = true;
	}
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
	
	@Override
	public float procSufferAttack( Armor armor, Object attacker, Char defender, int damage , EffectType type ) {

		if (attacker != null && attacker instanceof Char){
			if (Random.Int(8) == 0) {
				Char at = (Char) attacker;
				if (Dungeon.level.adjacent(at.pos, defender.pos)) {
					Buff.prolong(at, Frost.class, Frost.duration(at) * Random.Float(0.5f, 1f), new EffectType(type.attachType, EffectType.ICE));
					CellEmitter.get(at.pos).start(SnowParticle.FACTORY, 0.2f, 6);
				}

				EffectType buffType = new EffectType(type.attachType, EffectType.FIRE);
				Buff.affect(defender, Burning.class, buffType).reignite(buffType);
				defender.sprite.emitter().burst(FlameParticle.FACTORY, 5);

			}
		}
		return 1;
	}

	@Override
	public Glowing glowing() {
		return BLACK;
	}
}
