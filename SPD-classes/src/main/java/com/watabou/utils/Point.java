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

package com.watabou.utils;

public class Point {

	public int x;
	public int y;
    private static final double A = 180 / Math.PI;
	
	public Point() {
	}
	
	public Point( int x, int y ) {
		this.x = x;
		this.y = y;
	}
	
	public Point( Point p ) {
		this.x = p.x;
		this.y = p.y;
	}
	
	public Point set( int x, int y ) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	public Point set( Point p ) {
		x = p.x;
		y = p.y;
		return this;
	}
	
	public Point clone() {
		return new Point( this );
	}
	
	public Point scale( float f ) {
		this.x *= f;
		this.y *= f;
		return this;
	}
	
	public Point offset( int dx, int dy ) {
		x += dx;
		y += dy;
		return this;
	}
	
	public Point offset( Point d ) {
		x += d.x;
		y += d.y;
		return this;
	}

	public Point vector( Point d ) {
		x = d.x - x;
		y = d.y - y;
		return this;
	}

    public Point vectorRotate( float angle ) {
		/*
		x1 = cosA * x - sinA * y
		y1 = sinA * x + cosB * y
		*/
        double x1 = x * Math.cos(angle/A) - y * Math.sin(angle/A);
        double y1 = x * Math.sin(angle/A) + y * Math.cos(angle/A);
        x = (int)Math.round(x1);
        y = (int)Math.round(y1);
        return this;
    }

	public double getVectorAngle() {
		return Math.atan2( y, x ) * A;
	}
	
	@Override
	public boolean equals( Object obj ) {
		if (obj instanceof Point) {
			Point p = (Point)obj;
			return p.x == x && p.y == y;
		} else {
			return false;
		}
	}
}
