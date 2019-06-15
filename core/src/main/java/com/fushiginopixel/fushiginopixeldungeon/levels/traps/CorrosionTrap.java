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

package com.fushiginopixel.fushiginopixeldungeon.levels.traps;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.CorrosiveGas;
import com.fushiginopixel.fushiginopixeldungeon.levels.RegularLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.Room;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;

import java.util.ArrayList;

public class CorrosionTrap extends Trap {

	{
		color = GREY;
		shape = GRILL;
	}

	@Override
	public void activate() {

		/*
		CorrosiveGas corrosiveGas = Blob.seed(pos, 80 + 5 * Dungeon.depth, CorrosiveGas.class);

		corrosiveGas.setStrength(1+Dungeon.depth/4);

		GameScene.add(corrosiveGas);
		*/
		ArrayList<Integer> gasCells = new ArrayList<>();
		if (Dungeon.level instanceof RegularLevel && ((RegularLevel)Dungeon.level).room( pos ) != null){
			Room r = ((RegularLevel) Dungeon.level).room(pos);
			int cell;
			for (Point p : r.getPoints()){
				cell = Dungeon.level.pointToCell(p);
				if (!Dungeon.level.solid[cell]){
					gasCells.add(cell);
				}
			}

			//if we don't have rooms, then just do 5x5
		} else {
			PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 2 );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE) {
					gasCells.add(i);
				}
			}
		}

		for (int cell : gasCells) {
			Blob corrosiveGas = Blob.seed(cell, 8, CorrosiveGas.class);
			((CorrosiveGas)corrosiveGas).setStrength(Dungeon.depth/5 + 1);
			//GameScene.add(Blob.seed(cell, 8, CorrosiveGas.class));
			GameScene.add(corrosiveGas);
		}

	}
}
