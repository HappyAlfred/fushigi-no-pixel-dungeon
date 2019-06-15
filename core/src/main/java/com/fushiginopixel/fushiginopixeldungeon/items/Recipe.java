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

package com.fushiginopixel.fushiginopixeldungeon.items;

import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.items.food.Blandfruit;
import com.fushiginopixel.fushiginopixeldungeon.items.food.Food;
import com.fushiginopixel.fushiginopixeldungeon.items.food.SlimyGel;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfBeverage;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfExplode;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfLiquidFlame;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfPurity;
import com.fushiginopixel.fushiginopixeldungeon.items.stones.StoneOfAugmentation;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.darts.TippedDart;

import java.util.ArrayList;

public abstract class Recipe {
	
	public abstract boolean testIngredients(ArrayList<Item> ingredients);
	
	//not currently used
	public abstract int cost(ArrayList<Item> ingredients);
	
	public abstract Item brew(ArrayList<Item> ingredients);
	
	public abstract Item sampleOutput(ArrayList<Item> ingredients);

	public static abstract class SimpleRecipe extends Recipe {

		//*** These elements must be filled in by subclasses
		protected Class<?extends Item>[] inputs; //each class should be unique
		protected int[] inQuantity;

		protected int cost;

		protected Class<?extends Item> output;
		protected int outQuantity;
		//***

		//gets a simple list of items based on inputs
		public ArrayList<Item> getIngredients() {
			ArrayList<Item> result = new ArrayList<>();
			try {
				for (int i = 0; i < inputs.length; i++) {
					Item ingredient = inputs[i].newInstance();
					ingredient.quantity(inQuantity[i]);
					result.add(ingredient);
				}
			} catch (Exception e){
				Fushiginopixeldungeon.reportException( e );
				return null;
			}
			return result;
		}

		@Override
		public final boolean testIngredients(ArrayList<Item> ingredients) {

			int[] needed = inQuantity.clone();

			for (Item ingredient : ingredients){
				for (int i = 0; i < inputs.length; i++){
					if (ingredient.getClass() == inputs[i] && ingredient.isIdentifiedForAutomatic()){
						needed[i] -= ingredient.quantity();
						break;
					}
				}
			}

			for (int i : needed){
				if (i > 0){
					return false;
				}
			}

			return true;
		}

		public final int cost(ArrayList<Item> ingredients){
			return cost;
		}

		@Override
		public final Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			int[] needed = inQuantity.clone();

			for (Item ingredient : ingredients){
				for (int i = 0; i < inputs.length; i++) {
					if (ingredient.getClass() == inputs[i] && needed[i] > 0) {
						if (needed[i] <= ingredient.quantity()) {
							ingredient.quantity(ingredient.quantity() - needed[i]);
							needed[i] = 0;
						} else {
							needed[i] -= ingredient.quantity();
							ingredient.quantity(0);
						}
					}
				}
			}

			//sample output and real output are identical in this case.
			return itemOutput(null);
		}

		//ingredients are ignored, as output doesn't vary
		public Item itemOutput(ArrayList<Item> ingredients){
			try {
				Item result = output.newInstance();
				result.quantity(outQuantity);
				return result;
			} catch (Exception e) {
				Fushiginopixeldungeon.reportException( e );
				return null;
			}
		}

		//ingredients are ignored, as output doesn't vary
		public Item sampleOutput(ArrayList<Item> ingredients){
			try {
				Item result = output.newInstance();
				result.quantity(outQuantity);
				return result;
			} catch (Exception e) {
				Fushiginopixeldungeon.reportException( e );
				return null;
			}
		}
	}
	
	
	//*******
	// Static members
	//*******
	
	private static Recipe[] oneIngredientRecipes = new Recipe[]{
		new Food.OnigiriSlicing()
	};
	
	private static Recipe[] twoIngredientRecipes = new Recipe[]{
		new Blandfruit.CookFruit(),
		new PotionOfBeverage.RemakePotion(),
        new PotionOfExplode.MakeCannonBall(),
		new PotionOfPurity.ExtractHoney()
	};
	
	private static Recipe[] threeIngredientRecipes = new Recipe[]{
		new Potion.RandomPotion(),
		new Food.OnigiriProcess(),
		new SlimyGel.ExplosivesPreparing()
	};

    private static Recipe[] fourIngredientRecipes = new Recipe[]{
        new PotionOfLiquidFlame.AlchemyEnchantOfFire(),
		new StoneOfAugmentation.AlchemyEnchantOfBalance()
    };

	private static Recipe[] multipleIngredientRecipes = new Recipe[]{
		new TippedDart.TipDart(),
        new PotionOfLiquidFlame.Neutralization()
	};
	
	public static Recipe findRecipe(ArrayList<Item> ingredients){

		if(ingredients.size() > 0){
			for (Recipe recipe : multipleIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					return recipe;
				}
			}
		}

		if (ingredients.size() == 1){
			for (Recipe recipe : oneIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					return recipe;
				}
			}
			
		} else if (ingredients.size() == 2){
			for (Recipe recipe : twoIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					return recipe;
				}
			}
			
		} else if (ingredients.size() == 3){
			for (Recipe recipe : threeIngredientRecipes){
				if (recipe.testIngredients(ingredients)){
					return recipe;
				}
			}
		} else if (ingredients.size() == 4){
            for (Recipe recipe : fourIngredientRecipes){
                if (recipe.testIngredients(ingredients)){
                    return recipe;
                }
            }
        }
		
		return null;
	}
	
}


