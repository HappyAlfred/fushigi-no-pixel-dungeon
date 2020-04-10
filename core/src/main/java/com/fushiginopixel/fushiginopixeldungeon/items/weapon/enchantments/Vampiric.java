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
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Vampiric extends Weapon.Enchantment {

	private static ItemSprite.Glowing RED = new ItemSprite.Glowing( 0x660022 );
	
	@Override
	public float procInAttack( Weapon weapon, Char attacker, Char defender, int damage , EffectType type ) {
		
		int level = Math.max( 0, weapon.level() );
		
		// lvl 0 - 16%
		// lvl 1 - 17.65%
		// lvl 2 - 19.23%
		int maxValue = Math.round(damage * (8f / 50));
		int effValue = Math.min( Random.IntRange( 0, maxValue ), attacker.HT - attacker.HP );
		
		if (effValue > 0 && attacker.isAlive()) {
		
			attacker.HP += effValue;
			attacker.sprite.emitter().start( Speck.factory( Speck.HEALING ), 0.4f, 1 );
			attacker.sprite.showStatus( CharSprite.POSITIVE, Integer.toString( effValue ) );
			
		}

		return 1;
	}
	
	@Override
	public Glowing glowing() {
		return RED;
	}
}
