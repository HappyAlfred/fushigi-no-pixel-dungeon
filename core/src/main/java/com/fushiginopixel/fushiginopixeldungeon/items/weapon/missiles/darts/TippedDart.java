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
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.PinCushion;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.Recipe;
import com.fushiginopixel.fushiginopixeldungeon.plants.Blindweed;
import com.fushiginopixel.fushiginopixeldungeon.plants.Dreamfoil;
import com.fushiginopixel.fushiginopixeldungeon.plants.Earthroot;
import com.fushiginopixel.fushiginopixeldungeon.plants.Fadeleaf;
import com.fushiginopixel.fushiginopixeldungeon.plants.Firebloom;
import com.fushiginopixel.fushiginopixeldungeon.plants.Icecap;
import com.fushiginopixel.fushiginopixeldungeon.plants.Plant;
import com.fushiginopixel.fushiginopixeldungeon.plants.Rotberry;
import com.fushiginopixel.fushiginopixeldungeon.plants.Sorrowmoss;
import com.fushiginopixel.fushiginopixeldungeon.plants.Starflower;
import com.fushiginopixel.fushiginopixeldungeon.plants.Stormvine;
import com.fushiginopixel.fushiginopixeldungeon.plants.Sungrass;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class TippedDart extends Dart {
	
	{
		bones = true;
	}
	
	@Override
	protected void rangedHit(Char enemy, int cell) {
		if (enemy.isAlive() && sticky)
			Buff.affect(enemy, PinCushion.class).stick(new Dart());
		else
			Dungeon.level.drop( new Dart(), enemy.pos ).sprite.drop();
	}
	
	@Override
	public int price() {
		return 6 * quantity;
	}
	
	private static HashMap<Class<?extends Plant.Seed>, Class<?extends TippedDart>> types = new HashMap<>();
	static {
		types.put(Blindweed.Seed.class,     BlindingDart.class);
		types.put(Dreamfoil.Seed.class,     SleepDart.class);
		types.put(Earthroot.Seed.class,     ParalyticDart.class);
		types.put(Fadeleaf.Seed.class,      DisplacingDart.class);
		types.put(Firebloom.Seed.class,     IncendiaryDart.class);
		types.put(Icecap.Seed.class,        ChillingDart.class);
		types.put(Rotberry.Seed.class,      RotDart.class);
		types.put(Sorrowmoss.Seed.class,    PoisonDart.class);
		types.put(Starflower.Seed.class,    HolyDart.class);
		types.put(Stormvine.Seed.class,     ShockingDart.class);
		types.put(Sungrass.Seed.class,      HealingDart.class);
	}
	
	public static TippedDart randomTipped(){
		Plant.Seed s;
		do{
			s = (Plant.Seed) Generator.random(Generator.Category.SEED);
		} while (!types.containsKey(s.getClass()));
		
		try{
			return (TippedDart) types.get(s.getClass()).newInstance().quantity(2);
		} catch (Exception e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}
		
	}
	
	/*public static class TipDart extends Recipe{
		
		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 2) return false;
			
			if (ingredients.get(0).getClass() == Dart.class){
				if (!(ingredients.get(1) instanceof Plant.Seed)){
					return false;
				}
			} else if (ingredients.get(0) instanceof Plant.Seed){
				if (ingredients.get(1).getClass() == Dart.class){
					Item temp = ingredients.get(0);
					ingredients.set(0, ingredients.get(1));
					ingredients.set(1, temp);
				} else {
					return false;
				}
			} else {
				return false;
			}
			
			Plant.Seed seed = (Plant.Seed) ingredients.get(1);
			
			if (ingredients.get(0).quantity() >= 2
					&& seed.quantity() >= 1
					&& types.containsKey(seed.getClass())){
				return true;
			}
			
			return false;
		}
		
		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 2;
		}
		
		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			ingredients.get(0).quantity(ingredients.get(0).quantity() - 2);
			ingredients.get(1).quantity(ingredients.get(1).quantity() - 1);
			
			try{
				return types.get(ingredients.get(1).getClass()).newInstance().quantity(2);
			} catch (Exception e) {
				Fushiginopixeldungeon.reportException(e);
				return null;
			}
			
		}
		
		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			try{
				return types.get(ingredients.get(1).getClass()).newInstance().quantity(2);
			} catch (Exception e) {
				Fushiginopixeldungeon.reportException(e);
				return null;
			}
		}
	}*/

	public static class TipDart extends Recipe{

		public static int dartNeed = 2;
		public static int seedNeed = 1;
		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			ArrayList<Item> temp = new ArrayList<>();

			int seedCount = 0;
			for(Item item : ingredients){
				if(item instanceof Plant.Seed){
					seedCount += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != 1) return false;

			int dartCount = 0;
			for(Item item : ingredients){
				if(item.getClass() == Dart.class){
					dartCount += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != ingredients.size()) return false;

			for(int i = 0;i < temp.size() ; i++){
				ingredients.set(i, temp.get(i));
			}

			Plant.Seed seed = (Plant.Seed) ingredients.get(0);

			if (seedCount >= seedNeed
					&& dartCount >= dartNeed
					&& types.containsKey(seed.getClass())){
				return true;
			}

			return false;
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 3;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			int dart = dartNeed;
			int seed = seedNeed;
			for(int i=0 ; i<ingredients.size() ; i++){
				if(ingredients.get(i).getClass() == Dart.class && ingredients.get(i).quantity() > 0 && dart > 0){
					ingredients.get(i).quantity(ingredients.get(i).quantity() - 1);
					dart --;
					i--;
					continue;
				}

				if(ingredients.get(i) instanceof Plant.Seed && ingredients.get(i).quantity() > 0 && seed > 0){
					ingredients.get(i).quantity(ingredients.get(i).quantity() - 1);
					seed --;
					i--;
					continue;
				}
			}

			try{
				return types.get(ingredients.get(0).getClass()).newInstance().quantity(2);
			} catch (Exception e) {
				Fushiginopixeldungeon.reportException(e);
				return null;
			}

		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			try{
				return types.get(ingredients.get(0).getClass()).newInstance().quantity(2);
			} catch (Exception e) {
				Fushiginopixeldungeon.reportException(e);
				return null;
			}
		}
	}
}
