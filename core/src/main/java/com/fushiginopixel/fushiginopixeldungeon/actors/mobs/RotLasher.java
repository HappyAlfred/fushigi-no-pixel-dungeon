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
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Cripple;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.RotLasherSprite;
import com.watabou.utils.Random;

public class RotLasher extends Mob {

	{
		spriteClass = RotLasherSprite.class;

		HP = HT = 40;
		defenseSkill = 0;

		EXP = 1;

		loot = Generator.Category.SEED;
		lootChance = 1f;

		state = WANDERING = new Waiting();

		properties.add(Property.IMMOVABLE);
		properties.add(Property.MINIBOSS);
	}

	@Override
	protected boolean act() {
		if (enemy == null || !Dungeon.level.adjacent(pos, enemy.pos)) {
			HP = Math.min(HT, HP + 3);
		}
		return super.act();
	}

	@Override
	public int damage(int dmg, Object src, EffectType type) {
		if (src instanceof Burning) {
			destroy();
			sprite.die();
			return 0;
		} else {
			return super.damage(dmg, src, type);
		}
	}

	@Override
	public int attackProc(KindOfWeapon weapon, Char enemy, int damage, EffectType type) {
		//damage = super.attackProc( weapon, enemy, damage,type );
		Buff.affect( enemy, Cripple.class, 2f, new EffectType(type.attachType,0) );
		return super.attackProc( weapon, enemy, damage,type);
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	protected boolean getCloser(int target) {
		return true;
	}

	@Override
	protected boolean getFurther(int target) {
		return true;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(8, 15);
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 15;
	}
	*/

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 8);
	}
	
	{
		immunities.add( new EffectType(EffectType.GAS,0) );
	}

	private class Waiting extends Mob.Wandering{}
}
