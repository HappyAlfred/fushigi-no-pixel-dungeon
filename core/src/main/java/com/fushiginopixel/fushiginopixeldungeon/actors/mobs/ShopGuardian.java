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
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Amok;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.MagicalSleep;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon.Enchantment;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Grim;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Dagger;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MeleeWeapon;
import com.fushiginopixel.fushiginopixeldungeon.journal.Notes;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.StatueSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class ShopGuardian extends Mob {

	{
		spriteClass = ShopGuardianSprite.class;

		EXP = 0;
		state = WANDERING;

	}

	public ShopGuardian() {
		super();
		HP = HT = 15 + Dungeon.depth * 5;
		//defenseSkill = 4 + Dungeon.depth;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( HT / 3, HT);
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, Dungeon.depth);
	}

	@Override
	public boolean reset() {
		state = WANDERING;
		return true;
	}

	public static class ShopGuardianSprite extends StatueSprite {

		boolean type = true;
		public ShopGuardianSprite(){
			super();
			tint(1, 1, 0, 0.2f);
		}

		@Override
		public void resetColor() {
			super.resetColor();
			tint(1, 1, 0, 0.2f);
		}
	}

	@Override
	protected Char chooseEnemy() {
		Terror terror = buff( Terror.class );
		if (terror != null) {
			Char source = (Char)Actor.findById( terror.object );
			if (source != null) {
				return source;
			}
		}

		//find a new enemy if..
		boolean newEnemy = false;
		//we have no enemy, or the current one is dead
		if ( enemy == null || !enemy.isAlive() || state == WANDERING)
			newEnemy = true;
			//We are an ally, and current enemy is another ally.
		else if (alignment == Alignment.ALLY && enemy.alignment == Alignment.ALLY)
			newEnemy = true;
			//We are amoked and current enemy is the hero
		else if (buff( Amok.class ) != null && enemy == Dungeon.hero)
			newEnemy = true;
			//The fool is awake
		else if(enemy.buff(MagicalSleep.class) == null && enemy.alignment == alignment)
			newEnemy = true;

		if ( newEnemy ) {

			HashSet<Char> enemies = new HashSet<>();

			//if the mob is amoked...
			if ( buff(Amok.class) != null) {
				//try to find an enemy mob to attack first.
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ENEMY && mob != this && fieldOfView[mob.pos])
						enemies.add(mob);

				if (enemies.isEmpty()) {
					//try to find ally mobs to attack second.
					for (Mob mob : Dungeon.level.mobs)
						if (mob.alignment == Alignment.ALLY && mob != this && fieldOfView[mob.pos])
							enemies.add(mob);

					if (enemies.isEmpty()) {
						//try to find the hero third
						if (fieldOfView[Dungeon.hero.pos]) {
							enemies.add(Dungeon.hero);
						}
					}
				}

				//if the mob is an ally...
			} else if ( alignment == Alignment.ALLY ) {
				//look for hostile mobs that are not passive to attack
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ENEMY
							&& fieldOfView[mob.pos]
							&& mob.state != mob.PASSIVE)
						enemies.add(mob);

				//if the mob is an enemy...
			} else if (alignment == Alignment.ENEMY) {
				//look for ally mobs to attack
				for (Mob mob : Dungeon.level.mobs)
					//fool! don't sleep!
					if ((mob.alignment == Alignment.ALLY || mob.buff(MagicalSleep.class) != null) && fieldOfView[mob.pos])
						enemies.add(mob);

				//and look for the hero
				if (fieldOfView[Dungeon.hero.pos]) {
					enemies.add(Dungeon.hero);
				}

			}

			//neutral character in particular do not choose enemies.
			if (enemies.isEmpty()){
				return null;
			} else {
				//go after the closest potential enemy, preferring the hero if two are equidistant
				Char closest = null;
				for (Char curr : enemies){
					if (closest == null
							|| Dungeon.level.distance(pos, curr.pos) < Dungeon.level.distance(pos, closest.pos)
							|| Dungeon.level.distance(pos, curr.pos) == Dungeon.level.distance(pos, closest.pos) && curr == Dungeon.hero){
						closest = curr;
					}
				}
				return closest;
			}

		} else
			return enemy;
	}

	public static ShopGuardian summonGuardian(){
		Class <?>[] guards = new Class<?>[]{
			ShopGuardian.class,
			ShopGuardianFlying.class,
			ShopGuardianQuick.class,
			ShopGuardianSuper.class,
			ShopGuardianRanger.class,
			ShopGuardianScout.class
		};
		float[] guardsChance = new float[]{10, 1, 1, 1, 1, 1};

		try {
			ShopGuardian w = (ShopGuardian)guards[Random.chances(guardsChance)].newInstance();
			return w;
		} catch (Exception e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}
	}

}
