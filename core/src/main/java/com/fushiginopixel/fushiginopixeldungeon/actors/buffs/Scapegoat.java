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

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Scapegoat extends FlavourBuff {

	public static final float DURATION	= 15f;
	protected Class<? extends CharSprite> spClass;

	{
		type = buffType.NEGATIVE;
	}

	public void set(Class<? extends CharSprite> spClass) {
		this.spClass = spClass;
	}

	@Override
	public void fx(boolean on) {
		if (target instanceof Mob) {
			if (on) {
				CharSprite charSprite = ((Mob) target).changeSprite(spClass);
				charSprite.link(target);
			}else {
				CharSprite charSprite = ((Mob) target).sprite();
				charSprite.link(target);
			}

		}
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}

	private static final String SPCLASS	= "spClass";
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( SPCLASS, spClass );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		spClass = bundle.getClass( SPCLASS );
	}

	@Override
	public void detach() {
		float cd = cooldown();
		super.detach();
		if(target == null || !target.isAlive()){
			return;
		}
		if(cd > 0) {
			Buff.prolong(target, Vertigo.class, cd, new EffectType(EffectType.BUFF, 0));
		}
	}
}
