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

package com.fushiginopixel.fushiginopixeldungeon.actors.mobs;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.sprites.SeniorSprite;
import com.watabou.utils.Random;

public class Senior extends Monk {

	{
		spriteClass = SeniorSprite.class;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 35, 50 );
	}
	
	@Override
	public int attackProc( Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( enemy, damage,type );
		if (Random.Int( 10 ) == 0) {
			Buff.prolong( enemy, Paralysis.class, 1.1f,new EffectType(type.attachType,0) );
		}
		return super.attackProc( enemy, damage,type );
	}

	{
		immunities.add( new EffectType(0,EffectType.SPIRIT) );
	}
	
}
