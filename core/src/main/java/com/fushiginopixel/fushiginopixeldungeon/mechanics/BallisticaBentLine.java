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
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BallisticaBentLine {

	public ArrayList<Integer> path = new ArrayList<>();
	public ArrayList<PointF> pathPointF = new ArrayList<>();
	public ArrayList<PointF> nodes = new ArrayList<>();
	public Integer sourcePos = null;
	public Integer collisionPos = null;
	public PointF collisionPointF = null;
	public Integer dist = 0;
	public Float wide = 0f;


	private static final double A = 180 / Math.PI;
	//parameters to specify the colliding cell
	//000B:WONT_STOP,001B:STOP_TARGET,O10B:STOP_CHARS,100B:STOP_TERRAIN
	public static final int STOP_TARGET = 1; //ballistica will stop at the target cell
	public static final int STOP_CHARS = 2; //ballistica will stop on first char hit
	public static final int STOP_TERRAIN = 4; //ballistica will stop on terrain(LOS blocking, impassable, etc.)

	public static final int PROJECTILE =  	STOP_CHARS	| STOP_TERRAIN;

	public static final int WONT_STOP =     0;

	public BallisticaBentLine(int from, float to, float wide, int bendRange, int params, boolean using_rotation ){
		sourcePos = from;
		//build(from, to, (params & STOP_TARGET) > 0, (params & STOP_CHARS) > 0, (params & STOP_TERRAIN) > 0);

		if(using_rotation){
			build(from, to, wide, bendRange, params);
		}else {
			int vectorX = Dungeon.level.cellToPoint((int) to).x - Dungeon.level.cellToPoint(from).x;
			int vectorY = Dungeon.level.cellToPoint((int) to).y - Dungeon.level.cellToPoint(from).y;
			float angle = (float) new Point(vectorX, vectorY).getVectorAngle();
			build(from, angle, wide, bendRange, params);
		}

		if (collisionPos != null)
			dist = path.indexOf( collisionPos );
		else {
			collisionPos = path.get(dist = path.size() - 1);
			collisionPointF = pathPointF.get(dist);
		}
	}

	private void build(int from, float angle, float wide, int bendRange, int params ) {
		Point cell = Dungeon.level.cellToPoint(from);
		path.add(from);
		pathPointF.add(new PointF(cell.x, cell.y).offset(0.5f, 0.5f).scale(DungeonTilemap.SIZE));
		nodes.add(new PointF(cell.x, cell.y).offset(0.5f, 0.5f).scale(DungeonTilemap.SIZE));
		while (Dungeon.level.insideMap(cell)) {
			float rMin = (angle - wide / 2) % 360;
			float rMax = (angle + wide / 2) % 360;
			float a = Random.Float(rMin, rMax);
			int cellPos = Dungeon.level.pointToCell(cell);

			Ballistica bend = new Ballistica(cellPos, a, params, Ballistica.USING_ROTATION);
			int dist = Math.min(bend.dist, bendRange);
			if(dist <= bendRange){
				collide(bend.path.get(dist));
				collidePointF(bend.pathPointF.get(dist));
				if(dist == 0) break;
			}

			for(int i = 1; i < (dist + 1); i++){
				path.add(bend.path.get(i));
				pathPointF.add(bend.pathPointF.get(i));
			}
			nodes.add(bend.pathPointF.get(dist));

			cell = Dungeon.level.cellToPoint(bend.path.get(dist));
		}
	}

	//we only want to record the first position collision occurs at.
	private void collide(int cell){
		if (collisionPos == null)
			collisionPos = cell;
	}

	private void collidePointF(PointF cell){
		if (collisionPointF == null) {
			//collisionPointF = cell.offset(0.5f,0.5f).scale(DungeonTilemap.SIZE);
			collisionPointF = cell;
		}
	}

	//returns a segment of the path from start to end, inclusive.
	//if there is an error, returns an empty arraylist instead.
	public List<Integer> subPath(int start, int end){
		try {
			end = Math.min( end, path.size()-1);
			return path.subList(start, end+1);
		} catch (Exception e){
			Fushiginopixeldungeon.reportException(e);
			return new ArrayList<>();
		}
	}
}
