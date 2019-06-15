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
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.CloakOfShadows;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfHealing;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.BatSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Bat extends Mob {

	{
		spriteClass = BatSprite.class;
		
		HP = HT = 12;
		//defenseSkill = 4;
		baseSpeed = 2f;
		
		EXP = 2;

		WANDERING = new Wandering();
		HUNTING = new Hunting();
		
		flying = true;
	}

	{
		immunities.add(new EffectType(Blindness.class) );
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 5 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 12;
	}
	*/
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}

	@Override
	protected boolean act() {

		super.act(this);

		boolean justAlerted = alerted;
		alerted = false;

		if (justAlerted){
			sprite.showAlert();
		} else {
			sprite.hideAlert();
			sprite.hideLost();
		}

		if (paralysed > 0) {
			enemySeen = false;
			spend( TICK );
			return true;
		}

		enemy = chooseEnemy();

		boolean enemyInFOV = enemy != null && enemy.isAlive() && fieldOfView[enemy.pos];

		return state.act( enemyInFOV, justAlerted );
	}

	private class Wandering extends Mob.Wandering {
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV && (justAlerted || (enemy!=null && Random.Int(distance(enemy) / 2) == 0))) {

				if(enemy.buff(Invisibility.class) != null || enemy.buff(CloakOfShadows.cloakStealth.class) != null) {
					GLog.n( Messages.get(Bat.class, "ultrasonic"));
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

			}else {

				enemySeen = false;

				int oldPos = pos;
				if (target != -1 && getCloser( target )) {
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

	private class Hunting extends Mob.Hunting {
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV && !isCharmedBy( enemy )) {

				if(enemy.buff(Invisibility.class) != null || enemy.buff(CloakOfShadows.cloakStealth.class) != null) {
					GLog.n( Messages.get(Bat.class, "ultrasonic"));
					Invisibility.dispel(enemy);
				}

			}
			return super.act(enemyInFOV,justAlerted);
		}
	}
}
