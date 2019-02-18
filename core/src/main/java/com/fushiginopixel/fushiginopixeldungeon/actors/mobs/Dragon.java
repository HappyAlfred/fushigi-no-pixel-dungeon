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
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Fire;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Gold;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.DragonSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GnollSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Dragon extends Mob {

	private static final float TIME_TO_ZAP	= 1f;
	{
		spriteClass = DragonSprite.class;
		
		HP = HT = 120;
		defenseSkill = 16;
		
		EXP = 19;

		HUNTING = new Hunting();

		flying = true;
		
		loot = Generator.Category.RING;
		lootChance = 0.1f;

		properties.add(Property.FIERY);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 25, 35 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 42;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(2, 20);
	}

	public void zap(Ballistica bolt ) {

		for (int c : bolt.subPath(1, bolt.dist)) {
			if (Dungeon.level.flamable[c]) {

				GameScene.add( Blob.seed( c, 4, Fire.class ) );

			}

		}
		Char ch = Actor.findChar( bolt.collisionPos );
		if(ch == null) return;
		if (hit( this, ch, true )) {
			Buff.affect( ch, Burning.class, new EffectType(EffectType.MAGICAL_BOLT,EffectType.FIRE) ).reignite(ch);

			int dmg = Random.Int( 10, 25 );
			ch.damage( dmg, this ,new EffectType(EffectType.MAGICAL_BOLT,EffectType.FIRE));

			if (!ch.isAlive() && ch == Dungeon.hero) {
				Dungeon.fail( getClass() );
				GLog.n( Messages.get(this, "breath_kill") );
			}
		} else {
			ch.sprite.showStatus( CharSprite.NEUTRAL,  ch.defenseVerb() );
		}
	}

	public void onZapComplete(Ballistica bolt) {
		zap(bolt);
		next();
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

				Ballistica shot = new Ballistica(pos,enemy.pos,Ballistica.MAGIC_BOLT);
				if(Random.Int(4) == 0 && shot.collisionPos == enemy.pos){
					boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
					spend( TIME_TO_ZAP );
					if (visible) {
						sprite.zap( enemy.pos );
					} else {
						zap(shot);
					}
					return true;
				}
				else return doAttack( enemy );

			} else {

				if (enemyInFOV) {
					Ballistica shot = new Ballistica(pos,enemy.pos,Ballistica.MAGIC_BOLT);
					if(!isCharmedBy( enemy ) && Random.Int(4) == 0 && shot.collisionPos == enemy.pos){
						boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
						spend( TIME_TO_ZAP );
						if (visible) {
							sprite.zap( enemy.pos );
						} else {
							zap(shot);
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
