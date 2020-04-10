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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Chill;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.FlavourBuff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Frost;
import com.fushiginopixel.fushiginopixeldungeon.effects.Splash;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SnowParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Chilling extends Weapon.Enchantment {

	private static ItemSprite.Glowing TEAL = new ItemSprite.Glowing( 0x00FFFF );
	
	@Override
	public float procInAttack( Weapon weapon, Char attacker, Char defender, int damage , EffectType type ) {
		// lvl 0 - 20%
		// lvl 1 - 33%
		// lvl 2 - 43%
		/*
		int level = Math.max( 0, weapon.level() );
		
		if (Random.Int( level / 2 + 100 ) >= 80) {
			
			Buff.prolong( defender, Chill.class, Random.Float( 3f, 7f ) * (float)((Random.Int(level) + 20)/20), new EffectType(type.attachType,EffectType.ICE));
			Splash.at( defender.sprite.center(), 0xFFB2D6FF, 5);

		}
		*/

		defender.damage( 7, this, new EffectType(type.attachType,EffectType.ICE));
		Splash.at( defender.sprite.center(), 0xFFB2D6FF, 5);
		final EffectType currentType = type;

		Buff.prolong( defender, Chill.class, 3, new EffectType(currentType.attachType,EffectType.ICE));
		Chill chill = defender.buff(Chill.class);
		if (chill != null && chill.cooldown() >= 9){
			new FlavourBuff() {
				{
					actPriority = VFX_PRIO;
				}

				public boolean act() {
					Buff.affect(target, Frost.class, Frost.duration(target), new EffectType(currentType.attachType, EffectType.ICE));
					return super.act();
				}
			}.attachTo(defender);
		}

		return 1;
	}
	
	@Override
	public Glowing glowing() {
		return TEAL;
	}

}
