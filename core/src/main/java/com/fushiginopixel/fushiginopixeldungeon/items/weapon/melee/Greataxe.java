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

import com.fushiginopixel.fushiginopixeldungeon.items.weapon.curses.Exhausting;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class Greataxe extends MeleeWeapon {

	{
		image = ItemSpriteSheet.GREATAXE;

		tier = 4;
		LIMIT = 2;
		properties = new ArrayList<Enchantment>(){
			{
				add(new Exhausting());
			}
		};
	}

	@Override
	public int max(int lvl) {
		return  60 +    //50 base, up from 30
				lvl*UPGRADE_ATTACK;   //scaling unchanged
	}

	/*@Override
	public int STRReq(int lvl) {
		lvl = Math.max(0, lvl);
		//20 base strength req, up from 18
		return (10 + tier * 2) - (int)(Math.sqrt(8 * lvl + 1) - 1)/2;
	}*/

}
