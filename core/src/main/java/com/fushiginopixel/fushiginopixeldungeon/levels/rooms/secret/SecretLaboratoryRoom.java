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

import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Alchemy;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfExperience;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfExplode;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfFrost;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfHealing;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfInvisibility;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfLevitation;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfLiquidFlame;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfMindVision;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfPanacea;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfParalyticGas;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfPurity;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfVenom;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.HashMap;

//TODO specific implementation
public class SecretLaboratoryRoom extends SecretRoom {
	
	private static HashMap<Class<? extends Potion>, Float> potionChances = new HashMap<>();
	static{
		potionChances.put(PotionOfHealing.class,        2f);
		potionChances.put(PotionOfExperience.class,     5f);
		potionChances.put(PotionOfToxicGas.class,       1f);
		potionChances.put(PotionOfParalyticGas.class,   3f);
		potionChances.put(PotionOfLiquidFlame.class,    1f);
		potionChances.put(PotionOfLevitation.class,     1f);
		potionChances.put(PotionOfMindVision.class,     3f);
		potionChances.put(PotionOfPurity.class,         2f);
		potionChances.put(PotionOfInvisibility.class,   1f);
		potionChances.put(PotionOfFrost.class,          1f);

		potionChances.put(PotionOfPanacea.class,        2f);
		potionChances.put(PotionOfExplode.class,   		 5f);
		potionChances.put(PotionOfVenom.class,          5f);
	}
	
	public void paint( Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY_SP );
		
		entrance().set( Door.Type.HIDDEN );
		
		Point pot = center();
		Painter.set( level, pot, Terrain.ALCHEMY );
		
		Alchemy alchemy = new Alchemy();
		alchemy.seed( level, pot.x + level.width() * pot.y, Random.IntRange(30, 60) );
		level.blobs.put( Alchemy.class, alchemy );
		
		int n = Random.IntRange( 2, 3 );
		HashMap<Class<? extends Potion>, Float> chances = new HashMap<>(potionChances);
		for (int i=0; i < n; i++) {
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != Terrain.EMPTY_SP || level.heaps.get( pos ) != null);
			
			try{
				Class<?extends Potion> potionCls = Random.chances(chances);
				chances.put(potionCls, 0f);
				level.drop( potionCls.newInstance(), pos );
			} catch (Exception e){
				Fushiginopixeldungeon.reportException(e);
			}
			
		}
		
	}
	
}
