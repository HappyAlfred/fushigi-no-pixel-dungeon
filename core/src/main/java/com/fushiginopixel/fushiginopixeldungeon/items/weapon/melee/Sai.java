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

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

public class Sai extends MeleeWeapon {

	{
		image = ItemSpriteSheet.SAI;

		tier = 3;
		DLY = 0.5f; //2x speed
		LIMIT = 3;
	}

	@Override
	public int min(int lvl) {
		return  6 +
				lvl;
	}

	@Override
	public int max(int lvl) {
		return  12 +
				lvl*(UPGRADE_ATTACK-1);
	}

	@Override
	public int defenseFactor( Char owner ) {
		return 3;	//3 extra defence
	}
}
