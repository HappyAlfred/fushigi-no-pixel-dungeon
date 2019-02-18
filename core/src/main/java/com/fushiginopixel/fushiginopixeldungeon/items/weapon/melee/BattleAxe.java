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
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.Critical;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BattleAxe extends MeleeWeapon {

	{
		image = ItemSpriteSheet.BATTLE_AXE;

		tier = 4;
		//ACC = 1.24f; //24% boost to accuracy
		LIMIT = 2;
		properties = new ArrayList<Enchantment>(){
			{
				add(new Critical());
			}
		};

	}

	@Override
	public int max(int lvl) {
		return  32 +    //20 base, down from 25
				lvl*UPGRADE_ATTACK;   //scaling unchanged
	}

}
