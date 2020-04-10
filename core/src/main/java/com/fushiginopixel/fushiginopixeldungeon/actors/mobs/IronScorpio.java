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

import com.fushiginopixel.fushiginopixeldungeon.sprites.IronScorpioSprite;
import com.watabou.utils.Random;

public class IronScorpio extends Scorpio {

	{
		spriteClass = IronScorpioSprite.class;

		HP = HT = 90;
		//defenseSkill = 21;
		//viewDistance = Light.DISTANCE;

		EXP = 17;

		//properties.add(Property.DEMONIC);

		poisonStr = 2;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 27, 42 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 47;
	}
	*/

	@Override
	public int drRoll() {
		return Random.NormalIntRange(1, 15);
	}

	/*
	@Override
	public int attackProc( Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( enemy, damage, type );
		if (Random.Int( 2 ) == 0) {
			Buff.prolong( enemy, Cripple.class, Cripple.DURATION,new EffectType(type.attachType,EffectType.POISON) );
		}

		return damage;
	}.

	@Override
	protected boolean canAttack( Char enemy ) {
		Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
		return !Dungeon.level.adjacent( pos, enemy.pos ) && attack.collisionPos == enemy.pos;
	}

	@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING) {
			return enemySeen && getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}
	*/
}
