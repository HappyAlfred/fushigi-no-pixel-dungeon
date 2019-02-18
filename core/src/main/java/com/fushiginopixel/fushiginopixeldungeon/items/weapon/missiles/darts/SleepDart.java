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

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.FlavourBuff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Sleep;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

public class SleepDart extends TippedDart {
	
	{
		image = ItemSpriteSheet.SLEEP_DART;
	}
	
	@Override
	public int proc(Char attacker, final Char defender, int damage,EffectType type) {
		
		//need to delay this so damage from the dart doesn't break the sleep
		final EffectType effectType = type;
		new FlavourBuff(){
			{actPriority = VFX_PRIO;}
			public boolean act() {
				Buff.affect( defender, Sleep.class, new EffectType(effectType.attachType,EffectType.SPIRIT) );
				return super.act();
			}
		}.attachTo(defender);
		
		return super.proc(attacker, defender, damage, type);
	}
}
