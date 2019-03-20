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

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.levels.modes.Mode;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.Room;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public abstract class StandardRoom extends Room {
	
	public enum SizeCategory {
		
		NORMAL(4, 10, 1),
		LARGE(10, 14, 2),
		GIANT(14, 18, 3);
		
		public final int minDim, maxDim;
		public final int roomValue;
		
		SizeCategory(int min, int max, int val){
			minDim = min;
			maxDim = max;
			roomValue = val;
		}
		
		public int connectionWeight(){
			return roomValue*roomValue;
		}
		
	}
	
	public SizeCategory sizeCat;
	{ setSizeCat(); }

	//Note that if a room wishes to allow itself to be forced to a certain size category,
	//but would (effectively) never roll that size category, consider using Float.MIN_VALUE
	public float[] sizeCatProbs(){
		//always normal by default
		return new float[]{1, 0, 0};
	}
	
	public boolean setSizeCat(){
		return setSizeCat(0, SizeCategory.values().length-1);
	}
	
	//assumes room value is always ordinal+1
	public boolean setSizeCat( int maxRoomValue ){
		return setSizeCat(0, maxRoomValue-1);
	}
	
	//returns false if size cannot be set
	public boolean setSizeCat( int minOrdinal, int maxOrdinal ) {
		float[] probs = sizeCatProbs();
		SizeCategory[] categories = SizeCategory.values();
		
		if (probs.length != categories.length) return false;
		
		for (int i = 0; i < minOrdinal; i++)                    probs[i] = 0;
		for (int i = maxOrdinal+1; i < categories.length; i++)  probs[i] = 0;
		
		int ordinal = Random.chances(probs);
		
		if (ordinal != -1){
			sizeCat = categories[ordinal];
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int minWidth() { return sizeCat.minDim; }
	public int maxWidth() { return sizeCat.maxDim; }
	
	@Override
	public int minHeight() { return sizeCat.minDim; }
	public int maxHeight() { return sizeCat.maxDim; }
	
	@Override
	public int minConnections(int direction) {
		if (direction == ALL)   return 1;
		else                    return 0;
	}
	
	@Override
	public int maxConnections(int direction) {
		if (direction == ALL)   return 16;
		else                    return 4;
	}
	
	//FIXME this is a very messy way of handing variable standard rooms
	public static ArrayList<Class<?extends StandardRoom>> rooms = new ArrayList<>();
	static {//1+10+10
		rooms.add(EmptyRoom.class);


		rooms.add(SewerPipeRoom.class);
		rooms.add(RingRoom.class);

		rooms.add(SegmentedRoom.class);
		rooms.add(StatuesRoom.class);

		rooms.add(CaveRoom.class);
		rooms.add(CirclePitRoom.class);

		rooms.add(HallwayRoom.class);
		rooms.add(PillarsRoom.class);

		rooms.add(RuinsRoom.class);
		rooms.add(SkullsRoom.class);


		rooms.add(PlantsRoom.class);
		rooms.add(AquariumRoom.class);
		rooms.add(PlatformRoom.class);
		rooms.add(BurnedRoom.class);
		rooms.add(FissureRoom.class);
		rooms.add(GrassyGraveRoom.class);
		rooms.add(StripedRoom.class);
		rooms.add(StudyRoom.class);
		rooms.add(SuspiciousChestRoom.class);
		rooms.add(MinefieldRoom.class);
	}

	public static float[][] chances = new float[52][];
	static {
		chances[1] =  new float[]{20,  15,5, 0,0, 0,0, 0,0, 0,0,    1,0,1,0,1,0,1,1,0,0};
		chances[2] =  new float[]{20,  15,5, 0,0, 0,0, 0,0, 0,0,    1,1,1,1,1,1,1,1,1,1};
		chances[4] =  chances[3] = chances[2];
		chances[5] =  new float[]{50,  0,0, 0,0, 0,0, 0,0, 0,0,     0,0,0,0,0,0,0,0,0,0};
		chances[6] =  new float[]{20,  10,5, 5,0, 0,0, 0,0, 0,0,    1,1,1,1,1,1,1,1,1,1};
		chances[9] =  chances[8] = chances[7] = chances[6];
		chances[10] =  new float[]{50,  0,0, 0,0, 0,0, 0,0, 0,0,     0,0,0,0,0,0,0,0,0,0};
		
		chances[11] =  new float[]{20,  0,0, 15,5, 0,0, 0,0, 0,0,    1,1,1,1,1,1,1,1,1,1};
		chances[15] = chances[14] = chances[13] = chances[12] = chances[11];
		chances[16] =  new float[]{20,  0,0, 10,5, 5,0, 0,0, 0,0,    1,1,1,1,1,1,1,1,1,1};
		chances[20] = chances[19] = chances[18] = chances[17] = chances[16];
		
		chances[21] = new float[]{20,  0,0, 0,0, 15,5, 0,0, 0,0,    1,1,1,1,1,1,1,1,1,1};
		chances[25] = chances[24] = chances[23] = chances[22] = chances[21];
		chances[26] = new float[]{20,  0,0, 0,0, 10,5, 5,0, 0,0,    1,1,1,1,1,1,1,1,1,1};
		chances[30] = chances[29] = chances[28] = chances[27] = chances[26];

		chances[31] = new float[]{20,  0,0, 0,0, 0,0, 15,5, 0,0,    1,1,1,1,1,1,1,1,1,1};
		chances[35] = chances[34] = chances[33] = chances[32] = chances[31];
		chances[36] = new float[]{20,  0,0, 0,0, 0,0, 10,5, 5,0,    1,1,1,1,1,1,1,1,1,1};
		chances[40] = chances[39] = chances[38] = chances[37] = chances[36];
		
		chances[41] = chances[5];
		
		chances[42] = new float[]{20,  0,0, 0,0, 0,0, 0,0, 15,5,    1,1,1,1,1,1,1,1,1,1};
		chances[46] = chances[45] = chances[44] = chances[43] = chances[42];
		chances[47] = new float[]{0,  15,5, 15,5, 15,5, 15,5, 15,5,    1,1,1,1,1,1,1,1,1,1};
		chances[51] = chances[50] = chances[49] = chances[48] = chances[47];
	}
	
	
	public static StandardRoom createRoom(){
		/*
		try{
			return rooms.get(Random.chances(chances[Dungeon.depth])).newInstance();
		} catch (Exception e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}
		*/
		Mode mode = Dungeon.mode;
		return mode.createStandardRoom();
	}

	public enum Category {
		EMPTY(10),
		SEWER (10),
		PRISON (10),
		CAVE (10),
		CITY (10),
		HALL (10),
		SPECIAL (5);
		public Class<?>[] classes;
		public float[] probs;

		public float prob;
		public Class<? extends StandardRoom> superClass;

		private Category( float prob) {
			this.prob = prob;
		}

		static {
			EMPTY.classes = new Class<?>[]{
					EmptyRoom.class};
			EMPTY.probs = new float[]{ 1};

			SEWER.classes = new Class<?>[]{
					SewerPipeRoom.class,
					RingRoom.class/*,
					CrossPipeRoom.class*/};
			SEWER.probs = new float[]{ 15, 5/*, 5*/ };

			PRISON.classes = new Class<?>[]{
					SegmentedRoom.class,
					StatuesRoom.class};
			PRISON.probs = new float[]{ 15, 5 };

			CAVE.classes = new Class<?>[]{
					CaveRoom.class,
					CirclePitRoom.class};
			CAVE.probs = new float[]{ 15, 5 };

			CITY.classes = new Class<?>[]{
					HallwayRoom.class,
					PillarsRoom.class};
			CITY.probs = new float[]{ 15, 5 };

			HALL.classes = new Class<?>[]{
					RuinsRoom.class,
					SkullsRoom.class};
			HALL.probs = new float[]{ 15, 5 };

			SPECIAL.classes = new Class<?>[]{
					PlantsRoom.class,
					AquariumRoom.class,
					PlatformRoom.class,
					BurnedRoom.class,
					FissureRoom.class,
					GrassyGraveRoom.class,
					StripedRoom.class,
					StudyRoom.class,
					SuspiciousChestRoom.class,
					MinefieldRoom.class};
			SPECIAL.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		}
	}

	private static HashMap<Category,Float> categoryProbs = new LinkedHashMap<>();

	public static void reset() {
		for (Category cat : Category.values()) {
			if(!categoryProbs.containsKey(cat)) {
				categoryProbs.put(cat, cat.prob);
			}
		}
	}

	public static StandardRoom randomStandardRoom() {
		//reset();
		Category cat = Random.chances( categoryProbs );
		if (cat == null){
			reset();
			cat = Random.chances( categoryProbs );
		}
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return randomStandardRoom( cat );
	}

	public static StandardRoom randomStandardRoom( Category cat ) {
		try {
			return (StandardRoom)cat.classes[Random.chances( cat.probs )].newInstance();
		} catch (Exception e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}
	}
}
