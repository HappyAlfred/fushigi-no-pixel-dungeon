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
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Poison;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

public class PoisonDart extends TippedDart {
	
	{
		image = ItemSpriteSheet.POISON_DART;
	}
	
	@Override
	public int procInAttack(Char attacker, Char defender, int damage, EffectType type) {

		EffectType buffType = new EffectType(type.attachType,EffectType.POISON);
		Buff.affect( defender, Poison.class, buffType ).set( 3 + Dungeon.depth / 3, buffType );

		buffType = new EffectType(type.attachType,EffectType.POISON);
		Buff.affect( defender, Weakness.class, buffType ).addUp(1, buffType);
		return super.procInAttack(attacker, defender, damage, type);
	}
}
