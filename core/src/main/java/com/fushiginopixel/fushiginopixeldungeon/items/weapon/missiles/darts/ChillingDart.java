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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Chill;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

public class ChillingDart extends TippedDart {
	
	{
		image = ItemSpriteSheet.CHILLING_DART;
	}
	
	@Override
	public int procInAttack(Char attacker, Char defender, int damage, EffectType type) {
		
		if (Dungeon.level.water[defender.pos]){
			Buff.prolong(defender, Chill.class, 10f,new EffectType(type.attachType,EffectType.ICE));
		} else {
			Buff.prolong(defender, Chill.class, 6f,new EffectType(type.attachType,EffectType.ICE));
		}
		
		return super.procInAttack(attacker, defender, damage, type);
	}
}
