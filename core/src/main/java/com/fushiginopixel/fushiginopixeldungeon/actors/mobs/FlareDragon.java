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
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Fire;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Amok;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.DragonSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.FlareDragonSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.HashSet;

public class FlareDragon extends Dragon {

	private static final float TIME_TO_ZAP	= 1f;
	{
		spriteClass = FlareDragonSprite.class;

		HP = HT = 500;
		//defenseSkill = 54;
		viewDistance = 16;

		EXP = 30;

		WANDERING = new Wandering();
		HUNTING = new Hunting();

		flying = true;

		properties.add(Property.FIERY);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 60, 100 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 85;
	}
	*/

	@Override
	public int drRoll() {
		return Random.NormalIntRange(2, 20);
	}

	public void zap(Ballistica bolt ) {

		Char ch = enemy;//Actor.findChar( bolt.collisionPos );
		if(ch == null) return;
		if (hit( this, ch, true )) {

			int dmg = Random.Int( 25, 50 );
			ch.damage( dmg, this, new EffectType(EffectType.MAGICAL_BOLT,EffectType.LIGHT) );

			if (!ch.isAlive() && ch == Dungeon.hero) {
				Dungeon.fail( getClass() );
				GLog.n( Messages.get(this, "breath_kill") );
			}
		} else {
			ch.sprite.showStatus( CharSprite.NEUTRAL,  ch.defenseVerb() );
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

	protected class Wandering implements AiState {

		public static final String TAG	= "WANDERING";

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV && (justAlerted || Random.Int( distance( enemy ) / 2 + enemy.stealth() ) == 0)) {

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

			} else {

				enemySeen = false;

				int oldPos = pos;
				Ballistica shot = null;
				if(enemy != null) {
					shot = new Ballistica(pos, enemy.pos, Ballistica.STOP_TARGET);
				}
				if(enemy != null && enemy.invisible <= 0 && distance(enemy) <= viewDistance && Random.Int(4) == 0 && shot.collisionPos == enemy.pos){
					boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
					spend(TIME_TO_ZAP);
					if (visible) {
						sprite.zap(enemy.pos);
					} else {
						zap(shot);
					}
				} else if (target != -1 && getCloser( target )) {
					spend( 1 / speed() );
					return moveSprite( oldPos, pos );
				} else {
					target = Dungeon.level.randomDestination();
					spend( TICK );
				}

			}
			return true;
		}
	}

	protected class Hunting extends Mob.Hunting {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

				Ballistica shot = new Ballistica(pos,enemy.pos,Ballistica.STOP_TARGET);
				if(Random.Int(4) == 0 && shot.collisionPos == enemy.pos){
					boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
					spend( TIME_TO_ZAP );
					if (visible) {
						sprite.zap( enemy.pos );
					} else {
						zap(shot);
					}
					return !visible;
				}
				else return doAttack( enemy );

			} else {

				if (enemy != null && enemy.invisible <= 0 && distance(enemy) <= viewDistance) {
					Ballistica shot = new Ballistica(pos,enemy.pos,Ballistica.STOP_TARGET);
					if(!isCharmedBy( enemy ) && Random.Int(4) == 0 && shot.collisionPos == enemy.pos){
						boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
						spend( TIME_TO_ZAP );
						if (visible) {
							sprite.zap( enemy.pos );
						} else {
							zap(shot);
						}
						return !visible;
					}
					target = enemy.pos;
				} else if (enemy == null) {
					state = WANDERING;
					target = Dungeon.level.randomDestination();
					return true;
				}

				int oldPos = pos;
				if (target != -1 && getCloser( target )) {

					spend( 1 / speed() );
					return moveSprite( oldPos,  pos );

				} else {
					spend( TICK );
					if (!enemyInFOV) {
						sprite.showLost();
						state = WANDERING;
						target = Dungeon.level.randomDestination();
					}
					return true;
				}
			}
		}
	}
}
