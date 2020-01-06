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

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Bones;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Tengu;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Warden;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.WoolParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.IronKey;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.MazeRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.Room;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.standard.EmptyRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.GrippingTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.Trap;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.plants.Plant;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.tiles.CustomTiledVisual;
import com.fushiginopixel.fushiginopixeldungeon.ui.TargetHealthIndicator;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PrisonMidBossLevel extends Level {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
		stage = 1;
	}

	public enum State{
		START,
		FIGHT_START,
		FIGHT_ARENA,
		WON
	}
	
	private State state;
	private Warden warden;

	//keep track of that need to be removed as the level is changed. We dump 'em back into the level at the end.
	private ArrayList<Item> storedItems = new ArrayList<>();
	
	@Override
	public String tilesTex() {
		return Assets.TILES_PRISON;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_PRISON;
	}
	
	private static final String STATE	        = "state";
	private static final String WARDEN	        = "warden";
	private static final String STORED_ITEMS    = "storeditems";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( STATE, state );
		bundle.put( WARDEN, warden );
		bundle.put( STORED_ITEMS, storedItems);
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle(bundle);
		state = bundle.getEnum( STATE, State.class );

		//in some states tengu won't be in the world, in others he will be.
		if (state == State.START) {
			warden = (Warden) bundle.get( WARDEN );
		} else {
			for (Mob mob : mobs){
				if (mob instanceof Warden) {
					warden = (Warden) mob;
					break;
				}
			}
		}

		for (Bundlable item : bundle.getCollection(STORED_ITEMS)){
			storedItems.add( (Item)item );
		}
	}

	@Override
	public String getMusic(){
		if(super.getMusic() != null){
			return super.getMusic();
		}else{
			return Assets.TUNE_PRISON;
		}
	}
	
	@Override
	protected boolean build() {
		
		setSize(33, 33);
		
		map = MAP_START.clone();

		buildFlagMaps();
		cleanWalls();

		state = State.START;
		entrance = 16+4*33;
		exit = 0;

		resetTraps();

		return true;
	}
	
	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
		warden = new Warden(); //We want to keep track of tengu independently of other mobs, he's not always in the level.
	}
	
	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			drop( item, randomRespawnCell() ).type = Heap.Type.REMAINS;
		}
	}

	private int randomPrisonCell(){
		int pos = 2+2*33; //initial position at top-left room

		//randomly assign a room.
		int xRoom = Random.Int(5);
		int yRoom = Random.Int(5);
		//remove 4 corner room
		while ((xRoom == 0 && yRoom == 0) || (xRoom == 0 && yRoom == 4) || (xRoom == 4 && yRoom == 0) || (xRoom == 4 && yRoom == 4)){
			xRoom = Random.Int(5);
			yRoom = Random.Int(5);
		}
		pos += xRoom*6; //one of the 5 room for x
		pos += yRoom*6*33; // one of the 5 room for y

		//and then a certain tile in that room.
		pos += Random.Int(5) + Random.Int(5)*33;

		return pos;
	}

	@Override
	public void press( int cell, Char ch ) {

		super.press(cell, ch);

		if (ch == Dungeon.hero){
			//hero enters tengu's chamber
			if (state == State.START
					&& (new EmptyRoom().set(13, 13, 19, 19)).inside(cellToPoint(cell))){
				progress(State.FIGHT_START);
			}
		}
	}

	@Override
	public int randomRespawnCell() {
		return 16+4*33 + PathFinder.NEIGHBOURS8[Random.Int(8)]; //random cell adjacent to the entrance.
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(PrisonLevel.class, "water_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return Messages.get(PrisonLevel.class, "empty_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(PrisonLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	private void resetTraps(){
		traps.clear();

		for (int i = 0; i < length(); i++){
			if (map[i] == Terrain.INACTIVE_TRAP) {
				Trap t = new GrippingTrap().reveal();
				t.active = false;
				setTrap(t, i);
				map[i] = Terrain.INACTIVE_TRAP;
			}
		}
	}

	public void toggleDoor(){
		for (int i = 0; i < length(); i++){
			if (thatWasADoor(i) && map[i] !=Terrain.DOOR) {
				set(i,Terrain.DOOR);
			}
		}
		for (int i = 0; i < length(); i++){
			if ((map[i] == Terrain.DOOR || map[i] == Terrain.LOCKED_DOOR) && Random.Int(2) == 0) {
				if(map[i] == Terrain.DOOR) set(i,Terrain.LOCKED_DOOR);
				else set(i,Terrain.DOOR);
				CellEmitter.get( i ).burst( Speck.factory(Speck.LIGHT), 6 );
			}
		}
		GameScene.resetMap();
		Dungeon.observe();
	}

	public boolean thatWasADoor(int pos){
		if(!passable[pos]) return false;
		int left = pos - 1;
		int right = pos + 1;
		int up = pos - width;
		int down = pos + width;
		if((left >= 0 && right < length && solid[left] && solid[right]) || (up >= 0 && down < length && solid[up] && solid[down]))
			return true;
		return false;
	}

	private void changeMap(int[] map){
		this.map = map.clone();
		buildFlagMaps();
		cleanWalls();

		exit = entrance = 0;
		for (int i = 0; i < length(); i ++)
			if (map[i] == Terrain.ENTRANCE)
				entrance = i;
			else if (map[i] == Terrain.EXIT)
				exit = i;

		BArray.setFalse(visited);
		BArray.setFalse(mapped);
		
		for (Blob blob: blobs.values()){
			blob.fullyClear();
		}
		addVisuals(); //this also resets existing visuals
		resetTraps();


		GameScene.resetMap();
		Dungeon.observe();
	}

	private void clearEntities(Room safeArea){
		for (Heap heap : heaps.values()){
			if (safeArea == null || !safeArea.inside(cellToPoint(heap.pos))){
				for (Item item : heap.items)
					storedItems.add(item);
				heap.destroy();
			}
		}
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[Dungeon.level.mobs.size()])){
			if (mob != warden && (safeArea == null || !safeArea.inside(cellToPoint(mob.pos)))){
				mob.destroy();
				if (mob.sprite != null)
					mob.sprite.killAndErase();
			}
		}
		for (Plant plant : plants.values()){
			if (safeArea == null || !safeArea.inside(cellToPoint(plant.pos))){
				plants.remove(plant.pos);
			}
		}
	}

	public void progress(State st){
		if(state.equals(st)) return;
		switch (st){
			//moving to the beginning of the fight
			case FIGHT_START:
				seal();
				set(16 + 13 * 33, Terrain.LOCKED_DOOR);
				GameScene.updateMap(16 + 13 * 33);

				for (Mob m : mobs){
					//bring the first ally with you
					if (m.alignment == Char.Alignment.ALLY){
						m.pos = 16 + 14 * 33; //they should immediately walk out of the door
						m.sprite.place(m.pos);
						break;
					}
				}

				warden.state = warden.HUNTING;
				warden.pos = 16 + 16*33; //in the middle of the fight room
				GameScene.add( warden );
				warden.notice();

				state = State.FIGHT_START;
				break;

			//halfway through, move to the maze
			case FIGHT_ARENA:
				if(warden.progressState == 0) {
					warden.progressState = 1;
				}else break;

					changeMap(MAP_ARENA);
					clearEntities((Room) new EmptyRoom().set(14, 2, 18, 12)); //clear the entrance

					GameScene.flash(0xFFFFFF);
					Sample.INSTANCE.play(Assets.SND_BLAST);

					state = State.FIGHT_ARENA;
					break;

			//arena ended, fight over.
			case WON:
				unseal();

				Dungeon.hero.interrupt();
				Dungeon.hero.pos = 16+16*33;
				Dungeon.hero.sprite.interruptMotion();
				Dungeon.hero.sprite.place(Dungeon.hero.pos);

				warden.pos = 16+17*33;
				warden.sprite.place(16 + 17 * 33);
				
				//remove all mobs, but preserve allies
				ArrayList<Mob> allies = new ArrayList<>();
				for(Mob m : mobs.toArray(new Mob[0])){
					if (m.alignment == Char.Alignment.ALLY){
						allies.add(m);
						mobs.remove(m);
					}
				}
				clearEntities(null);
				
				changeMap(MAP_END);
				
				for (Mob m : allies){
					do{
						m.pos = Random.IntRange(3, 7) + Random.IntRange(26, 30)*33;
					} while (findMob(m.pos) != null);
					m.sprite().place(m.pos);
					mobs.add(m);
				}

				warden.die(Dungeon.hero);

				for (Item item : storedItems)
					drop(item, randomPrisonCell());
				
				GameScene.flash(0xFFFFFF);
				Sample.INSTANCE.play(Assets.SND_BLAST);
				
				state = State.WON;
				break;
		}
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		PrisonLevel.addPrisonVisuals(this, visuals);
		return visuals;
	}

	private static final int W = Terrain.WALL;
	private static final int D = Terrain.DOOR;
	private static final int L = Terrain.LOCKED_DOOR;
	private static final int e = Terrain.EMPTY;

	private static final int T = Terrain.INACTIVE_TRAP;

	private static final int E = Terrain.ENTRANCE;
	private static final int X = Terrain.EXIT;

	private static final int M = Terrain.WALL_DECO;
	private static final int P = Terrain.PEDESTAL;

	//TODO if I ever need to store more static maps I should externalize them instead of hard-coding
	//Especially as I means I won't be limited to legal identifiers
	private static final int[] MAP_START =
			{       W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, M, W, M, W, W, W, M, W, M, W, W, W, M, W, M, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, E, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, M, W, M, W, W, W, M, W, M, W, W, W, M, D, M, W, W, W, M, W, M, W, W, W, M, W, M, W, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, M, W, M, W, W, W, M, W, M, W, W, W, M, D, M, W, W, W, M, W, M, W, W, W, M, W, M, W, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, M, W, M, W, W, W, M, W, M, W, W, W, M, W, M, W, W, W, M, W, M, W, W, W, M, W, M, W, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, W, W, W, W, W, W, M, W, M, W, W, W, M, W, M, W, W, W, M, W, M, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,};

		private static final int[] MAP_ARENA =
			{       W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, M, W, W, W, W, W, M, W, W, W, W, W, M, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, E, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, M, W, W, W, W, D, M, D, W, W, W, D, M, D, W, W, W, D, M, D, W, W, W, W, M, W, W, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, D, M, D, W, W, W, D, M, D, W, W, W, D, M, D, W, W, W, D, M, D, W, W, W, D, M, D, W, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, D, M, D, W, W, W, D, M, D, W, W, W, D, M, D, W, W, W, D, M, D, W, W, W, D, M, D, W, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, W, W, W, W, W, W, D, M, D, W, W, W, D, M, D, W, W, W, D, M, D, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,};

	private static final int[] MAP_END =
			{       W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, M, W, M, W, W, W, M, W, M, W, W, W, M, W, M, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, D, e, e, E, e, e, D, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, M, W, M, W, W, W, M, D, M, W, W, W, M, D, M, W, W, W, M, D, M, W, W, W, M, W, M, W, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, M, D, M, W, W, W, M, D, M, W, W, W, M, D, M, W, W, W, M, D, M, W, W, W, M, D, M, W, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, M, D, M, W, W, W, M, D, M, W, W, W, M, D, M, W, W, W, M, D, M, W, W, W, M, D, M, W, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, D, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W,
					W, W, W, W, W, W, W, W, W, M, D, M, W, W, W, M, D, M, W, W, W, M, D, M, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, D, e, e, X, e, e, D, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, e, e, e, e, e, W, e, e, e, e, e, W, e, e, e, e, e, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,};



}
