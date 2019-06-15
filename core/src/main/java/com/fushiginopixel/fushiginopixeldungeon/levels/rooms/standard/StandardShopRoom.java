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

package com.fushiginopixel.fushiginopixeldungeon.levels.rooms.standard;

import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Shop;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.fushiginopixel.fushiginopixeldungeon.items.Ankh;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Gold;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.MerchantsBeacon;
import com.fushiginopixel.fushiginopixeldungeon.items.food.SpecialOnigiri;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfBeverage;
import com.fushiginopixel.fushiginopixeldungeon.items.stones.StoneOfAugmentation;
import com.fushiginopixel.fushiginopixeldungeon.items.stones.StoneOfEnchantment;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.painters.Painter;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.ShopInterface;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.secret.SecretRoom;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class StandardShopRoom extends StandardRoom implements ShopInterface {

	private ArrayList<Item> itemsToSpawn;
	public int shopKeeperID = 0;
	public Shopkeeper shopKeeper = null;

	private static final String NODE	= "shopKeeper";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		/*
		if(shopKeeper != null){
			shopKeeperID = shopKeeper.id();
		}
		*/
		getShopkeeper();
		bundle.put(NODE, shopKeeperID);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		shopKeeperID = bundle.getInt(NODE);
		/*
		if (shopKeeper == null && shopKeeperID != 0) {
			Actor a = Actor.findById(shopKeeperID);
			if (a != null) {
				shopKeeper = (Shopkeeper) a;
			} else {
				shopKeeperID = 0;
			}
		}
		*/
	}

	@Override
	public int minWidth() {
		if (itemsToSpawn == null) itemsToSpawn = generateItems();
		return Math.max(7, (int)(Math.sqrt(itemsToSpawn.size())+3));
	}

	@Override
	public int minHeight() {
		if (itemsToSpawn == null) itemsToSpawn = generateItems();
		return Math.max(7, (int)(Math.sqrt(itemsToSpawn.size())+3));
	}

	@Override
	public void paint(Level level) {
		super.paint(level);
		
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY_SP);

		placeShopkeeper( level );

		placeItems( level );

		Shop shopping = (Shop)level.blobs.get( Shop.class );
		if (shopping == null) {
			shopping = new Shop();
		}
		for (int i=top + 1; i < bottom; i++) {
			for (int j=left + 1; j < right; j++) {
				shopping.seed( level, j + level.width() * i, 1 );
			}
		}
		level.blobs.put( Shop.class, shopping );
	}

	protected void placeShopkeeper( Level level ) {

		int pos = level.pointToCell(center());

		shopKeeper = new Shopkeeper();
		shopKeeper.pos = pos;
		level.mobs.add( shopKeeper );

	}

	public Door entrance() {
		return connected.values().iterator().next();
	}

	protected void placeItems( Level level ){

		if (itemsToSpawn == null)
			itemsToSpawn = generateItems();

		Point itemPlacement = new Point(entrance());
		if (itemPlacement.y == top){
			itemPlacement.y++;
		} else if (itemPlacement.y == bottom) {
			itemPlacement.y--;
		} else if (itemPlacement.x == left){
			itemPlacement.x++;
		} else {
			itemPlacement.x--;
		}

		for (Item item : itemsToSpawn) {

			if (itemPlacement.x == left+1 && itemPlacement.y != top+1){
				itemPlacement.y--;
			} else if (itemPlacement.y == top+1 && itemPlacement.x != right-1){
				itemPlacement.x++;
			} else if (itemPlacement.x == right-1 && itemPlacement.y != bottom-1){
				itemPlacement.y++;
			} else {
				itemPlacement.x--;
			}

			int cell = level.pointToCell(itemPlacement);

			if (level.heaps.get( cell ) != null) {
				do {
					cell = level.pointToCell(random());
				} while (level.heaps.get( cell ) != null || level.findMob( cell ) == shopKeeper);
			}

			level.drop( item, cell ).type = Heap.Type.FOR_SALE;
		}

	}

	protected static ArrayList<Item> generateItems(Generator.Category cat) {
		ArrayList<Item> itemsToSpawn = new ArrayList<>();
		for (int i = 0; i < 9; i++)
			itemsToSpawn.add(Generator.random(cat));
		Random.shuffle(itemsToSpawn);
		return itemsToSpawn;
	}

	protected static ArrayList<Item> generateItems() {

		if(Random.Float(1) < 0.2){
			return generateItems( Generator.randomCategory() );
		}

		ArrayList<Item> itemsToSpawn = new ArrayList<>();
		for (int i=0; i < 8; i++) {
			Item item = Generator.random();
			if(item instanceof Gold){
				i--;
				continue;
			}
			itemsToSpawn.add(item);
		}

		itemsToSpawn.add(new Ankh());
		Random.shuffle(itemsToSpawn);
		return itemsToSpawn;
	}

	@Override
	public Shopkeeper getShopkeeper() {
		if(shopKeeperID != 0 && shopKeeper == null){
			Actor a = Actor.findById(shopKeeperID);
			if(a != null){
				shopKeeper = (Shopkeeper)a;
				shopKeeperID = a.id();
			}else{
				shopKeeperID = 0;
			}
		}
		return shopKeeper;
	}
}