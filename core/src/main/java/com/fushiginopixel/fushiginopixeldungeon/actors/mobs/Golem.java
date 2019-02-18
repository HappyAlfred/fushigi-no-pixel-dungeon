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

package com.fushiginopixel.fushiginopixeldungeon.actors.mobs;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Amok;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Sleep;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Imp;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GolemSprite;
import com.watabou.utils.Random;

public class Golem extends Mob {
	
	{
		spriteClass = GolemSprite.class;
		
		HP = HT = 180;
		defenseSkill = 29;
		
		EXP = 19;
		
		properties.add(Property.INORGANIC);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 25, 100 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 55;
	}
	
	@Override
	protected float attackDelay() {
		return 1.5f;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(4, 30);
	}
	
	@Override
	public void rollToDropLoot() {
		Imp.Quest.process( this );
		
		super.rollToDropLoot();
	}
	//golem had spirit so it can't immunity spirit effect
	/*{
		immunities.add( Amok.class );
		immunities.add( Terror.class );
		immunities.add( Sleep.class );
	}*/
}
