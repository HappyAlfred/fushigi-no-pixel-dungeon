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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee;

import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

public class Sword extends MeleeWeapon {
	
	{
		image = ItemSpriteSheet.SWORD;

		tier = 1;
		LIMIT = 4;
	}

	@Override
	public int min(int lvl) {
		return  4 +
				lvl;
	}

	@Override
	public int max(int lvl) {
		return  16 +    //12 base, down from 15
				lvl*UPGRADE_ATTACK;   //scaling unchanged
	}

	@Override
	public int price() {
		int price = super.price();
		price *=1.5;
		return  price;
	}

}
