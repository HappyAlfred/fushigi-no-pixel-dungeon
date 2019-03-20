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

package com.fushiginopixel.fushiginopixeldungeon;

import com.fushiginopixel.fushiginopixeldungeon.items.Dewdrop;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.ClothArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.HornOfPlenty;
import com.fushiginopixel.fushiginopixeldungeon.items.food.Blandfruit;
import com.fushiginopixel.fushiginopixeldungeon.items.food.Food;
import com.fushiginopixel.fushiginopixeldungeon.items.food.SpecialOnigiri;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfHealing;
import com.fushiginopixel.fushiginopixeldungeon.levels.DeadEndLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.LastLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.SurfaceLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.modes.Mode;
import com.fushiginopixel.fushiginopixeldungeon.levels.modes.NormalMode;
import com.fushiginopixel.fushiginopixeldungeon.levels.modes.SuperFushigiMode;

import java.util.ArrayList;

public class SpecialMode {

	//Some of these internal IDs are outdated and don't represent what these challenges do
	public static final int NORMAL		= 0;
	public static final int SUPER_FUSHIGI		= 1;
	//public static final int NIGHTMARE			= 2;
	/*public static final int NO_HEALING			= 3;
	public static final int NO_HERBALISM		= 4;
	public static final int SWARM_INTELLIGENCE	= 5;
	public static final int DARKNESS			= 6;
	public static final int NO_SCROLLS		    = 7;*/

	public static final int MAX_VALUE           = 2;

	public static final int[] MASKS = {
			SUPER_FUSHIGI
	};

	public static final ArrayList<Class <? extends Mode>> MODES = new ArrayList(){
		{
			add(NormalMode.class);
			add(SuperFushigiMode.class);
		}
	};

	public static Mode getMode(int i){
		Mode mode;
		try{
			mode = MODES.get(i).newInstance();
		}catch (Exception e){
			Fushiginopixeldungeon.reportException(e);
			mode = new NormalMode();
		}
		return mode;
	}

	public static int getModeValue(Class modeClass){
		return MODES.indexOf(modeClass);
	}

	/*
	public static Level newLevel(int Specialmode, int depth){
		Level level = null;
		if(Specialmode == SUPER_FUSHIGI){
			if (depth == 0) {
				level = new SurfaceLevel();
			}else if(depth > 0 && depth < 100){
				level = Level.randomLevel();
			}else if(depth == 100){
				level = new LastLevel();
			}

			if(level == null){
				level = new DeadEndLevel();
				Statistics.deepestFloor--;
			}
		}else{
			level = new DeadEndLevel();
			Statistics.deepestFloor--;
		}
		return level;
	}
	*/

}