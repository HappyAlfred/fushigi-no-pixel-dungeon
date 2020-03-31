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

package com.fushiginopixel.fushiginopixeldungeon.mechanics;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BallisticaSector {

	//note that the path is the FULL path of the projectile, including tiles after collision.
	//make sure to generate a subPath for the common case of going source to collision.
	public HashSet<Integer> affectedCells = new HashSet<>();
	public Integer sourcePos = null;
	public Integer dist = 0;
	public Float wide = 0f;


	private static final double A = 180 / Math.PI;
	//parameters to specify the colliding cell
	//000B:WONT_STOP,001B:STOP_TARGET,O10B:STOP_CHARS,100B:STOP_TERRAIN
	public static final int STOP_TARGET = 1; //ballistica will stop at the target cell
	public static final int STOP_CHARS = 2; //ballistica will stop on first char hit
	public static final int STOP_SOLID = 4; //ballistica will stop on terrain(LOS blocking, impassable, etc.)

	public static final int PROJECTILE =  	STOP_CHARS	| STOP_SOLID;

	public static final int WONT_STOP =     0;


	public BallisticaSector(int from, float to, float wide, int range, int params, boolean using_rotation ){
		sourcePos = from;
		//build(from, to, (params & STOP_TARGET) > 0, (params & STOP_CHARS) > 0, (params & STOP_TERRAIN) > 0);

		if(using_rotation){
			build(from, to, wide, range, params);
		}else {
			int vectorX = Dungeon.level.cellToPoint((int) to).x - Dungeon.level.cellToPoint(from).x;
			int vectorY = Dungeon.level.cellToPoint((int) to).y - Dungeon.level.cellToPoint(from).y;
			float angle = (float) new Point(vectorX, vectorY).getVectorAngle();
			if((params & STOP_TARGET) > 0){
				range = Math.max(Dungeon.level.distance(from, (int) to), range);
			}
			build(from, angle, wide, range, params);
		}
	}

	private void build(int from, float angle, float wide, int range, int params ) {
		affectedCells = new HashSet<>();
		boolean stopChars = (params & STOP_CHARS) > 0;
		boolean stopSolid = (params & STOP_SOLID) > 0;

		float rMin = (angle - wide / 2) % 360;
		float rMax = (angle + wide / 2) % 360;

		PathFinder.buildDistanceMap(from, BArray.not(Dungeon.level.solid, null), range);
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				Point vector = Dungeon.level.cellToPoint(from).vector(Dungeon.level.cellToPoint(i));
				float a = (float) vector.getVectorAngle();
				if((rMin > rMax && a > rMin && a < rMax)
						|| (rMin <= rMax && (a < rMin || a > rMax))
						|| PathFinder.distance[i] > range){
					continue;
				}
				Ballistica shot = new Ballistica(from, vector, params);
				for(Integer j: shot.subPath(1, range)){
					if (stopSolid && Dungeon.level.solid[j]){
						break;
					}
					affectedCells.add(j);
					if(stopChars && Actor.findChar( j ) != null){
						break;
					}
				}
			}
		}
	}
}
