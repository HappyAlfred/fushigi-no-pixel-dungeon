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
import com.watabou.utils.Point;
import com.watabou.utils.PointF;

import java.util.ArrayList;
import java.util.List;

public class Ballistica {

	//note that the path is the FULL path of the projectile, including tiles after collision.
	//make sure to generate a subPath for the common case of going source to collision.
	public ArrayList<Integer> path = new ArrayList<>();
	public ArrayList<PointF> pathPointF = new ArrayList<>();
	public Integer sourcePos = null;
	public Integer collisionPos = null;
	public PointF collisionPointF = null;
	public Integer dist = 0;


	private static final double A = 180 / Math.PI;
	//parameters to specify the colliding cell
	//000B:WONT_STOP,001B:STOP_TARGET,O10B:STOP_CHARS,100B:STOP_TERRAIN
	public static final int STOP_TARGET = 1; //ballistica will stop at the target cell
	public static final int STOP_CHARS = 2; //ballistica will stop on first char hit
	public static final int STOP_TERRAIN = 4; //ballistica will stop on terrain(LOS blocking, impassable, etc.)

	public static final int PROJECTILE =  	STOP_TARGET	| STOP_CHARS	| STOP_TERRAIN;

	public static final int MAGIC_BOLT =    STOP_CHARS  | STOP_TERRAIN;

	public static final int WONT_STOP =     0;

	public static final boolean USING_ROTATION = true;
	public static final boolean USING_POSITION = false;


	public Ballistica( int from, int to, int params ){
		this(from, to, params, false);
	}

	public Ballistica( int from, float to, int params, boolean using_rotation ){
		sourcePos = from;
		//build(from, to, (params & STOP_TARGET) > 0, (params & STOP_CHARS) > 0, (params & STOP_TERRAIN) > 0);

		if(using_rotation){
			build(from, new PointF((float)Math.cos(to / A), (float)Math.sin(to / A)), (params & STOP_TARGET) > 0, (params & STOP_CHARS) > 0, (params & STOP_TERRAIN) > 0);
		}else {
			int vectorX = Dungeon.level.cellToPoint((int) to).x - Dungeon.level.cellToPoint(from).x;
			int vectorY = Dungeon.level.cellToPoint((int) to).y - Dungeon.level.cellToPoint(from).y;
			build(from, new PointF(vectorX, vectorY), (params & STOP_TARGET) > 0, (params & STOP_CHARS) > 0, (params & STOP_TERRAIN) > 0);
		}
		if (collisionPos != null)
			dist = path.indexOf( collisionPos );
		else {
			collisionPos = path.get(dist = path.size() - 1);
			collisionPointF = pathPointF.get(dist);
		}
	}

	public Ballistica( int from, Point vector, int params ){
		sourcePos = from;
		//build(from, to, (params & STOP_TARGET) > 0, (params & STOP_CHARS) > 0, (params & STOP_TERRAIN) > 0);
		build(from, new PointF(vector.x ,vector.y), (params & STOP_TARGET) > 0, (params & STOP_CHARS) > 0, (params & STOP_TERRAIN) > 0);
		if (collisionPos != null)
			dist = path.indexOf( collisionPos );
		else {
			collisionPos = path.get(dist = path.size() - 1);
			collisionPointF = pathPointF.get(dist);
		}
	}

