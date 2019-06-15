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

import com.fushiginopixel.fushiginopixeldungeon.items.armor.properties.AbsorbWater;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.properties.Combustable;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class CaneArmor extends Armor {

	{
		image = ItemSpriteSheet.ARMOR_CANE;

		LIMIT = 2;
		tier = 3;

		properties = new ArrayList<Glyph>(){
			{
				add(new Combustable());
			}
		};
	}

	@Override
	public int DRMax(int lvl){
		return  33 +
				lvl*UPGRADE_DEFENSE;
	}

	@Override
	public int DRMin(int lvl){
		return 5 + lvl;
	}

}
