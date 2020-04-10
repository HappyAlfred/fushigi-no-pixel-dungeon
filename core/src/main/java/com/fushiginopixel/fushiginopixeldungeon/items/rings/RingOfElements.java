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
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Electricity;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Charm;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Chill;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Corrosion;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Frost;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Ooze;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Poison;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Dragon;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.DeathEye;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.FallenAngel;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Lich;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Shaman;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Warlock;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Yog;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.DisintegrationTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.GrimTrap;

import java.util.HashSet;

public class RingOfElements extends Ring {
	
	@Override
	protected RingBuff buff( ) {
		return new Resistance();
	}

	/*
	public static final HashSet<Class> RESISTS = new HashSet<>();
	static {
		RESISTS.add( Burning.class );
		RESISTS.add( Charm.class );
		RESISTS.add( Chill.class );
		RESISTS.add( Frost.class );
		RESISTS.add( Ooze.class );
		RESISTS.add( Paralysis.class );
		RESISTS.add( Poison.class );
		RESISTS.add( Corrosion.class );
		RESISTS.add( Weakness.class );
		
		RESISTS.add( DisintegrationTrap.class );
		RESISTS.add( GrimTrap.class );
		
		RESISTS.add( ToxicGas.class );
		RESISTS.add( Electricity.class );

		RESISTS.add( FallenAngel.class );
		RESISTS.add( Shaman.class );
		RESISTS.add( Dragon.class );
		RESISTS.add( Warlock.class );
		RESISTS.add( Lich.class );
		RESISTS.add( DeathEye.class );
		RESISTS.add( Yog.BurningFist.class );
	}*/
	public static final HashSet<EffectType> RESISTS = new HashSet<>();
	static {
		RESISTS.add( new EffectType(0, EffectType.FIRE) );
		RESISTS.add( new EffectType(0, EffectType.ICE) );
		RESISTS.add( new EffectType(0, EffectType.ELETRIC) );
		RESISTS.add( new EffectType(0, EffectType.LIGHT) );
		RESISTS.add( new EffectType(0, EffectType.DARK) );
		RESISTS.add( new EffectType(0, EffectType.AIR) );
	}
	
	public static float resist( Char target, EffectType effectType ){
		if (getBonus(target, Resistance.class) == 0) return 1f;
		/*
		for (Class c : RESISTS){
			if(c.isAssignableFrom(arcClass) && Buff.class.isAssignableFrom(arcClass)){
				return resistMultiplier(target, true);
			}else if (c.isAssignableFrom(arcClass)){
				return resistMultiplier(target, false);
			}
		}
		*/
		for (EffectType ef : RESISTS){
			if(EffectType.isExistType(effectType, ef)){
				boolean buff = Buff.class.isAssignableFrom(effectType.attachClass) || effectType.isExistAttachType(EffectType.BUFF);
				return resistMultiplier(target, buff);
			}
		}
		
		return 1f;
	}

	public static float resistMultiplier( Char target, boolean buff ){
		if (buff && getBonus(target, Resistance.class) > 5){
			return 0;
		}else if(getBonus(target, Resistance.class) > 5)
			return 0.5f;
		else return (float)Math.pow(0.9, getBonus(target, Resistance.class));
	}
	
	public class Resistance extends RingBuff {
	
	}
}
