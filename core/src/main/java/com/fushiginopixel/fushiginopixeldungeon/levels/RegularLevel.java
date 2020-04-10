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

package com.fushiginopixel.fushiginopixeldungeon.levels;

import com.fushiginopixel.fushiginopixeldungeon.Bones;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Bestiary;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.items.DewVial;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.Artifact;
import com.fushiginopixel.fushiginopixeldungeon.items.journal.GuidePage;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.GoldenKey;
import com.fushiginopixel.fushiginopixeldungeon.journal.Document;
import com.fushiginopixel.fushiginopixeldungeon.levels.builders.Builder;
import com.fushiginopixel.fushiginopixeldungeon.levels.builders.LoopBuilder;
import com.fushiginopixel.fushiginopixeldungeon.levels.builders.TreeBuilder;
import com.fushiginopixel.fushiginopixeldungeon.levels.builders.WebBuilder;
import com.fushiginopixel.fushiginopixeldungeon.levels.painters.Painter;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.Room;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.secret.SecretRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.secret.SecretShopRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.special.PitRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.special.ShopRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.special.SpecialRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.standard.EntranceRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.standard.ExitRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.standard.StandardRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.standard.StandardShopRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.BlazingTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.BurningTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.ChillingTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.DisintegrationTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.ExplosiveTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.FrostTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.Trap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.WornDartTrap;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class RegularLevel extends Level {
	
	protected ArrayList<Room> rooms;
	
	protected Builder builder;
	
	protected Room roomEntrance;
	protected Room roomExit;
	
	public int secretDoors;
	public LevelTraps levelTraps = null;
	
	@Override
	protected boolean build() {
		
		builder = builder();
		
		ArrayList<Room> initRooms = initRooms();
		Random.shuffle(initRooms);
		
		do {
			for (Room r : initRooms){
				r.neigbours.clear();
				r.connected.clear();
			}
			rooms = builder.build((ArrayList<Room>)initRooms.clone());
		} while (rooms == null);
		
		return painter().paint(this, rooms);
		
	}
	
	protected ArrayList<Room> initRooms() {
		ArrayList<Room> initRooms = new ArrayList<>();
		initRooms.add ( roomEntrance = new EntranceRoom());
		initRooms.add( roomExit = new ExitRoom());
		
		int standards = standardRooms();
		for (int i = 0; i < standards; i++) {
			StandardRoom s;
			do {
				s = StandardRoom.createRoom();
			} while (!s.setSizeCat( standards-i ));
			i += s.sizeCat.roomValue-1;
			initRooms.add(s);
		}

		int secrets = Dungeon.mode.isNormalMode() ? SecretRoom.secretsForFloor(Dungeon.depth) : SecretRoom.specialSecretsForFloor(Dungeon.depth);

		if (Dungeon.shopOnLevel()) {
			initRooms.add(new ShopRoom());
		}
		if(Random.Float(1) < Dungeon.mode.standardShopChance()){
			initRooms.add(new StandardShopRoom());
		}
		
		int specials = specialRooms();
		SpecialRoom.initForFloor();
		for (int i = 0; i < specials; i++)
			initRooms.add(SpecialRoom.createRoom());

		for (int i = 0; i < secrets; i++) {
			SecretRoom r =SecretRoom.createRoom();
			/*
			while((initRooms.contains(new ShopRoom()) || initRooms.contains(new SecretShopRoom())) && r instanceof SecretShopRoom){
				r = SecretRoom.createRoom();
			}
			*/
			initRooms.add(r);
		}
		return initRooms;
	}
	
	protected int standardRooms(){
		return 0;
	}
	
	protected int specialRooms(){
		return 0;
	}
	
	protected Builder builder(){
		ArrayList<Builder> builders = new ArrayList<>();

		builders.add(new LoopBuilder()
				.setLoopShape( 2 ,
						Random.Float(0.4f, 0.7f),
						Random.Float(0f, 0.5f)));
		builders.add(new TreeBuilder());
		builders.add(new WebBuilder()
				.setWebTunnelLength(Random.IntRange(8,10)));
		return Random.element(builders);
	}
	
	protected abstract Painter painter();
	
	protected float waterFill(){
		return 0;
	}
	
	protected int waterSmoothing(){
		return 0;
	}
	
	protected float grassFill(){
		return 0;
	}
	
	protected int grassSmoothing(){
		return 0;
	}
	
	protected int nTraps() {
		//return Random.NormalIntRange( 1, 3+(Dungeon.depth/3) );
		//1 + (0 ~ 100/9 )
		return Random.NormalIntRange( 1, 1 + (int)(100 * (float)Dungeon.depth / (Dungeon.depth + Dungeon.mode.maxDepth() * 8)) );
	}

	public Class<?>[] trapClasses(){
		return new Class<?>[]{WornDartTrap.class};
	}

	public float[] trapChances() {
		return new float[]{1};
	}
	
	@Override
	public int nMobs() {
		switch(Dungeon.depth) {
			case 1:
				//mobs are not randomly spawned on floor 1.
				return 5  + Random.Int(5);
			default:
				//more deeper ,more monster!
				//return 2 + Random.Int(Dungeon.depth / 10) + Random.Int(Dungeon.depth % 10) + Random.Int(5);
				return 5 + Random.Int(Dungeon.depth / 10) + Random.Int(5);
		}
	}
	
	private ArrayList<Class<?extends Mob>> mobsToSpawn = new ArrayList<>();
	
	@Override
	public Mob createMob() {
		if(Bestiary.getStandardMobRotation(Dungeon.depth, Dungeon.mode).isEmpty()){
			return null;
		}
		if (mobsToSpawn == null || mobsToSpawn.isEmpty()) {
			if (!Statistics.thief)
				mobsToSpawn = Bestiary.getMobRotation(Dungeon.depth, Dungeon.mode);
			else
				mobsToSpawn = Bestiary.getGuardRotation();
				Random.shuffle(mobsToSpawn);
		}
		
		try {
			return mobsToSpawn.remove(0).newInstance();
		} catch (Exception e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}
	}

	public void refreshMobsToSpawn(){
		mobsToSpawn.clear();
	}
	
	@Override
	protected void createMobs() {
		//on floor 1, 10 rats are created so the player can get level 2.
		//int mobsToSpawn = Dungeon.depth == 1 ? 10 : Math.min(nMobs(), MAX_OF_RESPAWN);
		int mobsToSpawn = Math.min(nMobs(), MAX_OF_RESPAWN);

		ArrayList<Room> stdRooms = new ArrayList<>();
		for (Room room : rooms) {
			if (room instanceof StandardRoom && room != roomEntrance) {
				for (int i = 0; i < ((StandardRoom) room).sizeCat.roomValue; i++) {
					stdRooms.add(room);
				}
			}
		}
		Random.shuffle(stdRooms);
		Iterator<Room> stdRoomIter = stdRooms.iterator();

		while (mobsToSpawn > 0) {
			if (!stdRoomIter.hasNext())
				stdRoomIter = stdRooms.iterator();
			Room roomToSpawn = stdRoomIter.next();

			Mob mob = createMob();
			mob.pos = pointToCell(roomToSpawn.random());

			if (findMob(mob.pos) == null && passable[mob.pos] && mob.pos != exit) {
				mobsToSpawn--;
				mobs.add(mob);

				//TODO: perhaps externalize this logic into a method. Do I want to make mobs more likely to clump deeper down?
				if (mobsToSpawn > 0 && Random.Int(4) == 0){
					mob = createMob();
					mob.pos = pointToCell(roomToSpawn.random());

					if (findMob(mob.pos)  == null && passable[mob.pos] && mob.pos != exit) {
						mobsToSpawn--;
						mobs.add(mob);
					}
				}
			}
		}

		for (Mob m : mobs){
			if (map[m.pos] == Terrain.HIGH_GRASS) {
				map[m.pos] = Terrain.GRASS;
				losBlocking[m.pos] = false;
			}

		}

	}
	
	@Override
	public int randomRespawnCell() {
		int count = 0;
		int cell = -1;
		
		while (true) {
			
			if (++count > 30) {
				return -1;
			}
			
			Room room = randomRoom( StandardRoom.class );
			if (room == null || room == roomEntrance) {
				continue;
			}

			cell = pointToCell(room.random(1));
			if (!heroFOV[cell]
					&& Actor.findChar( cell ) == null
					&& passable[cell]
					&& cell != exit) {
				return cell;
			}
			
		}
	}
	
	@Override
	public int randomDestination() {
		
		int count = 0;
		int cell = -1;
		
		while (true) {
			
			if (++count > 30) {
				return -1;
			}
			
			Room room = Random.element( rooms );
			if (room == null) {
				continue;
			}
			
			cell = pointToCell(room.random());
			if (passable[cell]) {
				return cell;
			}
			
		}
	}
	
	@Override
	protected void createItems() {

		if (!Dungeon.LimitedDrops.DEW_VIAL.dropped()) {
			addItemToSpawn( new DewVial() );
			Dungeon.LimitedDrops.DEW_VIAL.drop();
		}
		
		// drops 3/4/5 items 60%/30%/10% of the time
		int nItems = 3 + Random.chances(new float[]{6, 3, 1});
		
		for (int i=0; i < nItems; i++) {
			Heap.Type type = null;
			switch (Random.Int( 20 )) {
			case 0:
				type = Heap.Type.SKELETON;
				break;
			case 1:
			case 2:
			case 3:
			case 4:
				type = Heap.Type.CHEST;
				break;
			case 5:
				type = Dungeon.depth > 1 ? Heap.Type.MIMIC : Heap.Type.CHEST;
				break;
			default:
				type = Heap.Type.HEAP;
			}
			int cell = randomDropCell();
			if (map[cell] == Terrain.HIGH_GRASS) {
				map[cell] = Terrain.GRASS;
				losBlocking[cell] = false;
			}
			
			Item toDrop = Generator.random();

			if (toDrop == null) continue;

			if ((toDrop instanceof Artifact && Random.Int(2) == 0) ||
					(toDrop.isUpgradable() && Random.Int(4 - toDrop.level()) == 0)){
				Heap dropped = drop( toDrop, cell );
				if (heaps.get(cell) == dropped) {
					dropped.type = Heap.Type.LOCKED_CHEST;
					addItemToSpawn(new GoldenKey(Dungeon.depth));
				}
			} else {
				drop( toDrop, cell ).type = type;
			}
			
		}

		for (Item item : itemsToSpawn) {
			int cell = randomDropCell();
			drop( item, cell ).type = Heap.Type.HEAP;
			if (map[cell] == Terrain.HIGH_GRASS) {
				map[cell] = Terrain.GRASS;
				losBlocking[cell] = false;
			}
		}
		
		Item item = Bones.get();
		if (item != null) {
			int cell = randomDropCell();
			if (map[cell] == Terrain.HIGH_GRASS) {
				map[cell] = Terrain.GRASS;
				losBlocking[cell] = false;
			}
			drop( item, cell ).type = Heap.Type.REMAINS;
		}

		//guide pages
		Collection<String> allPages = Document.ADVENTURERS_GUIDE.pages();
		ArrayList<String> missingPages = new ArrayList<>();
		for ( String page : allPages){
			if (!Document.ADVENTURERS_GUIDE.hasPage(page)){
				missingPages.add(page);
			}
		}

		//these are dropped specially
		missingPages.remove(Document.GUIDE_INTRO_PAGE);
		missingPages.remove(Document.GUIDE_SEARCH_PAGE);

		int foundPages = allPages.size() - (missingPages.size() + 2);

		//chance to find a page scales with pages missing and depth
		if (missingPages.size() > 0 && Random.Float() < (Dungeon.depth/(float)(foundPages + 1))){
			GuidePage p = new GuidePage();
			p.page(missingPages.get(0));
			int cell = randomDropCell();
			if (map[cell] == Terrain.HIGH_GRASS) {
				map[cell] = Terrain.GRASS;
				losBlocking[cell] = false;
			}
			drop( p, cell );
		}

	}
	
	protected Room randomRoom( Class<?extends Room> type ) {
		Random.shuffle( rooms );
		for (Room r : rooms) {
			if (type.isInstance(r)) {
				return r;
			}
		}
		return null;
	}
	
	public Room room( int pos ) {
		for (Room room : rooms) {
			if (room.inside( cellToPoint(pos) )) {
				return room;
			}
		}
		
		return null;
	}
	
	protected int randomDropCell() {
		while (true) {
			Room room = randomRoom( StandardRoom.class );
			if (room != null && room != roomEntrance) {
				int pos = pointToCell(room.random());
				if (passable[pos]
						&& pos != exit
						&& heaps.get(pos) == null) {
					
					Trap t = traps.get(pos);
					
					//items cannot spawn on traps which destroy items
					if (t == null ||
							! (t instanceof BurningTrap || t instanceof BlazingTrap
							|| t instanceof ChillingTrap || t instanceof FrostTrap
							|| t instanceof ExplosiveTrap || t instanceof DisintegrationTrap)) {
						
						return pos;
					}
				}
			}
		}
	}
	
	@Override
	public int fallCell( boolean fallIntoPit ) {
		if (fallIntoPit) {
			for (Room room : rooms) {
				if (room instanceof PitRoom || room.legacyType.equals("PIT")) {
					int result;
					do {
						result = pointToCell(room.random());
					} while (traps.get(result) != null
							|| findMob(result) != null
							|| heaps.get(result) != null);
					return result;
				}
			}
		}
		
		return super.fallCell( false );
	}
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( "rooms", rooms );
		bundle.put( "mobs_to_spawn", mobsToSpawn.toArray(new Class[0]));
		bundle.put("levelTraps", levelTraps);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		rooms = new ArrayList<>( (Collection<Room>) ((Collection<?>) bundle.getCollection( "rooms" )) );
		for (Room r : rooms) {
			r.onLevelLoad( this );
			if (r instanceof EntranceRoom || r.legacyType.equals("ENTRANCE")){
				roomEntrance = r;
			} else if (r instanceof ExitRoom  || r.legacyType.equals("EXIT")){
				roomExit = r;
			}
		}
		
		if (bundle.contains( "mobs_to_spawn" )) {
			for (Class<? extends Mob> mob : bundle.getClassArray("mobs_to_spawn")) {
				if (mob != null) mobsToSpawn.add(mob);
			}
		}
		levelTraps = (LevelTraps)bundle.get("levelTraps");
	}

	public class LevelTraps implements Bundlable{
		public Class<?>[] trapClasses;
		public float[] trapChances;

		public LevelTraps(Class<?>[] trapClasses, float[] trapChances){
			if(Dungeon.mode.setLevelTrap(this)){

			}else {
				this.trapClasses = trapClasses;
				this.trapChances = trapChances;
			}
		}

		private static final String TRAPCLASSES = "trapClasses";
		private static final String TRAPCHANCES = "trapChances";

		@Override
		public void restoreFromBundle(Bundle bundle) {
			trapClasses = bundle.getClassArray(TRAPCLASSES);
			trapChances = bundle.getFloatArray(TRAPCHANCES);
		}

		@Override
		public void storeInBundle(Bundle bundle) {
			bundle.put(TRAPCLASSES, trapClasses);
			bundle.put(TRAPCHANCES, trapChances);
		}
	}
	
}
