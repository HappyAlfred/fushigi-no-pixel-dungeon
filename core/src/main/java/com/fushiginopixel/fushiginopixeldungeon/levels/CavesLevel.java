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
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Blacksmith;
import com.fushiginopixel.fushiginopixeldungeon.levels.painters.CavesPainter;
import com.fushiginopixel.fushiginopixeldungeon.levels.painters.Painter;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.Room;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.BurningTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.ConfusionTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.CorrosionTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.ExplosiveTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.FrostTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.GrippingTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.GuardianTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.LogTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.MucusTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.MultiplicationTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.PitfallTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.PoisonDartTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.RockfallTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.StormTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.SummoningTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.WarpingTrap;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.WeakeningTrap;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CavesLevel extends RegularLevel {

	{
		color1 = 0x534f3e;
		color2 = 0xb9d661;

		viewDistance = Math.min(6, viewDistance);
		stage = 2;
	}
	
	@Override
	protected ArrayList<Room> initRooms() {
		if(Dungeon.mode.isNormalMode()) {
			return Blacksmith.Quest.spawn(super.initRooms());
		}else{
			return super.initRooms();
		}
	}
	
	@Override
	protected int standardRooms() {
		//6 to 9, average 7.333
		return 6+Random.chances(new float[]{2, 3, 3, 1});
	}
	
	@Override
	protected int specialRooms() {
		//1 to 3, average 2.2
		return 1+Random.chances(new float[]{2, 4, 4});
	}
	
	@Override
	protected Painter painter() {
		LevelTraps levelTraps = new LevelTraps(trapClasses(), trapChances());
		this.levelTraps = levelTraps;
		return new CavesPainter()
				.setWater(feeling == Feeling.WATER ? 0.85f : 0.30f, 6)
				.setGrass(feeling == Feeling.GRASS ? 0.65f : 0.15f, 3)
				.setTraps(nTraps(), levelTraps);
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_CAVES;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_CAVES;
	}
	
	@Override
	public Class<?>[] trapClasses() {
		return new Class[]{ BurningTrap.class, PoisonDartTrap.class, FrostTrap.class, StormTrap.class, CorrosionTrap.class,
				GrippingTrap.class, ExplosiveTrap.class, RockfallTrap.class,  GuardianTrap.class, MucusTrap.class, LogTrap.class, WeakeningTrap.class,
				ConfusionTrap.class, SummoningTrap.class, WarpingTrap.class, MultiplicationTrap.class,
				PitfallTrap.class };
	}

	@Override
	public float[] trapChances() {
		return new float[]{ 8, 8, 8, 8, 8,
				4, 4, 4, 4, 4, 4,
				2, 2, 2, 2,
				1 };
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
			case Terrain.GRASS:
				return Messages.get(CavesLevel.class, "grass_name");
			case Terrain.HIGH_GRASS:
				return Messages.get(CavesLevel.class, "high_grass_name");
			case Terrain.WATER:
				return Messages.get(CavesLevel.class, "water_name");
			default:
				return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc( int tile ) {
		switch (tile) {
			case Terrain.ENTRANCE:
				return Messages.get(CavesLevel.class, "entrance_desc");
			case Terrain.EXIT:
				return Messages.get(CavesLevel.class, "exit_desc");
			case Terrain.HIGH_GRASS:
				return Messages.get(CavesLevel.class, "high_grass_desc");
			case Terrain.WALL_DECO:
				return Messages.get(CavesLevel.class, "wall_deco_desc");
			case Terrain.BOOKSHELF:
				return Messages.get(CavesLevel.class, "bookshelf_desc");
			default:
				return super.tileDesc( tile );
		}
	}

	@Override
	public String getMusic(){
		if(super.getMusic() != null){
			return super.getMusic();
		}else{
			return Assets.TUNE_CAVES;
		}
	}
	
	@Override
	public Group addVisuals() {
		super.addVisuals();
		addCavesVisuals( this, visuals );
		return visuals;
	}
	
	public static void addCavesVisuals( Level level, Group group ) {
		for (int i=0; i < level.length(); i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				group.add( new Vein( i ) );
			}
		}
	}
	
	private static class Vein extends Group {
		
		private int pos;
		
		private float delay;
		
		public Vein( int pos ) {
			super();
			
			this.pos = pos;
			
			delay = Random.Float( 2 );
		}
		
		@Override
		public void update() {
			
			if (visible = (pos < Dungeon.level.heroFOV.length && Dungeon.level.heroFOV[pos])) {
				
				super.update();

				if ((delay -= Game.elapsed) <= 0) {

					//pickaxe can remove the ore, should remove the sparkling too.
					if (Dungeon.level.map[pos] != Terrain.WALL_DECO){
						kill();
						return;
					}
					
					delay = Random.Float();
					
					PointF p = DungeonTilemap.tileToWorld( pos );
					((Sparkle)recycle( Sparkle.class )).reset(
						p.x + Random.Float( DungeonTilemap.SIZE ),
						p.y + Random.Float( DungeonTilemap.SIZE ) );
				}
			}
		}
	}
	
	public static final class Sparkle extends PixelParticle {
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			left = lifespan = 0.5f;
		}
		
		@Override
		public void update() {
			super.update();
			
			float p = left / lifespan;
			size( (am = p < 0.5f ? p * 2 : (1 - p) * 2) * 2 );
		}
	}
}