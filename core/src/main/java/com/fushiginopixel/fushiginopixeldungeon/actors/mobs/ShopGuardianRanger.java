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
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Crossbow;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Quarterstaff;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.darts.Dart;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.sprites.MissileSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.StatueSprite;
import com.watabou.utils.Callback;

public class ShopGuardianRanger extends ShopGuardian {

	{
		spriteClass = ShopGuardianRangerSprite.class;

	}

	public ShopGuardianRanger(){
		super();
		belongings.weapon = new Crossbow();

	}


	@Override
	public boolean canAttack( Char enemy ) {
		Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
		return attack.collisionPos == enemy.pos;
	}

	@Override
	public int attackSkill( Char target ) {
		int attack = super.attackSkill(target);
		return distance(target) > 1 ?  attack : attack/2;
	}

	public static class ShopGuardianRangerSprite extends StatueSprite {
		private Animation cast;

		boolean type = true;

		@Override
		public void init() {
			super.init();
			cast = attack.clone();
		}

		public ShopGuardianRangerSprite(){
			super();
			tint(1, 0.75f, 0, 0.2f);
		}

		@Override
		public void resetColor() {
			super.resetColor();
			tint(1, 0.75f, 0, 0.2f);
		}

		@Override
		public void attack( int cell ) {
			if (!Dungeon.level.adjacent(cell, ch.pos)) {

				((MissileSprite)parent.recycle( MissileSprite.class )).
						reset( ch.pos, cell, new Dart(),ch , new Callback() {
							@Override
							public void call() {
								ch.onAttackComplete();
							}
						} );

				play( cast );
				turnTo( ch.pos , cell );

			} else {

				super.attack( cell );

			}
		}
	}

}
