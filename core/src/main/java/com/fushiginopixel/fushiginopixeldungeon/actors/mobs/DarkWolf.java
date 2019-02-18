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

import com.fushiginopixel.fushiginopixeldungeon.Challenges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Amok;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bleeding;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.food.MysteryMeat;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.DarkWolfSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.PatrolDogSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class DarkWolf extends PatrolDog {

	{
		spriteClass = DarkWolfSprite.class;
		
		HP = HT = 200;
		defenseSkill = 34;
        EXP = 20;
	}
	public int range = 8;
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 37, 52 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 60;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(1, 18);
	}


}
