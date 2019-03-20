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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite.Glowing;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Energy extends Weapon.Enchantment {

	private static Glowing CREAM = new Glowing( 0xF7EED6 );

	public int charge = 0;
	public boolean charged = false;
	
	@Override
	public float proc( Weapon weapon, Char attacker, Char defender, int damage , EffectType type ) {
		return 1;
	}

	@Override
	public boolean canCriticalAttack( Weapon weapon, Char attacker, Char defender, int damage , EffectType type ) {

		if (charged){
			charge = 0;
			charged = false;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public float accuracyAdapt( Weapon weapon, Char attacker ,Char target, float acc  ) {
		if (charged){
			return 1000;
		} else {

			return 1;
		}
	}

	@Override
	public void onMissed(Weapon weapon, Char attacker, Char defender){
		int level = Math.max( 0, weapon.level() );

		float chargeTop = (float)(5 / (level * 0.2 + 1));
		chargeTop = Math.max(chargeTop , 2);

		charge++;

		if (charge >= chargeTop && attacker != null) {
			GLog.n(Messages.get(this, "charged", attacker.name));
			charged = true;
		}
	}

	@Override
	public Glowing glowing() {
		return CREAM;
	}
	
}
