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

import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ShadowParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;

public class ScrollOfExpand extends InventoryScroll {
	
	{
		initials = 12;
		mode = WndBag.Mode.POT;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

		if(!(item instanceof Pot)){
			GLog.i(Messages.get(this, "nothing"));
			return;
		}

		//logic for telling the user when item properties change from upgrades
		//...yes this is rather messy
		Pot p = (Pot) item;

		if(p.isExpandable()){
			GLog.i(Messages.get(this, "expand"));
			upgrade( curUser );
			p.expand();
		}else {
			GLog.i(Messages.get(this, "not_expand"));
		}
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
