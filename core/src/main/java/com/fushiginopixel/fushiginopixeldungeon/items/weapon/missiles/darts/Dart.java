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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.darts;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Ghostly;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Projecting;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Crossbow;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.MissileWeapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

public class Dart extends MissileWeapon {

	{
		image = ItemSpriteSheet.DART;

		usageAdapt = 0;
	}

	@Override
	public int min(int lvl) {
		return bow != null ? 4 + bow.level() : 1;
	}

	@Override
	public int max(int lvl) {
		//return bow != null ? 12 + 3*bow.level() : 2;
		return bow != null ? 16 + (UPGRADE_ATTACK - 1)*bow.level() : 5;
	}
	/*
	@Override
	protected float durabilityPerUse() {
		return 0;
	}
	*/
	
	private static Crossbow bow;
	
	private void updateCrossbow(){
		if (Dungeon.hero.belongings.weapon instanceof Crossbow){
			bow = (Crossbow) Dungeon.hero.belongings.weapon;
		} else {
			bow = null;
		}
	}
	
	@Override
	public int throwPos(Char user, int from, int dst) {
		KindOfWeapon wep = user.belongings.weapon;
		Crossbow curBow = wep instanceof Crossbow ? (Crossbow)wep : null;
		if (curBow != null && curBow.hasEnchant(Ghostly.class)
				&& !Dungeon.level.solid[dst] && Dungeon.level.distance(from, dst) <= 4){
			return dst;
		} else {
			return super.throwPos(user, from, dst);
		}
	}
	
	@Override
	public int procInAttack(Char attacker, Char defender, int damage, EffectType type) {
		KindOfWeapon wep = attacker.belongings.weapon;
		Crossbow curBow = wep instanceof Crossbow ? (Crossbow)wep : null;
		if (curBow != null){
			damage = curBow.procInAttack(attacker, defender, damage, type);
		}
		return super.procInAttack(attacker, defender, damage, type);
	}
	
	@Override
	protected void onThrow(int cell) {
		updateCrossbow();

		KindOfWeapon wep = curUser.belongings.weapon;
		Crossbow curBow = wep instanceof Crossbow ? (Crossbow)wep : null;
		Char enemy = Actor.findChar( cell );
		if(curBow != null && curBow.hasEnchant(Projecting.class) && enemy == null){
			for (Char ch : Actor.chars()){
				int collisionPos= throwPos(curUser, cell, ch.pos);
				if (collisionPos == ch.pos &&
						ch != curUser &&
						(enemy == null || Dungeon.level.trueDistance(cell, ch.pos) < Dungeon.level.trueDistance(cell, enemy.pos))){
					enemy = ch;
				}
			}
			if (enemy != null) {
				turnToOthers(this, curUser, enemy, cell);
				return;
			}
		}
		super.onThrow(cell);
	}
	
	@Override
	public String info() {
		updateCrossbow();
		return super.info();
	}
	
	@Override
	public int price() {
		return 4 * quantity;
	}
}
