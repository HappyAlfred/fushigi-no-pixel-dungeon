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

public class Flail extends MeleeWeapon {

	{
		image = ItemSpriteSheet.FLAIL;

		tier = 3;
		ACC = 0.9f; //0.9x accuracy
		//also cannot surprise attack, see Hero.canSurpriseAttack
		LIMIT = 3;
	}

	@Override
	public int max(int lvl) {
		return  35 +        //35 base, up from 25
				lvl*(UPGRADE_ATTACK+1);  //+8 per level, up from +5
	}
}
