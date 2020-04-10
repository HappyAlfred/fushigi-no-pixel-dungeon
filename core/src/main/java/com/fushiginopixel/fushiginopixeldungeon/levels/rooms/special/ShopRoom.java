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

package com.fushiginopixel.fushiginopixeldungeon.levels.rooms.special;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Shop;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Belongings;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Shopkeeper;
import com.fushiginopixel.fushiginopixeldungeon.items.Ankh;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bomb;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Honeypot;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.MerchantsBeacon;
import com.fushiginopixel.fushiginopixeldungeon.items.Stylus;
import com.fushiginopixel.fushiginopixeldungeon.items.Torch;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.LeatherArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.MailArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.PlateArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.ScaleArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.TimekeepersHourglass;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.MagicalHolster;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.PotionBandolier;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.ScrollHolder;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.VelvetPouch;
import com.fushiginopixel.fushiginopixeldungeon.items.food.SpecialOnigiri;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfBeverage;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfHealing;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.Scroll;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfIdentify;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.fushiginopixel.fushiginopixeldungeon.items.stones.Runestone;
import com.fushiginopixel.fushiginopixeldungeon.items.stones.StoneOfAugmentation;
import com.fushiginopixel.fushiginopixeldungeon.items.stones.StoneOfEnchantment;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.BattleAxe;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Greatsword;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.HandAxe;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Longsword;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Mace;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Sword;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.WarHammer;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Bolas;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.FishingSpear;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Javelin;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.MissileWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Shuriken;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.ThrowingHammer;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.ThrowingSpear;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Tomahawk;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Trident;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.darts.TippedDart;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.painters.Painter;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.ShopInterface;
import com.fushiginopixel.fushiginopixeldungeon.plants.Plant;
import com.watabou.utils.Bundle;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class ShopRoom extends SpecialRoom implements ShopInterface {

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
	
	public void paint( Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY_SP );

		placeShopkeeper( level );

		placeItems( level );
		
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

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
	
	protected static ArrayList<Item> generateItems() {

		ArrayList<Item> itemsToSpawn = new ArrayList<>();
		
		switch (Dungeon.depth) {
		case 6:
			itemsToSpawn.add( (Random.Int( 2 ) == 0 ? new Sword().identify() : new HandAxe()).identify() );
			itemsToSpawn.add( Random.Int( 2 ) == 0 ?
					new FishingSpear().quantity(2) :
					new Shuriken().quantity(2));
			itemsToSpawn.add( new LeatherArmor().identify() );
			break;
			
		case 11:
			itemsToSpawn.add( (Random.Int( 2 ) == 0 ? new Longsword().identify() : new Mace()).identify() );
			itemsToSpawn.add( Random.Int( 2 ) == 0 ?
					new ThrowingSpear().quantity(2) :
					new Bolas().quantity(2));
			itemsToSpawn.add( new MailArmor().identify() );
			break;
			
		case 16:
			itemsToSpawn.add( (Random.Int( 2 ) == 0 ? new Greatsword().identify() : new BattleAxe()).identify() );
			itemsToSpawn.add( Random.Int( 2 ) == 0 ?
					new Javelin().quantity(2) :
					new Tomahawk().quantity(2));
			itemsToSpawn.add( new ScaleArmor().identify() );
			break;
			
		case 21:
			itemsToSpawn.add( Random.Int( 2 ) == 0 ? new Greatsword().identify() : new WarHammer().identify() );
			itemsToSpawn.add( Random.Int(2) == 0 ?
					new Trident().quantity(2) :
					new ThrowingHammer().quantity(2));
			itemsToSpawn.add( new PlateArmor().identify() );
			itemsToSpawn.add( new Torch() );
			itemsToSpawn.add( new Torch() );
			itemsToSpawn.add( new Torch() );
			break;
		}
		
		itemsToSpawn.add( TippedDart.randomTipped() );

		itemsToSpawn.add( new MerchantsBeacon() );


		itemsToSpawn.add(ChooseBag(Dungeon.hero.belongings));


		itemsToSpawn.add( new PotionOfHealing() );
		itemsToSpawn.add( new PotionOfBeverage() );
		for (int i=0; i < 3; i++)
			itemsToSpawn.add( Generator.random( Generator.Category.POTION ) );

		itemsToSpawn.add( new ScrollOfIdentify() );
		itemsToSpawn.add( new ScrollOfRemoveCurse() );
		itemsToSpawn.add( new ScrollOfMagicMapping() );
		itemsToSpawn.add( Generator.random( Generator.Category.SCROLL ) );

		for (int i=0; i < 2; i++) {
			int flag = Random.Int(4);
			switch (flag){
				case 0:itemsToSpawn.add(Generator.random(Generator.Category.POTION));break;
				case 1:itemsToSpawn.add(Generator.random(Generator.Category.SCROLL));break;
				case 2:itemsToSpawn.add(Generator.random(Generator.Category.WAND));break;
				case 3:itemsToSpawn.add(Generator.random(Generator.Category.POT));break;
			}
		}

		itemsToSpawn.add( Generator.random( Generator.Category.POT ) );
		itemsToSpawn.add( new SpecialOnigiri() );
		itemsToSpawn.add( new SpecialOnigiri() );

		itemsToSpawn.add( new Bomb().random() );
		switch (Random.Int(5)){
			case 1:
				itemsToSpawn.add( Generator.random(Generator.Category.BOMBS) );
				break;
			case 2:
				itemsToSpawn.add( Generator.random(Generator.Category.POT) );
				break;
			case 3:
			case 4:
				itemsToSpawn.add( new Honeypot() );
				break;
		}

		itemsToSpawn.add( new Ankh() );
		//itemsToSpawn.add( Random.Int(2) == 0 ? new StoneOfAugmentation() : new StoneOfEnchantment());

		TimekeepersHourglass hourglass = Dungeon.hero.belongings.getItem(TimekeepersHourglass.class);
		if (hourglass != null){
			int bags = 0;
			//creates the given float percent of the remaining bags to be dropped.
			//this way players who get the hourglass late can still max it, usually.
			switch (Dungeon.depth) {
				case 11:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.20f ); break;
				case 21:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.25f ); break;
				case 31:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.50f ); break;
				case 42:
					bags = (int)Math.ceil(( 5-hourglass.sandBags) * 0.80f ); break;
			}

			for(int i = 1; i <= bags; i++){
				itemsToSpawn.add( new TimekeepersHourglass.sandBag());
				hourglass.sandBags ++;
			}
		}

		Item rare;
		switch (Random.Int(10)){
			case 0:
				rare = Generator.random( Generator.Category.WAND );
				rare.level( 0 );
				break;
			case 1:
				rare = Generator.random(Generator.Category.RING);
				rare.level( 0 );
				break;
			case 2:
				rare = Generator.random( Generator.Category.ARTIFACT );
				break;
			default:
				rare = new Stylus();
		}
		rare.cursed = rare.cursedKnown = false;
		itemsToSpawn.add( rare );

		//hard limit is 63 items + 1 shopkeeper, as shops can't be bigger than 8x8=64 internally
		if (itemsToSpawn.size() > 63)
			throw new RuntimeException("Shop attempted to carry more than 63 items!");

		Random.shuffle(itemsToSpawn);
		return itemsToSpawn;
	}

	protected static Bag ChooseBag(Belongings pack){
	
		//0=pouch, 1=holder, 2=bandolier, 3=holster
		int[] bagItems = new int[4];

		//count up items in the main bag
		for (Item item : pack.backpack.items) {
			if (item instanceof Plant.Seed || item instanceof Runestone)    bagItems[0]++;
			if (item instanceof Scroll)                                     bagItems[1]++;
			if (item instanceof Potion)                                     bagItems[2]++;
			if (item instanceof Wand || item instanceof MissileWeapon)      bagItems[3]++;
		}
		
		//disqualify bags that have already been dropped
		if (Dungeon.LimitedDrops.VELVET_POUCH.dropped())                    bagItems[0] = -1;
		if (Dungeon.LimitedDrops.SCROLL_HOLDER.dropped())                   bagItems[1] = -1;
		if (Dungeon.LimitedDrops.POTION_BANDOLIER.dropped())                bagItems[2] = -1;
		if (Dungeon.LimitedDrops.MAGICAL_HOLSTER.dropped())                 bagItems[3] = -1;
		
		//find the best bag to drop. This does give a preference to later bags, if counts are equal
		int bestBagIdx = 0;
		for (int i = 1; i <= 3; i++){
			if (bagItems[bestBagIdx] <= bagItems[i]){
				bestBagIdx = i;
			}
		}
		
		//drop it, or return nothing if no bag works
		if (bagItems[bestBagIdx] == -1) return null;
		switch (bestBagIdx){
			case 0: default:
				Dungeon.LimitedDrops.VELVET_POUCH.drop();
				return new VelvetPouch();
			case 1:
				Dungeon.LimitedDrops.SCROLL_HOLDER.drop();
				return new ScrollHolder();
			case 2:
				Dungeon.LimitedDrops.POTION_BANDOLIER.drop();
				return new PotionBandolier();
			case 3:
				Dungeon.LimitedDrops.MAGICAL_HOLSTER.drop();
				return new MagicalHolster();
		}

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
