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

import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.painters.Painter;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.Room;
import com.watabou.utils.GameMath;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.HashSet;

public class CrossPipeRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(7, super.minWidth());
	}

	@Override
	public int minHeight() {
		return Math.max(7, super.minHeight());
	}

	@Override
	public float[] sizeCatProbs() {
		return new float[]{4, 2, 1};
	}

	@Override
	protected boolean setSize(int minW, int maxW, int minH, int maxH) {
		if(minW < minWidth()
				|| maxW > maxWidth()
				|| minH < minHeight()
				|| maxH > maxHeight()
				|| minW > maxW
				|| minH > maxH){
			return false;
		}else{

			int limitMax = Math.min(maxH, maxW);
			int limitMin = Math.max(minH, minW);
			int w = Random.NormalIntRange(limitMin, limitMax);
			if(w % 2 != 0 && (w + 1 > limitMax || w - 1 < limitMin)){
				return false;
			}else if(w % 2 != 0 && w + 1 > limitMax){
				w += 1;
			}else if(w % 2 != 0 && w - 1 > limitMin){
				w -= 1;
			}
			int h = w;

			resize(w - 1, h - 1);

			deviation = new Point(0,0);
			return true;
		}
	}

	private boolean straight = false;
	private Point deviation = new Point(0,0);

	@Override
	public boolean canConnect(Point p) {
		//refuses connections next to corners
		Point center = center();
		if(straight && super.canConnect(p)) {
			deviation = p.clone().vector(center);
			return ((p.x == left || p.x == right) && p.y == center.y) || ((p.y == top || p.y == bottom) && p.x == center.x);
		}else if(deviation.equals(new Point(0,0)) && super.canConnect(p)){
			deviation = p.clone().vector(center);
			return true;
		}else{
			return super.canConnect(p) && (center.equals(new Point(p.x + deviation.x, p.y + deviation.y))
					|| center.equals(new Point(p.x - deviation.x, p.y - deviation.y))
					|| center.equals(new Point(p.x + deviation.y, p.y - deviation.x))
					|| center.equals(new Point(p.x - deviation.y, p.y + deviation.x)));
		}
	}

	//considers both direction and point limits
	public boolean canConnect( Room r ){
		Rect i = intersect( r );

		boolean foundPoint = false;
		straight = true;
		for(int j = 0; j < 2; j++) {
			for (Point p : i.getPoints()) {
				if (canConnect(p) && r.canConnect(p)) {
					foundPoint = true;
					break;
				}
			}

			if(!foundPoint){
				straight = false;
			}
		}
		if (!foundPoint) return false;

		if (i.width() == 0 && i.left == left)
			return canConnect(LEFT) && r.canConnect(LEFT);
		else if (i.height() == 0 && i.top == top)
			return canConnect(TOP) && r.canConnect(TOP);
		else if (i.width() == 0 && i.right == right)
			return canConnect(RIGHT) && r.canConnect(RIGHT);
		else if (i.height() == 0 && i.bottom == bottom)
			return canConnect(BOTTOM) && r.canConnect(BOTTOM);
		else
			return false;
	}

	@Override
	public int maxConnections(int direction) {
		if (direction == ALL)   return 4;
		else                    return 1;
	}

	//FIXME this class is a total mess, lots of copy-pasta from tunnel and perimeter rooms here
	@Override
	public void paint(Level level) {

		Painter.fill( level, this, Terrain.WALL );

		Point start = center();
		Point end = new Point(start.x - deviation.x, start.y - deviation.y);
		ArrayList<Point> waterVector = new ArrayList<>();

        if (end.x == left) end.x += 2;
        else if (end.y == top) end.y += 2;
        else if (end.x == right) end.x -= 2;
        else if (end.y == bottom) end.y -= 2;

        if(end.x + 1 == right) end.x -= 1;
        if(end.y + 1 == bottom) end.y -= 1;
        if(end.x - 1 == left) end.x += 1;
        if(end.y - 1 == top) end.y += 1;

        Painter.drawLine(level, start, end, Terrain.WATER);

        for(Point p : getPoints()){
            if(level.map[level.pointToCell(p)] == Terrain.WATER){
                waterVector.add(start.clone().vector(p));
            }
        }

        for(int i = 0; i < 3; i++) {
            for (Point p : waterVector) {
                p = p.vectorRotate(90);
                Painter.set(level, start.clone().offset(p), Terrain.WATER);
            }
        }

		for(Point p : getPoints()){
			int cell = level.pointToCell(p);
			if (level.map[cell] == Terrain.WATER){
				for (int i : PathFinder.NEIGHBOURS8){
					if (level.map[cell + i] == Terrain.WALL){
						Painter.set(level, cell + i, Terrain.EMPTY);
					}
				}
			}
		}

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}
	}

	@Override
	public boolean canPlaceWater(Point p) {
		return false;
	}
}
