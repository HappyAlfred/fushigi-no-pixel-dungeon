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
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.food.SlimyGel;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.InventoryPot;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.CursingTrap;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.DragonSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GelCubeSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GelCube extends Mob {

	private static final float TIME_TO_ZAP	= 1f;
	{
		spriteClass = GelCubeSprite.class;
		
		HP = HT = 30;
		defenseSkill = 7;
		
		EXP = 5;

		HUNTING = new Hunting();

		loot = SlimyGel.class;
		lootChance = 0.05f;

		properties.add(Property.ACIDIC);
	}

	public int zapSkill() {
		return 0;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 11 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 13;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}

	public void zap(Ballistica bolt ) {

		Char ch = Actor.findChar( bolt.collisionPos );
		if(ch == null) return;
		if (hit( this, ch, false )) {

			if(zapSkill() > Random.Int(3) && ch instanceof Hero && shootPot((Hero)ch)) {

			}else{
				attack( ch , new EffectType(EffectType.MISSILE ,0));
				/*
				int dmg = Random.Int(1, 9);
				ch.damage(dmg, this, new EffectType(EffectType.MISSILE, 0));

				if (!ch.isAlive() && ch == Dungeon.hero) {
					Dungeon.fail(getClass());
					GLog.n(Messages.get(this, "gel_kill"));
				}
				*/
			}
		} else {
			ch.sprite.showStatus( CharSprite.NEUTRAL,  ch.defenseVerb() );
		}
	}

	public boolean shootPot(Hero hero){
		ArrayList<Item> canShoot = new ArrayList<>();
		for(Item itemhad : hero.belongings) {
			if(itemhad instanceof Bag) {
				for (Item item : hero.belongings.backpack.items) {
					if (item instanceof InventoryPot && !((InventoryPot)item).isFull()) {
						canShoot.add(item);
					}
				}
			}
		}

		if(!canShoot.isEmpty()) {
			InventoryPot pot = (InventoryPot) Random.element(canShoot);
			pot.input(new SlimyGel());
			GLog.n( Messages.get(this, "gel_zap" , pot.name()) );
			return true;
		}else return false;
	}

	public void onZapComplete(Ballistica bolt) {
		zap(bolt);
		next();
	}

	protected class Hunting extends Mob.Hunting {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

				Ballistica shot = new Ballistica(pos,enemy.pos,Ballistica.PROJECTILE);
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
					Ballistica shot = new Ballistica(pos,enemy.pos,Ballistica.PROJECTILE);
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
