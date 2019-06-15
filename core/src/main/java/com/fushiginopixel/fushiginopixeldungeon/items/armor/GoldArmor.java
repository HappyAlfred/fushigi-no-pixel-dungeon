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

package com.fushiginopixel.fushiginopixeldungeon.items.armor;

import com.fushiginopixel.fushiginopixeldungeon.items.armor.properties.GildedArmor;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class GoldArmor extends Armor {

	{
		image = ItemSpriteSheet.ARMOR_GOLD;

		LIMIT = 5;
		tier = 2;
		properties = new ArrayList<Glyph>(){
			{
				add(new GildedArmor());
			}
		};
	}

	@Override
	public int DRMax(int lvl){
		return  10 +
				lvl*UPGRADE_DEFENSE;
	}

	@Override
	public int DRMin(int lvl){
		return 2 + lvl;
	}

	@Override
	public int price() {
		return super.price() * 2;
	}
}
