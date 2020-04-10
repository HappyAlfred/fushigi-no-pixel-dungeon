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

import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectResistance;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

import java.text.DecimalFormat;
import java.util.HashSet;

public class Buff extends Actor {
	
	public Char target;

	{
		actPriority = BUFF_PRIO; //low priority, towards the end of a turn
	}

	//determines how the buff is announced when it is shown.
	//buffs that work behind the scenes, or have other visual indicators can usually be silent.
	public enum buffType {POSITIVE, NEGATIVE, NEUTRAL, SILENT};
	public buffType type = buffType.SILENT;
	
	protected HashSet<EffectResistance> resistances = new HashSet<>();
	
	public HashSet<EffectResistance> resistances() {
		return new HashSet<>(resistances);
	}

	/*
	protected HashSet<EffectType> immunities = new HashSet<>();
	
	public HashSet<EffectType> immunities() {
		return new HashSet<>(immunities);
	}
	*/

	public final boolean attachTo(Char target){
		return attachTo(target,new EffectType(0,0));
	}
	
	public boolean attachTo( Char target, EffectType type ) {

		if (target.isImmune( getClass(),type ) || target.resist(getClass(),type) <= 0) {
			return false;
		}
		
		this.target = target;
		target.add( this );

		if (target.buffs().contains(this)){
			if (target.sprite != null) fx( true );
			return true;
		} else
			return false;
	}
	
	public void detach() {
		if (target.sprite != null) fx( false );
		target.remove( this );
	}
	
	@Override
	public boolean act() {
		diactivate();
		return true;
	}
	
	public int icon() {
		return BuffIndicator.NONE;
	}
	
	public void tintIcon( Image icon ){
		//do nothing by default
	}

	public void fx(boolean on) {
		//do nothing by default
	};

	public String heroMessage(){
		return null;
	}

	public String desc(){
		return "";
	}

	//using to modify buff's effect strength
	public float modifyResist(float value, EffectType type) {
		if(target == null) return 0;
		else return value * target.resist(getClass(), type);
	};

	//to handle the common case of showing how many turns are remaining in a buff description.
	protected String dispTurns(float input){
		return new DecimalFormat("#.##").format(input);
	}

	//creates a fresh instance of the buff and attaches that, this allows duplication.
	public static<T extends Buff> T append( Char target, Class<T> buffClass, EffectType type ) {
		try {
			T buff = buffClass.newInstance();
			buff.attachTo( target , type );
			return buff;
		} catch (Exception e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}
	}

	public static<T extends Buff> T append( Char target, Class<T> buffClass ) {
		return append(target,buffClass,new EffectType(0,0));
	}

	public static<T extends FlavourBuff> T append( Char target, Class<T> buffClass, float duration,EffectType type ) {
		T buff = append( target, buffClass,type );
		buff.spend( Math.max(duration * target.resist(buffClass,type), 0) );
		return buff;
	}

	public static<T extends FlavourBuff> T append( Char target, Class<T> buffClass, float duration ) {
		return append(target,buffClass,duration,new EffectType(0,0));
	}

	//same as append, but prevents duplication.
    public static<T extends Buff> T affect( Char target, Class<T> buffClass,EffectType type ) {
        T buff = target.buff( buffClass );
        if (buff != null) {
            return buff;
        } else {
            return append( target, buffClass,type );
        }
    }

	public static<T extends Buff> T affect( Char target, Class<T> buffClass ) {
		return affect(target,buffClass,new EffectType(0,0));
	}

	public static<T extends FlavourBuff> T affect( Char target, Class<T> buffClass, float duration ) {
		return affect(target,buffClass,duration,new EffectType(0,0));
	}

    public static<T extends FlavourBuff> T affect( Char target, Class<T> buffClass, float duration, EffectType type ) {
        T buff = affect( target, buffClass,type );
        buff.spend( Math.max(duration * target.resist(buffClass,type), 0) );
        return buff;
    }

	//postpones an already active buff, or creates & attaches a new buff and delays that.
	public static<T extends FlavourBuff> T prolong( Char target, Class<T> buffClass, float duration ) {
		return prolong(target,buffClass,duration,new EffectType(0,0));
	}

	public static<T extends FlavourBuff> T prolong( Char target, Class<T> buffClass, float duration, EffectType type ) {
		T buff = affect( target, buffClass, type );
		buff.postpone( Math.max(duration * target.resist(buffClass,type), 0) );
		return buff;
	}
	
	public static void detach( Buff buff ) {
		if (buff != null) {
			buff.detach();
		}
	}
	
	public static void detach( Char target, Class<? extends Buff> cl ) {
		detach( target.buff( cl ) );
	}
}
