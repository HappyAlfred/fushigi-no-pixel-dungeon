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

package com.fushiginopixel.fushiginopixeldungeon.windows;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.Warehouse;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.GodownKeeper;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Wandmaker;
import com.fushiginopixel.fushiginopixeldungeon.items.EquipableItem;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.Artifact;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.CorpseDust;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.Embers;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.plants.Rotberry;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.PixelScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.RedButton;
import com.fushiginopixel.fushiginopixeldungeon.ui.RenderedTextMultiline;
import com.fushiginopixel.fushiginopixeldungeon.ui.Window;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;

import java.util.ArrayList;

public class WndGodownKeeper extends Window {

	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	private static final int WAREHOUSE_LIMIT		= 20;

	public WndGodownKeeper(final GodownKeeper godownKeeper) {
		
		super();
		this.godownKeeper = godownKeeper;
		IconTitle titlebar = new IconTitle();
		titlebar.icon(godownKeeper.sprite());
		titlebar.label(Messages.titleCase(godownKeeper.name));
		titlebar.setRect(0, 0, WIDTH, 0);
		add( titlebar );

		ArrayList<Item> items = Warehouse.getBundle();
		if(items == null){
			items = new ArrayList<>();
		}

		for(Item item :items){
			item.reset();
		}

		String msg = Messages.get(godownKeeper, "rule" , items.size() , WAREHOUSE_LIMIT);

		RenderedTextMultiline message = PixelScene.renderMultiline( msg, 6 );
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add( message );

		RedButton btnSave = new RedButton( Messages.get(this, "save_item" , GodownKeeper.cost) ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.selectItem( itemSelector, WndBag.Mode.ALL, Messages.get(WndGodownKeeper.this, "save") );
			}
		};
		btnSave.setRect(0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT);
		btnSave.enable( GodownKeeper.cost <= Dungeon.gold && items.size() < WAREHOUSE_LIMIT );
		add( btnSave );

		RedButton btnGet = new RedButton( Messages.get(this, "get_item" ) ) {
			@Override
			protected void onClick() {
				hide();
				GameScene.show( new WndWarehouse( itemSelector1,WndWarehouse.Mode.ALL, Messages.get(WndGodownKeeper.this, "get_item")) );
			}
		};
		btnGet.setRect(0, btnSave.bottom() + GAP, WIDTH, BTN_HEIGHT);
		btnGet.enable( items.size() > 0 && (Dungeon.mode.enabledGotFromWarehouse() || Statistics.overed) );
		add( btnGet );
		
		resize(WIDTH, (int) btnGet.bottom());
	}

	protected static GodownKeeper godownKeeper = null;

	protected static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(final Item item) {
			if(item != null) {
				if(item.bones || (item.classlimit != null && item.classlimit == Dungeon.hero.heroClass)){

				}else {
					if(godownKeeper != null){
						GameScene.show(new WndQuest(godownKeeper,Messages.get(godownKeeper,"cant_save")));
					}
					return;
				}

				if(item instanceof Pot && ((Pot)item).items.size() > 0){
					if(godownKeeper != null){
						GameScene.show(new WndQuest(godownKeeper,Messages.get(godownKeeper,"cant_save")));
					}
					return;
				}

				if(item instanceof Armor && ((Armor)item).checkSeal()!= null){
					if(godownKeeper != null){
						GameScene.show(new WndQuest(godownKeeper,Messages.get(godownKeeper,"cant_save")));
					}
					return;
				}

				if(item instanceof Artifact){
					if(godownKeeper != null){
						GameScene.show(new WndQuest(godownKeeper,Messages.get(godownKeeper,"cant_save")));
					}
					return;
				}

				Item oldItem = item;
				if (oldItem.isEquipped(Dungeon.hero)) {
					if (!((EquipableItem) oldItem).doUnequip(Dungeon.hero, false))
						return;
					Dungeon.gold -= GodownKeeper.cost;
					Warehouse.save(oldItem);
					return;
				}
				Dungeon.gold -= GodownKeeper.cost;
				Warehouse.save(oldItem.detach(Dungeon.hero.belongings.backpack));
			}

		}
	};

	protected static WndWarehouse.Listener itemSelector1 = new WndWarehouse.Listener() {
		@Override
		public void onSelect(final Item item) {
			if(item != null) {
				if(item.collect()){
					Warehouse.remove(item);
				}
			}

		}
	};
}
