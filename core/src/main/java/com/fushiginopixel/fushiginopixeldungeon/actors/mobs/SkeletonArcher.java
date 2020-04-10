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

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.effects.Pushing;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.WandOfBlastWave;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Crossbow;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MeleeWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.ThrowingSpear;
import com.fushiginopixel.fushiginopixeldungeon.levels.features.Chasm;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.SkeletonArcherSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.SkeletonSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SkeletonArcher extends Skeleton {
	
	{
		spriteClass = SkeletonArcherSprite.class;

		//defenseSkill = 11;
		
		EXP = 9;

		loot = null;    //see createloot.
		lootChance = 0.25f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 11, 25 );
	}


	@Override
	public boolean canAttack( Char enemy ) {
		Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
		return attack.collisionPos == enemy.pos;
	}
	
	@Override
	protected Item createLoot() {
		Item loot;
		if (Random.Int(3) > 0)
		return new ThrowingSpear().random();
		else{
			loot = new Crossbow().random();
			loot.level(0);
			return loot;
		}
	}

	@Override
	public int attackProc(KindOfWeapon weapon, Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( weapon, enemy, damage,type );
		if (Random.Int( 5 ) >= 3){
			/*
			int opposite = enemy.pos + (enemy.pos - this.pos);
			Ballistica trajectory = new Ballistica(enemy.pos, opposite, Ballistica.MAGIC_BOLT);
			WandOfBlastWave.throwChar(enemy,trajectory,2);*/
			WandOfBlastWave.knockBack(this,enemy,2);
		}
		return damage;
	}
	
	@Override
	public int attackSkill( Char target ) {
		int attack = super.attackSkill(target);
		return distance(target) > 1 ?  attack : attack/2;
	}

}
