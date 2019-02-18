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

package com.fushiginopixel.fushiginopixeldungeon;

import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Gold;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.Artifact;
import com.fushiginopixel.fushiginopixeldungeon.items.pots.Pot;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.FileUtils;
import com.watabou.utils.Random;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import static com.fushiginopixel.fushiginopixeldungeon.Statistics.amuletObtained;

public class Warehouse {

	private static final String WAREHOUSE_FILE	= "warehouse.dat";

	private static final String ITEMS	= "items";
	public static ArrayList<Item> items = new ArrayList<>();
	
	public static void save(Item item) {

		items = getBundle();

		if(items != null) {
			items.add(item);
		}else{
			items = new ArrayList<>();
			items.add(item);
		}

		Bundle bundle = new Bundle();
		bundle.put( ITEMS, items );

		try {
			FileUtils.bundleToFile( WAREHOUSE_FILE, bundle );
		} catch (IOException e) {
			Fushiginopixeldungeon.reportException(e);
		}
	}

	public static Item remove(Item item) {

		items = getBundle();

		if(items == null){
			items = new ArrayList<>();
		}

		if(items != null && items.contains(item)){
			items.remove(item);
		}

		Bundle bundle = new Bundle();
		bundle.put( ITEMS, items );

		try {
			FileUtils.bundleToFile( WAREHOUSE_FILE, bundle );
			return item;
		} catch (IOException e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}
	}

	public static ArrayList<Item> getBundle() {
	    /*
	    if items is empty then load file
	    else return items
	     */
        if (!items.isEmpty()){
            return items;
        }
		try {
			Bundle bundle = FileUtils.bundleFromFile(WAREHOUSE_FILE);
			for (Bundlable bundleitem : bundle.getCollection( ITEMS )) {
				if (bundleitem != null) items.add((Item)bundleitem);
			};
			return items;
		} catch (IOException e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}
	}
}
