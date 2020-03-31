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
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Diablo;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Yog;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.FlameParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.SkeletonKey;
import com.fushiginopixel.fushiginopixeldungeon.levels.painters.Painter;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Group;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class HallsMidBossLevel extends Level {
	
	{
		color1 = 0x801500;
		color2 = 0xa68521;
		
		viewDistance = Math.min(4, viewDistance);
		stage = 4;
	}

	private static final int WIDTH = 33;
	private static final int HEIGHT = 33;

	private static final int ROOM_WIDTH		= 23;
	private static final int ROOM_HEIGHT	= 11;
	private static final int HALL_WIDTH		= 11;
	private static final int HALL_HEIGHT	= 29;

	private static final int ROOM_LEFT	= (WIDTH - ROOM_WIDTH) / 2;
	private static final int HALL_LEFT	= (WIDTH - HALL_WIDTH) / 2;
	private static final int HALL_TOP	= (HEIGHT - HALL_HEIGHT)/2;
	private static final int ROOM_TOP	= (ROOM_WIDTH - HALL_WIDTH)/2 + HALL_TOP;

	private int arenaDoor;
	private boolean enteredArena = false;
	private boolean keyDropped = false;
	
	@Override
	public String tilesTex() {
		return Assets.TILES_HALLS;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_HALLS;
	}
	
	private static final String DOOR	= "door";
	private static final String ENTERED	= "entered";
	private static final String DROPPED	= "droppped";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( DOOR, arenaDoor );
		bundle.put( ENTERED, enteredArena );
		bundle.put( DROPPED, keyDropped );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		arenaDoor = bundle.getInt( DOOR );
		enteredArena = bundle.getBoolean( ENTERED );
		keyDropped = bundle.getBoolean( DROPPED );
	}

	@Override
	public String getMusic(){
		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if(lock != null) return Assets.BOSS_MID;
		if(super.getMusic() != null){
			return super.getMusic();
		}else{
			return Assets.TUNE_HALLS;
		}
	}
	
	@Override
	protected boolean build() {
		
		setSize(WIDTH, HEIGHT);

		Painter.fill( this, HALL_LEFT, HALL_TOP, HALL_WIDTH, HALL_HEIGHT, Terrain.EMPTY );
		Painter.fill( this, ROOM_LEFT, ROOM_TOP, ROOM_WIDTH, ROOM_HEIGHT, Terrain.EMPTY );
		exit = WIDTH / 2 + (HALL_TOP - 1) * width();
		map[exit] = Terrain.LOCKED_EXIT;
		entrance = (HALL_TOP + HALL_HEIGHT - 1) * width() + WIDTH / 2;
		map[entrance] = Terrain.ENTRANCE;
		Painter.fill( this, HALL_LEFT, HALL_TOP + HALL_HEIGHT - 4, HALL_WIDTH, 1, Terrain.WALL );

		arenaDoor = (HALL_TOP + HALL_HEIGHT - 4) * width() + WIDTH / 2;
		map[arenaDoor] = Terrain.DOOR;
		
		boolean[] patch = Patch.generate(width, height, 0.30f, 6, true);
		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && patch[i]) {
				map[i] = Terrain.WATER;
			}
		}
		
		for (int i = 0; i < length(); i++) {
			if (map[i] == Terrain.EMPTY && Random.Int(10) == 0) {
				map[i] = Terrain.EMPTY_DECO;
			}
		}
		
		return true;
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
			int pos;
			do {
				pos =
						Random.IntRange( HALL_LEFT, HALL_LEFT + HALL_WIDTH - 1 ) +
								Random.IntRange( HALL_TOP + HALL_HEIGHT - 3, HALL_TOP + HALL_HEIGHT - 1 ) * width();
			} while (pos == entrance);
			drop( item, pos ).type = Heap.Type.REMAINS;
		}
	}
	
	@Override
	public int randomRespawnCell() {
		int cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		while (!passable[cell]){
			cell = entrance + PathFinder.NEIGHBOURS8[Random.Int(8)];
		}
		return cell;
	}
	
	@Override
	public void press( int cell, Char hero ) {
		
		super.press( cell, hero );
		
		if (!enteredArena && outsideEntraceRoom( cell ) && hero == Dungeon.hero) {
			
			enteredArena = true;
			seal();

			for (Mob m : mobs){
				//bring the first ally with you
				if (m.alignment == Char.Alignment.ALLY){
					m.pos = Dungeon.hero.pos + (Random.Int(2) == 0 ? +1 : -1);
					m.sprite.place(m.pos);
					break;
				}
			}

			Diablo boss = new Diablo();
			boss.state = boss.WANDERING;
			int count = 0;
			boss.pos = (ROOM_TOP + ROOM_HEIGHT/2) * width() + WIDTH / 2;
			while (!passable[boss.pos] ||
					!outsideEntraceRoom( boss.pos )) {
				boss.pos += (Random.Int(2) == 0 ? 1 : -1) + (Random.Int(2) == 0 ? 1 : -1) * width();
			}
			GameScene.add( boss );

			if (heroFOV[boss.pos]) {
				boss.notice();
				boss.sprite.alpha( 0 );
				boss.sprite.parent.add( new AlphaTweener( boss.sprite, 1, 0.1f ) );
			}

			Camera.main.shake( 3, 0.7f );
			Sample.INSTANCE.play( Assets.SND_ROCKS );
			set( arenaDoor, Terrain.LOCKED_DOOR );
			GameScene.updateMap( arenaDoor );
			Dungeon.observe();
		}
	}

	private boolean outsideEntraceRoom( int cell ) {
		return cell / width() < arenaDoor / width();
	}
	
	@Override
	public Heap drop( Item item, int cell ) {
		
		if (!keyDropped && item instanceof SkeletonKey) {
			keyDropped = true;
			unseal();

			set( arenaDoor, Terrain.DOOR );
			GameScene.updateMap( arenaDoor );
		}
		
		return super.drop( item, cell );
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_name");
			case Terrain.GRASS:
				return Messages.get(HallsLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(HallsLevel.class, "high_grass_name");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
			case Terrain.WATER:
				return Messages.get(HallsLevel.class, "water_desc");
			case Terrain.STATUE:
			case Terrain.STATUE_SP:
				return Messages.get(HallsLevel.class, "statue_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(HallsLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}
	
	@Override
	public Group addVisuals () {
		super.addVisuals();
		HallsLevel.addHallsVisuals( this, visuals );
		return visuals;
	}
}
