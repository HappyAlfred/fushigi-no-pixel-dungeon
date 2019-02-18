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

package com.fushiginopixel.fushiginopixeldungeon.levels.rooms.secret;

import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bomb;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.CannonBall;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.painters.Painter;
import com.watabou.utils.Random;

public class SecretArtilleryRoom extends SecretRoom {

	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 5);
	}

	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 5);
	}
	
	@Override
	public void paint(Level level) {
		super.paint(level);
		
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY_SP);
		
		Painter.set(level, center(), Terrain.STATUE_SP);

		int j = Random.Int(3,6);

		for (int i = 0; i < j; i++){
			int itemPos;
			do{
				itemPos = level.pointToCell(random());
			} while ( level.map[itemPos] != Terrain.EMPTY_SP
					|| level.heaps.get(itemPos) != null);
			
			if( i == 0 && Random.Int(3) > 0){
				level.drop(new Bomb.DoubleBomb(), itemPos);
			} else if (Random.Int(3) > 0) {
				level.drop(Generator.randomMissile(), itemPos);
			}else{
				level.drop(new CannonBall(), itemPos);
			}
		}
		
		entrance().set(Door.Type.HIDDEN);
	}
}