	private void build(int from, PointF vector, boolean stopTarget, boolean stopChars, boolean stopTerrain ) {
		int w = Dungeon.level.width();

		int x0 = from % w;	//from x
		int y0 = from / w;	//from y

		Point to = new Point(x0 + Math.round(vector.x),y0 + Math.round(vector.y));

		float stepX = 0;	//vector x extend 1
		float stepY = 0;	//vector y extend 1

		float lengthX = Math.abs( vector.x );	//length x
		float lengthY = Math.abs( vector.y );	//length y

		if(lengthX == lengthY && lengthX == 0){

			/*
			stepX = 0;
			stepY = 0;
			*/

			Point cell = Dungeon.level.cellToPoint(from);
			path.add(Dungeon.level.pointToCell(cell));
			pathPointF.add(new PointF(cell.x, cell.y).offset(0.5f,0.5f).scale(DungeonTilemap.SIZE));
			collide(Dungeon.level.pointToCell(cell));
			collidePointF(new PointF(cell.x, cell.y));
			return;

		}
		else if (lengthX > lengthY) {

			stepX = vector.x > 0 ? 1 : -1;		//x move 1,each step
			stepY = (float)vector.y / lengthX;	//y move vectorY/vectorX,each step

		} else {

			stepX = (float)vector.x /lengthY;	//x move vectorX/vectorY,each step
			stepY = vector.y > 0 ? 1 : -1;		//y move 1,each step

		}

		Point cell = Dungeon.level.cellToPoint(from);
		float tempX = 0;
		float tempY = 0;

		while (Dungeon.level.insideMap(cell)) {
			int cellPos = Dungeon.level.pointToCell(cell);

			/*
			for(int p:path){
				if(p == cellPos){
					return;
				}
			}
			*/

			//if we're in a wall, collide with the previous cell along the path.
			if (stopTerrain && cellPos != sourcePos && !Dungeon.level.passable[cellPos] && !Dungeon.level.avoid[cellPos]) {
				collide(path.get(path.size() - 1));
				collidePointF(new PointF(cell.x + tempX - stepX, cell.y + tempY - stepY));
			}

			path.add(cellPos);
			pathPointF.add(new PointF(cell.x + tempX, cell.y + tempY));

			if ((stopTerrain && cellPos != sourcePos && Dungeon.level.losBlocking[cellPos])
					|| (cellPos != sourcePos && stopChars && Actor.findChar( cellPos ) != null)
					|| (cell.equals(to) && stopTarget)){
				collide(Dungeon.level.pointToCell(cell));
				collidePointF(new PointF(cell.x + tempX, cell.y + tempY));
			}

			tempX += stepX;
			tempY += stepY;

			if(Math.round(tempX) != 0){
				cell.x += Math.round(tempX);
				tempX -= Math.round(tempX);
			}
			if(Math.round(tempY) != 0){
				cell.y += Math.round(tempY);
				tempY -= Math.round(tempY);
			}
		}

		for(PointF p: pathPointF){
			p.offset(0.5f, 0.5f).scale(DungeonTilemap.SIZE);
		}
	}

	//unused
	private void build( int from, int to, boolean stopTarget, boolean stopChars, boolean stopTerrain ) {
		int w = Dungeon.level.width();

		int x0 = from % w;	//from x
		int x1 = to % w;	//to x
		int y0 = from / w;	//from y
		int y1 = to / w;	//to y

		int dx = x1 - x0;	//vector x
		int dy = y1 - y0;	//vector y

		int stepX = dx > 0 ? +1 : -1;	//vector x extend 1
		int stepY = dy > 0 ? +1 : -1;	//vector y extend 1

		dx = Math.abs( dx );	//length x
		dy = Math.abs( dy );	//length y

		int stepA;
		int stepB;
		int dA;		//longer side
		int dB;		//shorter side

		if (dx > dy) {

			stepA = stepX;		//vector x extend 1→pos + 1
			stepB = stepY * w;	//vector y extend 1→pos + w
			dA = dx;
			dB = dy;

		} else {

			stepA = stepY * w;	//vector y extend 1→pos + w
			stepB = stepX;		//vector x extend 1→pos + 1
			dA = dy;
			dB = dx;

		}

		int cell = from;

		int err = dA / 2;
		while (Dungeon.level.insideMap(cell)) {

			//if we're in a wall, collide with the previous cell along the path.
			if (stopTerrain && cell != sourcePos && !Dungeon.level.passable[cell] && !Dungeon.level.avoid[cell]) {
				collide(path.get(path.size() - 1));
			}

			path.add(cell);

			if ((stopTerrain && cell != sourcePos && Dungeon.level.losBlocking[cell])
					|| (cell != sourcePos && stopChars && Actor.findChar( cell ) != null)
					|| (cell == to && stopTarget)){
				collide(cell);
			}

			cell += stepA;

			err += dB;
			if (err >= dA) {	//dA <= 2 * dB
				err = err - dA;
				cell = cell + stepB;
			}
		}
	}

	//we only want to record the first position collision occurs at.
	private void collide(int cell){
		if (collisionPos == null)
			collisionPos = cell;
	}

	private void collidePointF(PointF cell){
		if (collisionPointF == null) {
			collisionPointF = cell.offset(0.5f,0.5f).scale(DungeonTilemap.SIZE);
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
