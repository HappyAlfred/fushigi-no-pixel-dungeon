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

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.food.SlimyGel;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.InventoryPot;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GelCubeSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.RedGelCubeSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class RedGelCube extends GelCube {
	{
		spriteClass = RedGelCubeSprite.class;
		
		HP = HT = 100;
		defenseSkill = 25;
		
		EXP = 19;
	}

	public int zapSkill() {
		return 2;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 15, 47 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 52;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(1, 15);
	}
}
