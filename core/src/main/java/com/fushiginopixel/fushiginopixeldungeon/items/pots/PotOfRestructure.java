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

import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.WaterOfTransmutation;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;

public class PotOfRestructure extends InventoryPot {

	{
		initials = 2;

		bones = true;
	}

	@Override
	public void onItemSelected( Item item ) {
		if(item.isUnique()){
			return;
		}

		Item newItem = Generator.random();
		for (int i = 0 ;newItem instanceof Pot;i++){
			if(i == 9){
				newItem = Generator.random(Generator.Category.GOLD);
				break;
			}else newItem = Generator.random();
		}
		if(newItem != null) {
			items.remove(item);
			items.add(newItem);
			GLog.i(Messages.get(this, "trans_to", newItem));
		}

	}

	@Override
	public int price() {
		return super.price() * 3;
	}
}
