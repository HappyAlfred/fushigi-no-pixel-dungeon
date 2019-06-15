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

import com.fushiginopixel.fushiginopixeldungeon.Challenges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Amok;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.CloakOfShadows;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.BatSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.SpiritBatSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class SpiritBat extends Bat {

	{
		spriteClass = SpiritBatSprite.class;
		
		HP = HT = 200;
		//defenseSkill = 60;
		baseSpeed = 2f;
		viewDistance = 16;
		
		EXP = 24;

		WANDERING = new Wandering();

		passWall = true;

		properties.add(Property.UNDEAD);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 40, 80 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 80;
	}
	*/
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 8);
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

		if ( newEnemy ) {

			HashSet<Char> enemies = new HashSet<>();

			//if the mob is amoked...
			if ( buff(Amok.class) != null) {
				//try to find an enemy mob to attack first.
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ENEMY && mob != this && distance(mob) <= viewDistance)
						enemies.add(mob);

				if (enemies.isEmpty()) {
					//try to find ally mobs to attack second.
					for (Mob mob : Dungeon.level.mobs)
						if (mob.alignment == Alignment.ALLY && mob != this && distance(mob) <= viewDistance)
							enemies.add(mob);

					if (enemies.isEmpty()) {
						//try to find the hero third
						if (distance(Dungeon.hero) <= viewDistance) {
							enemies.add(Dungeon.hero);
						}
					}
				}

				//if the mob is an ally...
			} else if ( alignment == Alignment.ALLY ) {
				//look for hostile mobs that are not passive to attack
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ENEMY
							&& distance(mob) <= viewDistance
							&& mob.state != mob.PASSIVE)
						enemies.add(mob);

				//if the mob is an enemy...
			} else if (alignment == Alignment.ENEMY) {
				//look for ally mobs to attack
				for (Mob mob : Dungeon.level.mobs)
					if (mob.alignment == Alignment.ALLY && distance(mob) <= viewDistance)
						enemies.add(mob);

				//and look for the hero
				if (distance(Dungeon.hero) <= viewDistance) {
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

	private class Wandering extends Mob.Wandering {
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV && (justAlerted || (enemy!=null && Random.Int(distance(enemy) / 2) == 0))) {

				if(enemy.buff(Invisibility.class) != null || enemy.buff(CloakOfShadows.cloakStealth.class) != null) {
					GLog.n( Messages.get(SpiritBat.class, "ultrasonic"));
					Invisibility.dispel(enemy);
				}
				enemySeen = true;

				noticeByMyself();
				alerted = true;
				state = HUNTING;
				target = enemy.pos;

				if (Dungeon.isChallenged( Challenges.SWARM_INTELLIGENCE )) {
					for (Mob mob : Dungeon.level.mobs) {
						if (Dungeon.level.distance(pos, mob.pos) <= 8 && mob.state != mob.HUNTING) {
							mob.beckon( target );
						}
					}
				}

			}else if(enemy != null) {
				enemySeen = false;
				target = enemy.pos;
				int oldPos = pos;
				if (target != -1 && getCloser(target)) {
					spend(1 / speed());
					return moveSprite(oldPos, pos);
				} else {
					spend(TICK);
					return true;
				}
			}
			return super.act(enemyInFOV,justAlerted);
		}
	}
}
