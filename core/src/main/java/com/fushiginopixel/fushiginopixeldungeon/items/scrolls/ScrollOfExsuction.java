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

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;

public class ScrollOfExsuction extends InventoryScroll {
	
	{
		initials = 14;
		mode = WndBag.Mode.POT;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

		if(!(item instanceof Pot)){
			GLog.i(Messages.get(this, "nothing"));
			return;
		}

		Pot p = (Pot) item;

		if(!p.isEmpty()){
			GLog.i(Messages.get(this, "suck_out"));
			for (Item potItem : p.items.toArray(new Item[0])) {
				Dungeon.level.drop( potItem.detachAll( curUser.belongings.backpack ), curUser.pos ).sprite.drop( curUser.pos );
			}
			p.updateQuickslot();
		}else {
			GLog.i(Messages.get(this, "empty"));
		}
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
