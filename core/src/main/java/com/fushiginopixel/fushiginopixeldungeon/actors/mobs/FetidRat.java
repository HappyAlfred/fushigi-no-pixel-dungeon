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
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.StenchGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Ooze;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Ghost;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.FetidRatSprite;
import com.watabou.utils.Random;

public class FetidRat extends Rat {

	{
		spriteClass = FetidRatSprite.class;

		HP = HT = 40;
		//defenseSkill = 7;

		EXP = 8;

		state = WANDERING;

		properties.add(Property.MINIBOSS);
		properties.add(Property.DEMONIC);
	}
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 10 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 15;
	}
	*/

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}

	@Override
	public int attackProc( Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( enemy, damage, type );
		if (Random.Int(3) == 0) {
			Buff.affect(enemy, Ooze.class, new EffectType(type.attachType,EffectType.CORRROSION));
		}

		return damage;
	}

	@Override
	public int defenseProc( Char enemy, int damage, EffectType type  ) {

		GameScene.add(Blob.seed(pos, 20, StenchGas.class));

		return super.defenseProc(enemy, damage, type);
	}

	@Override
	public void die( Object cause, EffectType type ) {
		super.die( cause, type );

		Ghost.Quest.process();
	}
	
	{
		immunities.add( new EffectType(StenchGas.class) );
	}
}