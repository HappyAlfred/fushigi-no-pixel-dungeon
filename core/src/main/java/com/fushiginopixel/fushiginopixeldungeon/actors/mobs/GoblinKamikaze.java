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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Chill;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Frost;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bomb;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GoblinBlasterSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GoblinKamikazeSprite;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class GoblinKamikaze extends GoblinBlaster {

	{
		spriteClass = GoblinKamikazeSprite.class;
		
		HP = HT = 190;
		//defenseSkill = 30;
		
		EXP = 1;
		
		loot = new Bomb();
		lootChance = 0.05f;

		HUNTING = new Hunting();
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 1 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 57;
	}
	*/

	protected boolean expAct(){

		if(buff(Burning.class) != null){
			suicide();
		}

		if(buff(Frost.class) != null || buff(Chill.class) != null){
			coolDown();
			return false;
		}

		if(HP < HT/4){
			die(this,new EffectType(EffectType.BURST,0));
			return true;
		}

		if(countDown != -1){
			countDown --;
			if(countDown <= 0){
				die(this ,new EffectType(EffectType.BURST,0));
				return true;
			}
		}

		return false;

	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 12);
	}

	@Override
	public void die( Object cause, EffectType type ) {

		if(cause instanceof Burning || type.isExistAttachType(EffectType.BURST)){
			selfExplode();

			destroy();
			((GoblinKamikazeSprite)sprite).explodeFlag = true;
			sprite.die();
		}else {
			super.die(cause, type);
		}
	}

	private boolean jump(int target){
		Ballistica route = new Ballistica(pos, target, Ballistica.PROJECTILE);

		int cell = route.collisionPos;

		//can't occupy the same cell as another char, so move back one.
		if (Actor.findChar( cell ) != null && cell != pos) {
			cell = route.path.get(route.dist - 1);
		}
		if (cell == pos || cell == -1
				|| Dungeon.level.pit[cell])
			return false;
		else {
			final int dest = cell;
			final GoblinKamikaze user = this;
			sprite.jump(user.pos, cell, new Callback() {
				@Override
				public void call() {
					user.pos = dest;
					Dungeon.level.press(dest, user, true);

					CellEmitter.center(dest).burst(Speck.factory(Speck.DUST), 10);
					Camera.main.shake(2, 0.5f);

					suicide();
					user.spend(TICK);
					user.next();

				}
			});
		}
		return true;
	}

	protected class Hunting extends GoblinSapper.Hunting {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;

			if (enemyInFOV
					&& !isCharmedBy( enemy )
					&& !canAttack( enemy )

					&& jump(enemy.pos)){
				return false;
			} else {
				return super.act( enemyInFOV, justAlerted );
			}
		}
	}
}
