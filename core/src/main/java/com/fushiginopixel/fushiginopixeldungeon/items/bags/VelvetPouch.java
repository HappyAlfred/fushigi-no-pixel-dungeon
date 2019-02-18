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

package com.fushiginopixel.fushiginopixeldungeon.items.bags;

import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.stones.Runestone;
import com.fushiginopixel.fushiginopixeldungeon.plants.Plant;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

public class VelvetPouch extends Bag {

	{
		image = ItemSpriteSheet.POUCH;
		
		size = 20;
	}
	
	@Override
	public boolean grab( Item item ) {
		return item instanceof Plant.Seed || item instanceof Runestone;
	}
	
	@Override
	public int price() {
		return 30;
	}

}
