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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bleeding;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.WhiteRatSprite;
import com.watabou.utils.Random;

public class WhiteRat extends Rat {

	{
		spriteClass = WhiteRatSprite.class;
		
		HP = HT = 12;

		loot = Generator.Category.SEED;
		lootChance = 0.2f;
		EXP = 2;
		//defenseSkill = 3;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 5 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 10;
	}
	*/

	@Override
	public int attackProc(KindOfWeapon weapon, Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( weapon, enemy, damage,type );
		if (Random.Int( 2 ) == 0) {
			Buff.affect( enemy, Bleeding.class,new EffectType(type.attachType,0) ).set( damage/2 );
		}
		
		return damage;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}
}
