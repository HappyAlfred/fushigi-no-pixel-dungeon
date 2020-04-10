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
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.effects.Enchanting;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ShadowParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Boomerang;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;

public class ScrollOfEarthBless extends InventoryScroll {
	
	{
		initials = 2;
		mode = WndBag.Mode.ALL;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

		Item i = item;

		boolean canInfuse = false;
		boolean wasCursed = item.cursed;
		if (item instanceof Weapon) {
			Weapon wep = (Weapon)item;
			canInfuse = wep.canEnchant(null);
			if(!canInfuse && !wasCursed){
				GLog.i(Messages.get(this, "nothing"));
				return;
			}
			if(canInfuse) {
				((Weapon) item).enchant();

				GLog.p( Messages.get(this, "infuse", item.name()) );
				curUser.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.1f, 5 );
				Enchanting.show(curUser, item);
			}
		} else if(item instanceof Armor){
			Armor arm = (Armor)item;
			canInfuse = arm.canInscribe(null);
			if(!canInfuse && !wasCursed){
				GLog.i(Messages.get(this, "nothing"));
				return;
			}
			if(canInfuse) {
				((Armor) item).inscribe();

				GLog.p( Messages.get(this, "infuse", item.name()) );
				curUser.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.1f, 5 );
				Enchanting.show(curUser, item);
			}
		} else if (item instanceof Wand || item instanceof Ring) {
			canInfuse = item.isUpgradable();

			if(canInfuse) {
				item.upgrade();
				upgrade(curUser);
			}
			Badges.validateItemLevelAquired( item );

		} else{
			GLog.i(Messages.get(this, "nothing"));
			return;
		}

		item.cursed = false;
		if (wasCursed && !item.cursed){
			removeCurse( Dungeon.hero );
		}

		/*
		if (item instanceof Weapon || item instanceof Boomerang)
			((Weapon)item).upgrade(true);
		else if(item instanceof Armor)
			((Armor)item).upgrade(true);
		else{
			GLog.i(Messages.get(this, "nothing"));
			return;
		}
		
		GLog.p( Messages.get(this, "infuse", item.name()) );
		
		Badges.validateItemLevelAquired(item);

		curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
		Enchanting.show(curUser, item);
		*/
	}

	public static void upgrade( Char hero ) {
		hero.sprite.emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );
	}

	public static void removeCurse( Char hero ){
		GLog.p( Messages.get(ScrollOfSkyBless.class, "remove_curse") );
		hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
	}
	
	@Override
	public void empoweredRead() {
		//does nothing for now, this should never happen.
	}

	@Override
	public int price() {
		return isKnown() ? 100 * quantity : super.price();
	}
}
