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

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bleeding;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.effects.Pushing;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfBlastWave;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.sprites.BatSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.EaterBatSprite;
import com.watabou.utils.Random;

public class EaterBat extends Bat {

	{
		spriteClass = EaterBatSprite.class;
		
		HP = HT = 80;
		//defenseSkill = 25;
		baseSpeed = 2f;
		
		EXP = 13;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 20, 30 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 40;
	}
	*/
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 6);
	}

	@Override
	public int attackProc(KindOfWeapon weapon, Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( weapon, enemy, damage, type );
		if (Random.Int( 3 ) == 0) {
			Buff.affect( enemy, Blindness.class , 5f, new EffectType(type.attachType,0));
			int opposite = pos + (pos - enemy.pos);
			Ballistica trajectory = new Ballistica(pos, opposite, Ballistica.MAGIC_BOLT);
			int dist = Math.min(Random.IntRange(1,2) , trajectory.dist);
			int newPos = trajectory.path.get(dist);
			if ((Dungeon.level.passable[newPos] || Dungeon.level.avoid[newPos])
					&& Actor.findChar( newPos ) == null) {
				Actor.addDelayed(new Pushing(this, pos, newPos), -1);
				pos = newPos;
			}
		}

		return damage;
	}
}
