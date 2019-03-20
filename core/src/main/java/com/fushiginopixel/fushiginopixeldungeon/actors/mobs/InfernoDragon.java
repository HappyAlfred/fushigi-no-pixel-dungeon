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
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.DragonSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.InfernoDragonSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class InfernoDragon extends Dragon {

	private static final float TIME_TO_ZAP	= 1f;
	{
		spriteClass = InfernoDragonSprite.class;
		
		HP = HT = 500;
		defenseSkill = 43;
		
		EXP = 30;

		HUNTING = new Hunting();

		loot = Generator.Category.RING;
		lootChance = 0.1f;

		properties.add(Property.FIERY);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 60, 100 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 100;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(2, 20);
	}

	private HashSet<Integer> affectedCells = new HashSet<>();
	private HashSet<Integer> visualCells = new HashSet<>();
	int direction = 0;

	public void dragonBreath( Ballistica bolt, Callback callback ) {
		//need to perform flame spread logic here so we can determine what cells to put flames in.
		affectedCells = new HashSet<>();
		visualCells = new HashSet<>();
		// 4/6/9 distance
		int maxDist = 6;
		int dist = Math.min(bolt.dist, maxDist);

		for (int i = 0; i < PathFinder.CIRCLE8.length; i++){
			if (bolt.sourcePos+PathFinder.CIRCLE8[i] == bolt.path.get(1)){
				direction = i;
				break;
			}
		}

		float strength = maxDist;
		for (int c : bolt.subPath(1, dist)) {
			strength--; //as we start at dist 1, not 0.
			affectedCells.add(c);
			if (strength > 1) {
				spreadFlames(c + PathFinder.CIRCLE8[left(direction)], strength - 1);
				spreadFlames(c + PathFinder.CIRCLE8[direction], strength - 1);
				spreadFlames(c + PathFinder.CIRCLE8[right(direction)], strength - 1);
			} else {
				visualCells.add(c);
			}
		}

		//going to call this one manually
		visualCells.remove(bolt.path.get(dist));

		for (int cell : visualCells){
			//this way we only get the cells at the tip, much better performance.
			((MagicMissile)this.sprite.parent.recycle( MagicMissile.class )).reset(
					MagicMissile.FIRE_CONE,
					this.sprite,
					cell,
					null
			);
		}
		MagicMissile.boltFromChar( this.sprite.parent,
				MagicMissile.FIRE_CONE,
				this.sprite,
				bolt.path.get(dist/2),
				callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	private void spreadFlames(int cell, float strength){
		if (strength >= 0 && (Dungeon.level.passable[cell] || Dungeon.level.flamable[cell])){
			affectedCells.add(cell);
			if (strength >= 1.5f) {
				visualCells.remove(cell);
				spreadFlames(cell + PathFinder.CIRCLE8[left(direction)], strength - 1.5f);
				spreadFlames(cell + PathFinder.CIRCLE8[direction], strength - 1.5f);
				spreadFlames(cell + PathFinder.CIRCLE8[right(direction)], strength - 1.5f);
			} else {
				visualCells.add(cell);
			}
		} else if (!Dungeon.level.passable[cell])
			visualCells.add(cell);
	}

	private int left(int direction){
		return direction == 0 ? 7 : direction-1;
	}

	private int right(int direction){
		return direction == 7 ? 0 : direction+1;
	}

	public void zap(Ballistica bolt ) {

		for( int cell : affectedCells){

			//ignore caster cell
			if (cell == bolt.sourcePos){
				continue;
			}

			//only ignite cells directly near caster if they are flammable
			if (!Dungeon.level.adjacent(bolt.sourcePos, cell)
					|| Dungeon.level.flamable[cell]){
				GameScene.add( Blob.seed( cell, 4, Fire.class ) );
			}


			Char ch = Actor.findChar( cell );
			if(ch == null) continue;
			if (ch != null) {
				ch.damage(Random.Int( 25, 50 ), this,new EffectType(EffectType.MAGICAL_BOLT,EffectType.FIRE));
				Buff.affect( ch, Burning.class, new EffectType(EffectType.MAGICAL_BOLT,EffectType.FIRE) ).reignite( ch );
			}
			if (!ch.isAlive() && ch == Dungeon.hero) {
				Dungeon.fail( getClass() );
				GLog.n( Messages.get(this, "breath_kill") );
			}
		}
	}

	@Override
	protected Item createLoot() {
		Item loot;
		loot = Generator.random(Generator.Category.RING);
		loot.cursed = false;
		loot.level(0);
		return loot;
	}

	protected class Hunting extends Mob.Hunting {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

				final Ballistica shot = new Ballistica(pos,enemy.pos,Ballistica.STOP_TERRAIN);
				if(distance(enemy ) <= 6 && Random.Int(4) == 0){
					boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
					spend( TIME_TO_ZAP );
					if (visible) {
						sprite.zap( enemy.pos );
					} else {
						dragonBreath(shot, new Callback() {
							public void call() {
								zap(shot);
							}
						});
					}
					return true;
				}
				else return doAttack( enemy );

			} else {

				if (enemyInFOV) {
					final Ballistica shot = new Ballistica(pos,enemy.pos,Ballistica.STOP_TERRAIN);
					if(!isCharmedBy( enemy ) && distance(enemy ) <= 6 && Random.Int(4) == 0){
						boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
						spend( TIME_TO_ZAP );
						if (visible) {
							sprite.zap( enemy.pos );
						} else {
							dragonBreath(shot, new Callback() {
								public void call() {
									zap(shot);
								}
							});
						}
						return true;
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
