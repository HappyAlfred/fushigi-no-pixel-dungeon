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

package com.fushiginopixel.fushiginopixeldungeon.actors.buffs;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Drowsy extends Buff {

	{
		type = buffType.NEUTRAL;
	}

	public float cooldown = Random.Int(3,6);

	@Override
	public int icon() {
		return BuffIndicator.DROWSY;
	}

	public boolean attachTo( Char target, EffectType type ) {
		if (!target.isImmune(Sleep.class, new EffectType(0,EffectType.SPIRIT)) && super.attachTo(target,type)) {
			if (cooldown() == 0)
				spend(cooldown);
			return true;
		}
		return false;
	}

	@Override
	public boolean act(){
		Buff.affect(target, MagicalSleep.class, new EffectType(EffectType.BUFF,EffectType.SPIRIT));

		detach();
		return true;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(cooldown()+1));
	}
}
