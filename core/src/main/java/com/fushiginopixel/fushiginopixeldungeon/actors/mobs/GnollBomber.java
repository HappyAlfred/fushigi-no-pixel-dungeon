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
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Firework;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.MissileWeapon;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GnollBomberSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.MissileSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GnollBomber extends Gnoll {

	//When enemy escaped ,throw a firework
	private boolean lastBomb = true;
	{
		spriteClass = GnollBomberSprite.class;

		HP = HT = 35;
		defenseSkill = 5;

		EXP = 6;

		//at half quantity, see createLoot()
		loot = new Firework();
		lootChance = 0.5f;

		HUNTING = new Hunting();
	}

	public int delay = 0;
	public float explodeDelay = 2f;

	private boolean firework(int target)
	{
		final Firework firework = new Firework();
		firework.enemyThrow = this;
		final int cell = new Ballistica( this.pos, target, Ballistica.PROJECTILE ).collisionPos;
		this.sprite.zap( cell );

		Sample.INSTANCE.play( Assets.SND_MISS, 0.6f, 0.6f, 1.5f );

		Char enemy = Actor.findChar( cell );

		final float delay = firework.castDelay(this, target);

		if (enemy != null) {
			((MissileSprite) this.sprite.parent.recycle(MissileSprite.class)).
					reset(this.sprite,
							enemy.sprite,
							firework,
							this,
							new Callback() {
								@Override
								public void call() {
									firework.lightThrow(cell, explodeDelay);
									next();
								}
							});
		} else {
			((MissileSprite) this.sprite.parent.recycle(MissileSprite.class)).
					reset(this.sprite,
							cell,
							firework,
							this,
							new Callback() {
								@Override
								public void call() {
									firework.lightThrow(cell, explodeDelay);
									next();
								}
							});
		}
		return  true;
	}

	private static String LASTBOMB = "lastbomb";
	private static String DELAY = "delay";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LASTBOMB, lastBomb);
		bundle.put(DELAY, delay);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		lastBomb = bundle.getBoolean(LASTBOMB);
		delay = bundle.getInt(DELAY);
	}

	private class Hunting extends Mob.Hunting{
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;

			if ( enemyInFOV
					&& !isCharmedBy( enemy )
					&& !canAttack( enemy ) && distance(enemy ) > 1 &&  new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos) {
				float spend = 2f;
				delay ++;
				if(delay >=3) {
					sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Mob.class, "rage"));
					explodeDelay = 0;
					delay = 0;
				}else explodeDelay = 2f;
				if (enemyInFOV) {
					lastBomb = true;
					target = enemy.pos;
				} else if (enemy == null) {
					state = WANDERING;
					target = Dungeon.level.randomDestination();
					return true;
				}
				if(distance(enemy) > 2 && Random.Int(5) == 0){

					ArrayList<Integer> candidates = new ArrayList<>();
					for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
						int p = enemy.pos + PathFinder.NEIGHBOURS9[i];
						if (new Ballistica( pos, p, Ballistica.PROJECTILE).collisionPos == p && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
							candidates.add( p );
						}
					}
					for(int i=0;i<3 && candidates.size() > 0;i++){
						int index = Random.index( candidates );
						firework(candidates.remove( index ));
					}
					spend(spend);
					return true;
				}
				else{
					firework(enemy.pos);
					spend(spend);
					return true;
				}
			} else if (target != -1 && lastBomb && Dungeon.level.distance( pos, target ) > 2 && new Ballistica( pos, target, Ballistica.PROJECTILE).collisionPos == target) {

					spend(2f);
					firework(target);
					lastBomb = false;
					return true;

				}
				else {
				delay = 0;
				return super.act(enemyInFOV, justAlerted);
			}
		}
	}

}
