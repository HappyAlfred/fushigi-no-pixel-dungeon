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

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.fushiginopixel.fushiginopixeldungeon.levels.RegularLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MultiplicationTrap extends Trap {

	{
		color = TEAL;
		shape = DIAMOND;
	}

	@Override
	public void activate() {

		ArrayList<Integer> validCells = new ArrayList<>();
		if(!(Dungeon.level instanceof RegularLevel)){
			return;
		}

		for(int i = 0 ; i < Dungeon.level.length() ; i++){
			int j = Random.Int(15);
			if((Dungeon.level.map[i] == Terrain.EMPTY || Dungeon.level.map[i] == Terrain.INACTIVE_TRAP) && j == 0) {
				validCells.add(i);
			}
		}

		for (int trapPos : validCells) {

			Class<?>[] trapClasses = ((RegularLevel)Dungeon.level).trapClasses();
			float[] trapChances = ((RegularLevel)Dungeon.level).trapChances();
			try {
				Trap trap = ((Trap)trapClasses[Random.chances( trapChances )].newInstance()).hide();
				if(!Dungeon.bossLevel() || !trap.dangerous() || !(trap instanceof MultiplicationTrap)) {
					Dungeon.level.setTrap(trap, trapPos);
					//some traps will not be hidden
					Dungeon.level.map[trapPos] = trap.visible ? Terrain.TRAP : Terrain.SECRET_TRAP;
				}else{
					trap.disarm();
					Dungeon.level.setTrap(trap, trapPos);
					//some traps will not be hidden
					Dungeon.level.map[trapPos] = trap.visible ? Terrain.TRAP : Terrain.SECRET_TRAP;
				}
				int flags = Terrain.flags[Dungeon.level.map[trapPos]];
				Dungeon.level.passable[trapPos]		= (flags & Terrain.PASSABLE) != 0;
				Dungeon.level.secret[trapPos]		= (flags & Terrain.SECRET) != 0;
				Dungeon.level.avoid[trapPos]		= (flags & Terrain.AVOID) != 0;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		GLog.w( Messages.get(this, "multiplication") );
		Sample.INSTANCE.play(Assets.SND_ROCKS);

	}
}
