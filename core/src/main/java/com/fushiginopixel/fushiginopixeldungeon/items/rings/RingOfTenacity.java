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
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bleeding;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Cripple;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Roots;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bombs;

import java.util.HashSet;

public class RingOfTenacity extends Ring {

	public static final HashSet<EffectType> RESISTS = new HashSet<>();
	static {
		RESISTS.add( new EffectType(EffectType.MELEE, 0) );
		RESISTS.add( new EffectType(EffectType.MISSILE, 0) );
		RESISTS.add( new EffectType(EffectType.BURST, 0) );

		RESISTS.add( new EffectType(Bleeding.class) );
		RESISTS.add( new EffectType(Cripple.class) );
		/*
		RESISTS.add( new EffectType(Bleeding.class) );
		RESISTS.add( new EffectType(Cripple.class) );
		RESISTS.add( new EffectType(Roots.class) );
		RESISTS.add( new EffectType(Vertigo.class) );

		RESISTS.add( new EffectType(Bombs.class) );
		*/
	}

	@Override
	protected RingBuff buff( ) {
		return new Tenacity();
	}

	public static float resist( Char target, EffectType effect ){
		/*
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
		*/
		for (EffectType ef : RESISTS){
			if(EffectType.isExistType(effect, ef)){
				boolean buff = Buff.class.isAssignableFrom(effect.attachClass) || effect.isExistAttachType(EffectType.BUFF);
				return resistMultiplier(target, buff);
			}
		}

		return 1f;
	}

	public static float resistMultiplier( Char target, boolean buff ){
		if (buff && getBonus(target, Tenacity.class) > 5){
			return 0;
		}else if(getBonus(target, Tenacity.class) > 5)
			return 0.75f;
		else return (float)Math.pow(0.975, getBonus(target, Tenacity.class));
	}

	/*
	public static float damageMultiplier( Char t ){
		//(HT - HP)/HT = heroes current % missing health.
		return (float)Math.pow(0.85, getBonus( t, Tenacity.class)*((float)(t.HT - t.HP)/t.HT));
	}
	*/

	public class Tenacity extends RingBuff {
	}
}

