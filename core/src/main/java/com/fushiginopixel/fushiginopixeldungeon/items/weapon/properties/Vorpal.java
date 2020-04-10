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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bleeding;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.effects.Splash;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Vorpal extends Weapon.Enchantment {

	private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0xAA6666 );

	@Override
	public float procInAttack(Weapon weapon, Char attacker, Char defender, int damage , EffectType type ) {
		// lvl 0 - 33%
		// lvl 1 - 50%
		// lvl 2 - 60%
		int level = Math.max( 0, weapon.level() );

		if (Random.Int( level + 3 ) >= 2) {

			Buff.affect(defender, Bleeding.class,new EffectType(type.attachType,0)).set(damage/4);
			Splash.at( defender.sprite.center(), -PointF.PI / 2, PointF.PI / 6,
					defender.sprite.blood(), 10 );

		}

		return 1;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return RED;
	}

}
