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
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.GodownKeeper;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs.NpcAbbey;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs.NpcAbeys;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs.NpcAlfred;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs.NpcLynn;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs.NpcRen;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs.NpcRoberry;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.YunMeng;
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

public class SurfaceLevel extends Level {

	{
		color1 = 0x6a723d;
		color2 = 0x88924c;
		stage = 0;
	}

	//keep track of that need to be removed as the level is changed. We dump 'em back into the level at the end.
	private ArrayList<Item> storedItems = new ArrayList<>();
	
	@Override
	public String tilesTex() {
		return Assets.TILES_SEWERS;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_SEWERS;
	}
	
	@Override
	protected boolean build() {
		
		setSize(32, 32);
		
		map = MAP_START.clone();

		buildFlagMaps();
		cleanWalls();

		entrance = 25+23*32;
		exit = 17+13*32;


		placeNpc();
		return true;
	}

	private void placeNpc() {

		Mob alfred = new NpcAlfred();
		alfred.pos = 10 + 11 * 32;
		mobs.add( alfred );

		Mob ren = new NpcRen();
		ren.pos = 7 + 20 * 32;
		mobs.add( ren );

		Mob abbey = new NpcAbbey();
		abbey.pos = 8 + 10 * 32;
		mobs.add( abbey );

		Mob godownKeeper = new GodownKeeper();
		godownKeeper.pos = 11 + 1 * 32;
		mobs.add( godownKeeper );

		Mob yunmeng = new YunMeng();
		yunmeng.pos = 24 + 10 * 32;
		mobs.add( yunmeng );

		Mob abeys = new NpcAbeys();
		abeys.pos = 6 + 9 * 32;
		mobs.add( abeys );

		Mob lynn = new NpcLynn();
		lynn.pos = 12 + 10 * 32;
		mobs.add( lynn );

		Mob roberry = new NpcRoberry();
		roberry.pos = 17 + 5 * 32;
		mobs.add( roberry );

	}
	
	@Override
	public Mob createMob() {
		return null;
	}
	
	@Override
	protected void createMobs() {
	}
	
	public Actor respawner() {
		return null;
	}

	@Override
	protected void createItems() {
		Item item = Bones.get();
		if (item != null) {
			drop( item, exit - 1 ).type = Heap.Type.REMAINS;
		}
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(SewerLevel.class, "water_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.EMPTY_DECO:
				return Messages.get(SewerLevel.class, "empty_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(SewerLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public Group addVisuals() {
		super.addVisuals();
		SewerLevel.addSewerVisuals(this, visuals);
		return visuals;
	}

	private static final int W = Terrain.WALL;
	private static final int D = Terrain.DOOR;
	private static final int L = Terrain.LOCKED_DOOR;
	private static final int e = Terrain.EMPTY;
	private static final int A = Terrain.WATER;
	private static final int m = Terrain.EMPTY_SP;
	private static final int g = Terrain.GRASS;

	private static final int S = Terrain.STATUE;

	private static final int E = Terrain.ENTRANCE;
	private static final int X = Terrain.EXIT;

	private static final int M = Terrain.WALL_DECO;
	private static final int P = Terrain.PEDESTAL;

	//TODO if I ever need to store more static maps I should externalize them instead of hard-coding
	//Especially as I means I won't be limited to legal identifiers
	private static final int[] MAP_START =
			{       W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W,
					W, m, m, m, W, e, W, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, m, m, m, D, e, e, e, W, W,
					W, m, m, m, W, e, W, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, m, m, m, W, e, e, e, W, W,
					W, W, D, W, W, e, W, W, W, W, W, D, W, W, e, e, e, e, W, m, m, m, m, m, m, m, W, e, e, e, W, W,
					W, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, D, m, m, m, m, m, m, m, W, e, W, W, W, W,
					W, W, e, e, W, W, W, W, W, W, W, W, W, W, e, e, e, e, W, m, m, m, m, m, m, m, W, e, e, e, e, W,
					W, W, e, e, W, m, m, m, m, W, m, m, m, W, e, e, e, e, W, m, m, m, W, W, W, W, W, W, W, W, W, W,
					W, W, e, e, W, m, m, m, m, W, m, m, m, W, e, e, e, e, W, m, m, m, W, W, W, W, W, W, W, W, W, W,
					W, W, e, e, W, m, m, m, m, W, m, m, m, W, e, e, e, e, W, m, m, m, W, m, m, m, m, m, m, m, m, W,
					W, W, e, e, W, m, m, m, m, W, W, W, D, W, e, e, e, e, W, W, W, W, W, m, m, m, m, m, m, m, m, W,
					W, W, e, e, W, m, m, m, m, W, e, e, e, e, e, e, e, e, e, e, e, e, W, m, m, m, m, m, m, m, m, W,
					W, W, e, e, W, m, m, m, m, W, e, e, e, e, e, e, e, e, e, e, e, e, W, m, m, m, m, m, m, m, m, W,
					W, W, e, e, D, m, m, m, m, D, e, e, e, e, e, e, e, e, e, e, e, e, D, m, m, m, m, m, m, m, m, W,
					W, W, e, e, W, W, W, W, W, W, e, e, e, e, e, e, e, X, e, e, e, e, W, W, D, W, W, W, W, D, W, W,
					W, e, e, e, e, e, e, e, e, e, e, e, e, e, m, m, m, m, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
					W, e, e, e, e, e, e, e, e, e, e, e, e, e, m, A, A, m, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
					W, e, e, e, e, e, e, e, e, e, e, e, e, e, m, A, A, m, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
					W, e, e, e, e, e, e, e, e, e, e, e, e, e, m, m, m, m, e, e, e, e, e, e, e, e, e, e, e, e, e, W,
					W, W, W, D, W, W, W, W, W, W, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, W,
					W, e, e, e, W, m, m, m, m, W, e, e, e, e, e, e, e, e, e, e, e, e, W, m, m, m, m, m, m, m, m, W,
					W, e, e, e, W, m, m, m, m, W, e, e, e, e, e, e, e, e, e, e, e, e, D, m, m, m, m, m, m, m, m, W,
					W, e, e, e, W, m, m, m, m, W, e, e, e, e, e, e, e, e, e, e, e, e, W, m, m, m, m, m, m, m, m, W,
					W, W, D, W, W, m, m, m, m, W, W, W, D, W, e, e, e, e, W, D, W, W, W, W, W, W, W, m, m, m, m, W,
					W, m, m, m, W, m, m, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, W, m, E, W, m, m, m, m, W,
					W, m, m, m, W, m, m, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, W, m, m, W, m, m, m, m, W,
					W, m, m, m, W, m, m, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, D, m, m, W, W, W, D, W, W,
					W, m, m, m, W, m, m, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, W, m, m, W, g, g, g, g, W,
					W, W, W, W, W, m, m, m, m, m, m, m, m, W, e, e, e, e, W, m, m, m, m, W, m, m, W, e, e, e, e, W,
					W, m, m, m, W, W, W, W, W, W, W, W, W, W, e, e, e, e, W, W, W, W, W, W, W, W, W, g, g, g, g, W,
					W, m, m, m, D, e, e, e, W, W, W, W, W, W, e, e, e, e, W, W, W, W, W, W, W, W, W, e, e, e, e, W,
					W, m, m, m, W, e, e, e, e, e, e, e, e, e, e, e, e, e, W, W, W, W, W, W, W, W, W, g, g, g, g, W,
					W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W, W};

}
