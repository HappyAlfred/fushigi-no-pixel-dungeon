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

package com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Shop;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.ShopGuardian;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Pushing;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ElmoParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.levels.RegularLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.Room;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ShopkeeperSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.audio.Music;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Shopkeeper extends NPC {

	{
		spriteClass = ShopkeeperSprite.class;

		properties.add(Property.IMMOVABLE);
	}
	
	@Override
	protected boolean act() {

		throwItem();
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		spend( TICK );
		return true;
	}
	
	@Override
	public int damage( int dmg, Object src ,EffectType type) {
		flee();
		return 0;
	}
	
	@Override
	public void add( Buff buff ) {
		flee();
	}
	
	public void flee() {
		if (Dungeon.level instanceof RegularLevel && (((RegularLevel)Dungeon.level).room(pos) != null)){
			Room r = ((RegularLevel) Dungeon.level).room(pos);
			int cell;

			for (Point p : r.getPoints()){
				cell = Dungeon.level.pointToCell(p);

				if(Dungeon.level.blobs.containsKey(Shop.class)) {
					Shop shop = (Shop) Dungeon.level.blobs.get(Shop.class);
					if (shop != null)
						shop.clear(cell);
				}

				Heap heap = Dungeon.level.heaps.get( cell );
				if(heap != null && heap.type == Heap.Type.FOR_SALE)
					heap.type = Heap.Type.HEAP;
			}
		} else {
			Heap[] heaps = new Heap[Dungeon.level.heaps.values().size()];
			int j = 0;

			for (Heap heap : Dungeon.level.heaps.values()) {
				if (heap.type == Heap.Type.FOR_SALE) {
					heaps[j] = heap;
					j++;
				}
			}
			for(j = 0;j < heaps.length;j++){
				if(heaps[j] == null) break;
				heaps[j].type = Heap.Type.HEAP;
			}
			/*for (Heap heap : Dungeon.level.heaps.values()) {
				if (heap.type == Heap.Type.FOR_SALE) {
					heap.type = Heap.Type.HEAP;
				}
			}*/
		}
		Mob[] shopKeeper = new Mob[Dungeon.level.mobs.size()];
		int j = 0;

		for (Mob mob : Dungeon.level.mobs) {
			if(mob instanceof Shopkeeper && mob != this) {
				shopKeeper[j] = mob;
				j++;
			}

			mob.beckon( pos );
		}
		for(j = 0;j < shopKeeper.length;j++){
			if(shopKeeper[j] == null) break;
			((Shopkeeper)shopKeeper[j]).escape();
		}

		if (Dungeon.level.heroFOV[pos]) {
			GLog.w( Messages.get(this, "alarm") );
			CellEmitter.center(pos).start( Speck.factory(Speck.SCREAM), 0.3f, 3 );
		}

		Sample.INSTANCE.play( Assets.SND_ALERT );

		for (int i = 0; i <  7 + (Dungeon.depth - 5) / 5 * 2; i++){
			ShopGuardian guardian = ShopGuardian.summonGuardian();
			guardian.pos = Dungeon.level.randomRespawnCell();
			GameScene.add(guardian);
			guardian.beckon(pos );
		}

		Char ch = Actor.findChar( Dungeon.level.entrance );
		if (ch != null) {
			ArrayList<Integer> candidates = new ArrayList<>();
			for (int n : PathFinder.NEIGHBOURS8) {
				int cell = Dungeon.level.entrance  + n;
				if ((Dungeon.level.passable[cell] || Dungeon.level.avoid[cell]) && Actor.findChar(cell) == null) {
					candidates.add(cell);
				}
			}
			if (candidates.size() > 0) {
				int newPos = Random.element( candidates );
				Actor.addDelayed( new Pushing( ch, ch.pos, newPos ), -1 );

				ch.pos = newPos;
				Dungeon.level.press( newPos, ch );

			}
		}

		if (Actor.findChar( Dungeon.level.entrance ) != null) {
			ShopGuardian guardian = ShopGuardian.summonGuardian();
			guardian.pos = Dungeon.level.entrance;
			GameScene.add(guardian);
			guardian.beckon(pos);
		}
		alarm();

		destroy();
		
		sprite.killAndErase();
		CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
	}

	protected void alarm(){
		Statistics.thief = true;
		if(Dungeon.level instanceof RegularLevel) {
			((RegularLevel)Dungeon.level).refreshMobsToSpawn();
		}
		GameScene.playlevelmusic();
	}

	public void escape() {
		if (Dungeon.level instanceof RegularLevel && ((RegularLevel) Dungeon.level).room(pos) != null){
			Room r = ((RegularLevel) Dungeon.level).room(pos);
			int cell;
			for (Point p : r.getPoints()){
				cell = Dungeon.level.pointToCell(p);

				if(Dungeon.level.blobs.containsKey(Shop.class)) {
					Shop shop = (Shop) Dungeon.level.blobs.get(Shop.class);
					if (shop != null)
						shop.clear(cell);
				}

				Heap heap = Dungeon.level.heaps.get( cell );
				if(heap != null && heap.type == Heap.Type.FOR_SALE) {
					CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY,4);
					heap.destroy();
				}
			}
		} else {
			Heap[] heaps = new Heap[Dungeon.level.heaps.values().size()];
			int j = 0;

			for (Heap heap : Dungeon.level.heaps.values()) {
				if (heap.type == Heap.Type.FOR_SALE) {
					heaps[j] = heap;
					j++;
				}
			}
			for(j = 0;j < heaps.length;j++){
				if(heaps[j] == null) break;
				CellEmitter.get(heaps[j].pos).burst(ElmoParticle.FACTORY,4);
				heaps[j].destroy();
			}

			/*for (Heap heap : Dungeon.level.heaps.values()) {
				if (heap.type == Heap.Type.FOR_SALE) {
					CellEmitter.get(heap.pos).burst(ElmoParticle.FACTORY,4);
					heap.destroy();
				}
			}*/
		}

		destroy();
		sprite.killAndErase();
		CellEmitter.get(pos).burst(ElmoParticle.FACTORY,6);


	}

	public boolean addItem(Item item){
		if (Dungeon.level instanceof RegularLevel && (((RegularLevel)Dungeon.level).room(pos) != null)){
			Room r = ((RegularLevel) Dungeon.level).room(pos);
			int cell;

			ArrayList<Integer> validCells = new ArrayList<>();
			for (Point p : r.getPoints()){
				cell = Dungeon.level.pointToCell(p);

				if(r.inside(p)) {
                    Heap heap = Dungeon.level.heaps.get(cell);
                    if (heap == null && Actor.findChar(cell) != this) {
                        validCells.add(cell);
                    }

                    if(heap != null && heap.type == Heap.Type.FOR_SALE && item.stackable){
                    	for(Item sale :heap.items){
							if (sale.isSimilar( item )) {
								sale.merge( item );
								return true;
							}
						}
					}
                }
			}

			if(!validCells.isEmpty()) {
				cell = Random.element(validCells);

				Dungeon.level.drop( item, cell ).type = Heap.Type.FOR_SALE;
				return true;
			}else{
				return false;
			}

		} else {
			return true;
		}

	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	public static WndBag sell() {
		return GameScene.selectItem( itemSelector, WndBag.Mode.FOR_SALE, Messages.get(Shopkeeper.class, "sell"));
	}
	
	private static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				WndBag parentWnd = sell();
				GameScene.show( new WndTradeItem( item, parentWnd ) );
			}
		}
	};

	@Override
	public boolean interact() {
		sell();
		return false;
	}
}
