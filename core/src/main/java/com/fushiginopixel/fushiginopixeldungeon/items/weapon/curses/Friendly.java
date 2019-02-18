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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.curses;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Charm;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Friendly extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
	
	@Override
	public float proc(Weapon weapon, Char attacker, Char defender, int damage , EffectType type ) {
		
		if (Random.Int(10) == 0){
			
			Buff.affect( attacker, Charm.class, Random.IntRange(9, 15), new EffectType(0,EffectType.SPIRIT) ).object = defender.id();
			attacker.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );
			
			Buff.affect( defender, Charm.class, Random.IntRange(3, 5), new EffectType(0,EffectType.SPIRIT) ).object = attacker.id();
			defender.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );
			
		}
		
		return 1;
	}
	
	@Override
	public boolean curse() {
		return true;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}

}
