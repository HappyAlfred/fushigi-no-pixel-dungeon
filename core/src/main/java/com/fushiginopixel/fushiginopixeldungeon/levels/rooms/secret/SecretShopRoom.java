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

package com.fushiginopixel.fushiginopixeldungeon.levels.rooms.secret;

import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Shop;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.fushiginopixel.fushiginopixeldungeon.items.Ankh;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
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
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SecretShopRoom extends SecretRoom implements ShopInterface {

	private ArrayList<Item> itemsToSpawn;
	public int shopKeeperID = 0;

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
		entrance().set(Door.Type.REGULAR);

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

		Shopkeeper shopKeeper = new Shopkeeper();
		shopKeeper.pos = pos;
		level.mobs.add( shopKeeper );
		shopKeeperID = shopKeeper.id();

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
				} while (level.heaps.get( cell ) != null || level.findMob( cell ) != null);
			}

			level.drop( item, cell ).type = Heap.Type.FOR_SALE;
		}

	}

	protected static Item upgradeGenerate(Generator.Category category){

		Item item = Generator.random(category);
		item.level(item.level()+ 1);
		return  item;
	}


	protected static ArrayList<Item> generateItems() {

		ArrayList<Item> itemsToSpawn = new ArrayList<>();
		if(Random.Int(2) == 1) {
			for (int i = 0; i < Random.Int(3); i++)
				itemsToSpawn.add(upgradeGenerate(Generator.Category.WEAPON));
			for (int i = 0; i < Random.Int(3); i++)
				itemsToSpawn.add(upgradeGenerate(Generator.Category.ARMOR));
			for (int i = 0; i < Random.Int(3); i++)
				itemsToSpawn.add(upgradeGenerate(Generator.Category.WAND));
			for (int i = 0; i < Random.Int(3); i++)
				itemsToSpawn.add(upgradeGenerate(Generator.Category.RING));
		}
		else{
			for (int i = 0; i < Random.Int(3); i++)
				itemsToSpawn.add(upgradeGenerate(Generator.Category.WEAPON).identify());
			for (int i = 0; i < Random.Int(3); i++)
				itemsToSpawn.add(upgradeGenerate(Generator.Category.ARMOR).identify());
			for (int i = 0; i < Random.Int(3); i++)
				itemsToSpawn.add(upgradeGenerate(Generator.Category.WAND).identify());
			for (int i = 0; i < Random.Int(3); i++)
				itemsToSpawn.add(upgradeGenerate(Generator.Category.RING).identify());
		}

		for (int i=0; i < Random.Int(3); i++)
			itemsToSpawn.add( Generator.random( Generator.Category.BOMBS ) );

		for (int i=0; i < Random.Int(3) ; i++)
			itemsToSpawn.add( new PotionOfBeverage() );

		itemsToSpawn.add( Generator.random( Generator.Category.POT ) );
		itemsToSpawn.add(new MerchantsBeacon());

		itemsToSpawn.add(new SpecialOnigiri());

		itemsToSpawn.add(new Ankh());
		itemsToSpawn.add( Random.Int(2) == 0 ? new StoneOfAugmentation() : new StoneOfEnchantment());
		Random.shuffle(itemsToSpawn);
		return itemsToSpawn;
	}

	@Override
	public Shopkeeper getShopkeeper() {
		Shopkeeper shopKeeper = null;
		if(shopKeeperID != 0){
			Actor a = Actor.findById(shopKeeperID);
			if(a != null){
				shopKeeper = (Shopkeeper)a;
				shopKeeperID = shopKeeper.id();
			}else{
				shopKeeperID = 0;
			}
		}
		return shopKeeper;
	}
}
