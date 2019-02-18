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

package com.fushiginopixel.fushiginopixeldungeon.items.scrolls;

import com.fushiginopixel.fushiginopixeldungeon.Challenges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.EquipableItem;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.food.BigOnigiri;
import com.fushiginopixel.fushiginopixeldungeon.items.food.Food;
import com.fushiginopixel.fushiginopixeldungeon.items.food.SpecialOnigiri;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;
import com.watabou.utils.Random;

public class ScrollOfOnigiri extends InventoryScroll {
	
	{
		initials = 13;
		mode = WndBag.Mode.ALL;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

		if(item.isUnique()){
			GLog.i(Messages.get(this, "nothing"));
			return;
		}

		if(item.cursed){
			item.cursed = false;
		}
		if (item.isEquipped(Dungeon.hero)) {
			if (!((EquipableItem) item).doUnequip(Dungeon.hero, false))
				return;
		}
		item.detachAll((curUser).belongings.backpack);

		Item onigiri = new Item();
		if(Random.Int(100) < 12){
			onigiri = new BigOnigiri();
		}else{
			onigiri = new Food();
		}
		if (!Challenges.isItemBlocked(onigiri) && onigiri instanceof Food) {
			onigiri.collect();
		}else{
			onigiri = new SpecialOnigiri();
			onigiri.collect();
		}
		GLog.i(Messages.get(this, "trans_to", onigiri));
	}
	
	public static void upgrade( Hero hero ) {
		hero.sprite.emitter().start( Speck.factory( Speck.KIT ), 0.2f, 10 );
	}
	
	@Override
	public void empoweredRead() {
		//does nothing for now, this should never happen.
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
