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
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.sprites.BatSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.VampireBatSprite;
import com.watabou.utils.Random;

public class VampireBat extends Bat {

	{
		spriteClass = VampireBatSprite.class;
		
		HP = HT = 220;
		defenseSkill = 38;
		baseSpeed = 2f;
		
		EXP = 22;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 25, 55 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 80;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 8);
	}

	@Override
	public int attackProc( Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( enemy, damage,type );
		int reg = Math.min( damage, HT - HP );

		if (reg > 0 && isAlive()) {
			HP += reg;
			sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
		}

		return damage;
	}
	
}
