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

package com.fushiginopixel.fushiginopixeldungeon.items.rings;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bleeding;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Cripple;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Roots;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bombs;

import java.util.HashSet;

public class RingOfTenacity extends Ring {

	public static final HashSet<Class> RESISTS = new HashSet<>();
	static {
		RESISTS.add( Bleeding.class );
		RESISTS.add( Blindness.class );
		RESISTS.add( Cripple.class );
		RESISTS.add( Roots.class );
		RESISTS.add( Vertigo.class );

		RESISTS.add( Bombs.class );
	}

	@Override
	protected RingBuff buff( ) {
		return new Tenacity();
	}

	public static float resist( Char target, Class effect ){
		if (getBonus(target, Tenacity.class) == 0) return 1f;

		if (getBonus(target, Tenacity.class) > 5){
			for (Class c : RESISTS){
				if (c.isAssignableFrom(effect) && Buff.class.isAssignableFrom(effect)){
					return 0;
				}
			}
		}

		for (Class c : RESISTS){
			if (c.isAssignableFrom(effect)){
				return (float)Math.pow(0.925, getBonus(target, Tenacity.class));
			}
		}

		return 1f;
	}
	
	public static float damageMultiplier( Char t ){
		//(HT - HP)/HT = heroes current % missing health.
		return (float)Math.pow(0.85, getBonus( t, Tenacity.class)*((float)(t.HT - t.HP)/t.HT));
	}

	public class Tenacity extends RingBuff {
	}
}

