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
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.ui.BuffIndicator;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;

public class MagicalSleep extends Buff {

	private static final float STEP = 1f;

	@Override
	public boolean attachTo( Char target, EffectType type ) {
		if (!target.isImmune(Sleep.class,type) && super.attachTo( target,type )) {

			if (target instanceof Hero)
				if (target.HP == target.buff(Regeneration.class).regencap()) {
					GLog.i(Messages.get(this, "toohealthy"));
					detach();
					return true;
				} else {
					GLog.i(Messages.get(this, "fallasleep"));
				}
			else if (target instanceof Mob)
				((Mob)target).state = ((Mob)target).SLEEPING;

			target.paralysed++;

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean act(){
		if (target instanceof Mob && ((Mob) target).state != ((Mob) target).SLEEPING){
			detach();
			return true;
		}
		if (target instanceof Hero) {
			target.HP = Math.min(target.HP+1, target.HT);
			((Hero) target).resting = true;
			if (target.HP == target.buff(Regeneration.class).regencap()) {
				GLog.p(Messages.get(this, "wakeup"));
				detach();
			}
		}
		spend( STEP );
		return true;
	}

	@Override
	public void detach() {
		if (target.paralysed > 0)
			target.paralysed--;
		if (target instanceof Hero)
			((Hero) target).resting = false;
		super.detach();
	}

	@Override
	public int icon() {
		return BuffIndicator.MAGIC_SLEEP;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}