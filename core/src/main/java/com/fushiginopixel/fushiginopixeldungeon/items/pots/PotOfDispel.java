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

package com.fushiginopixel.fushiginopixeldungeon.items.pots;

import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;

public class PotOfDispel extends InventoryPot {

	{
		initials = 8;

		bones = true;
	}

	@Override
	public void onItemSelected( Item item ) {

		boolean procced = false;
		if(item.cursed) {
			item.cursed = false;
			procced = true;

		}
		if (item instanceof Weapon && ((Weapon) item).enchantment.size() > 0){
			Weapon w = (Weapon) item;
			w.enchantment.clear();
			w.cursed = false;
			procced = true;
			item = (Item)w;
		}
		if (item instanceof Armor && ((Armor) item).glyph.size() > 0){
			Armor a = (Armor) item;
			a.glyph.clear();
			a.cursed = false;
			procced = true;
			item = (Item)a;
		}
		if (item instanceof Bag){
			for (Item bagItem : ((Bag)item).items){
				if (bagItem != null && bagItem.cursed) {
					bagItem.cursed = false;
					procced = true;
				}
			}
		}
		if(procced){
			GLog.i(Messages.get(this, "dispel", item));
			knownByUse();
		}

	}

	@Override
	public int potPrice() {
		return super.potPrice() * 2;
	}
}
