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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.darts;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.ShadowCaster;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplacingDart extends TippedDart {
	
	{
		image = ItemSpriteSheet.DISPLACING_DART;
	}
	
	@Override
	public int procInAttack(Char attacker, Char defender, int damage, EffectType type) {
		
		if (!defender.properties().contains(Char.Property.IMMOVABLE)){
			
			int startDist = Dungeon.level.distance(attacker.pos, defender.pos);
			
			HashMap<Integer, ArrayList<Integer>> positions = new HashMap<>();
			
			for (int pos = 0; pos < Dungeon.level.length(); pos++){
				if (Dungeon.level.heroFOV[pos]
						&& Dungeon.level.passable[pos]
						&& Actor.findChar(pos) == null){
					
					int dist = Dungeon.level.distance(attacker.pos, pos);
					if (dist > startDist){
						if (positions.get(dist) == null){
							positions.put(dist, new ArrayList<Integer>());
						}
						positions.get(dist).add(pos);
					}
					
				}
			}
			
			float[] probs = new float[ShadowCaster.MAX_DISTANCE+1];
			
			for (int i = 0; i <= ShadowCaster.MAX_DISTANCE; i++){
				if (positions.get(i) != null){
					probs[i] = i - startDist;
				}
			}
			
			int chosenDist = Random.chances(probs);
			
			if (chosenDist != -1){
				int pos = positions.get(chosenDist).get(Random.index(positions.get(chosenDist)));
				ScrollOfTeleportation.appear( defender, pos );
				Dungeon.level.press( pos, defender );
			}
		
		}
		
		return super.procInAttack(attacker, defender, damage, type);
	}
}
