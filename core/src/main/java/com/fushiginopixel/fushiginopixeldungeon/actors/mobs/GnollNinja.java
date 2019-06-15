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
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Fire;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Poison;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Ghost;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Firework;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.MissileWeapon;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GnollNinjaSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GnollTricksterSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.MissileSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class GnollNinja extends Gnoll {

	{
		spriteClass = GnollNinjaSprite.class;

		HP = HT = 70;
		//defenseSkill = 20;

		EXP = 13;

		//at half quantity, see createLoot()
		loot = Generator.Category.MISSILE;
		lootChance = 0.05f;

		HUNTING = new Hunting();
	}

	private boolean justMoved = false;
	private boolean confirmMoved = false;
	private boolean lastBomb = false;
	public float explodeDelay = 2f;

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 13, 22 );
	}

	@Override
	public int attackSkill( Char target ) {
		int attack = super.attackSkill(target);
		return distance(target) > 1 ?  attack : attack/2;
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
		return Dungeon.level.adjacent(pos, enemy.pos) || attack.collisionPos == enemy.pos;
	}

	@Override
	public void move( int step ) {
		justMoved = true;
		confirmMoved = true;
		super.move(step);
	}

	@Override
	public void spend( float time ) {
		if(!confirmMoved) {
			justMoved = false;
		}else{
			confirmMoved = false;
		}
		super.spend(time);
	}

	@Override
    public float attackDelay(){
		return justMoved ? 0 : 1f;
	}
	
	@Override
	protected Item createLoot() {
		MissileWeapon drop = (MissileWeapon)super.createLoot();
		//half quantity, rounded up
		drop.quantity((drop.quantity()+1)/2);
		return drop;
	}

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

	private static final String JUSTMOVED = "justMoved";
	private static final String CONFIRMMOVED = "confirmMoved";
	private static final String LASTBOMB = "lastbomb";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put(JUSTMOVED, justMoved);
		bundle.put(CONFIRMMOVED, confirmMoved);
		bundle.put(LASTBOMB, lastBomb);
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		justMoved = bundle.getBoolean( JUSTMOVED );
		confirmMoved = bundle.getBoolean( CONFIRMMOVED );
		lastBomb = bundle.getBoolean( LASTBOMB );
	}

	private class Hunting extends Mob.Hunting{
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;

			if ( enemyInFOV
					&& !isCharmedBy( enemy )
					&& canAttack( enemy ) && new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos) {
				if (enemyInFOV) {
					lastBomb = true;
					target = enemy.pos;
				} else if (enemy == null) {
					state = WANDERING;
					target = Dungeon.level.randomDestination();
					return true;
				}

				int oldPos = pos;
				if(justMoved && distance(enemy ) <= 2){
					return doAttack(enemy);
				}else if(distance(enemy) <= 2 && target != -1 && getFurther( target )){

					spend( 1 / speed() );
					return moveSprite( oldPos, pos );

				}else{
					if(Random.Int(6) <= 1 && distance(enemy ) > 1) {
						firework(enemy.pos);
						spend(1f);
						return true;
					}else{
						return doAttack(enemy);
					}
				}
			} else if (!enemyInFOV && target != -1 && lastBomb && Dungeon.level.distance( pos, target ) > 2 && new Ballistica( pos, target, Ballistica.PROJECTILE).collisionPos == target) {

				spend(1f);
				firework(target);
				lastBomb = false;
				return true;

			}else {
				return super.act(enemyInFOV, justAlerted);
			}
		}
	}

}
