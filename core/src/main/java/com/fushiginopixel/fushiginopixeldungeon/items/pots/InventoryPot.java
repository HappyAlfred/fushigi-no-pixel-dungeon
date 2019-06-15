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

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.EquipableItem;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.ItemStatusHandler;
import com.fushiginopixel.fushiginopixeldungeon.journal.Catalog;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndOptions;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndPotTab;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public abstract class InventoryPot extends Pot {

	protected String inventoryTitle = Messages.get(this, "inv_title");
	protected WndBag.Mode mode = WndBag.Mode.ALL;

	@Override
	public void doAdd() {

		if(items.size() >= size){
			GLog.w( Messages.get(this, "full") );
		}else GameScene.selectItem( itemSelector, mode, inventoryTitle );
	}

	public boolean canInput(Item item){
		return !isFull() && !(item instanceof Pot);
	}

	public void input(Item item){
		if(canInput(item)) {
			items.add(item);
			onItemSelected(item);
			updateQuickslot();
			Sample.INSTANCE.play(Assets.SND_BEACON);
		}
	}

	public abstract void onItemSelected( Item item );

	protected static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(final Item item ) {

			//FIXME this safety check shouldn't be necessary
			//it would be better to eliminate the curItem static variable.
			if (!(curItem instanceof InventoryPot)){
				return;
			}

			if (item != null) {

				if(item instanceof Pot){
					GLog.w( Messages.get(InventoryPot.class, "cant_add") );
					return;
				}else{

					GameScene.show( new WndOptions( Messages.titleCase(curItem.name()), Messages.get(InventoryPot.class, "add_in"),
							Messages.get(InventoryPot.class, "one"), Messages.get(InventoryPot.class, "all") ) {
						@Override
						protected void onSelect( int index ) {
							Item oldItem = item;
							if (oldItem.isEquipped(curUser)) {
								if (!((EquipableItem) oldItem).doUnequip(curUser, false))
									return;
								((InventoryPot) curItem).items.add(oldItem);
							} else {
								switch (index) {
									case 0:
										oldItem = item.detach((curUser).belongings.backpack);
										((InventoryPot) curItem).items.add(oldItem);
										break;
									case 1:
										oldItem = item.detachAll((curUser).belongings.backpack);
										((InventoryPot) curItem).items.add(oldItem);
										break;
								}
							}
							((InventoryPot) curItem).onItemSelected(oldItem);
							((InventoryPot) curItem).addAnimation();
							curItem.updateQuickslot();
							Sample.INSTANCE.play(Assets.SND_BEACON);
						}
					} );
				}
			}
		}
	};
}
