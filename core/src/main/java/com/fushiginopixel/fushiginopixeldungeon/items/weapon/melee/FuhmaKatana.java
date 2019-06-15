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

import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.FirstStrike;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

import java.util.ArrayList;

public class FuhmaKatana extends MeleeWeapon {
	
	{
		image = ItemSpriteSheet.FUHMA_KATANA;

		tier = 4;
		LIMIT = 3;
		ACC = 1.25f;

		properties = new ArrayList<Enchantment>(){
			{
					add(new FirstStrike());
			}
		};
	}

	@Override
	public int min(int lvl) {
		return  10 +
				lvl;
	}

	@Override
	public int max(int lvl) {
		return  35 +
				lvl*UPGRADE_ATTACK;
	}

}
