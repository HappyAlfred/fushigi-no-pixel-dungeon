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

package com.fushiginopixel.fushiginopixeldungeon.items.food;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Hunger;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.Recipe;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.MasterThievesArmband;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfExplode;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SlimyGel extends Food {

	{
		image = ItemSpriteSheet.GEL;
		energy = Hunger.STARVING/20f;
	}

	@Override
	public int price() {
		return 20 * quantity;
	}


	public static class ExplosivesPreparing extends Recipe {

		public static int gelNeed = 1;
		public static int potionNeedA = 1;
		public static int potionNeedB = 1;

		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			ArrayList<Item> temp = new ArrayList<>();

			int gelCount = 0;
			for(Item item : ingredients){
				if(item.getClass() == SlimyGel.class){
					gelCount += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != 1) return false;

			int potionCountA = 0;
			Item potionA = null;
			for(Item item : ingredients){
				if(item instanceof Potion){
					potionA = item;
					potionCountA += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != 2) return false;

			int potionCountB = 0;
			for(Item item : ingredients){
				if(item instanceof Potion && item.isSimilar(potionA)){
					potionCountB += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != ingredients.size()) return false;

			for(int i = 0;i < temp.size() ; i++){
				ingredients.set(i, temp.get(i));
			}

			if (gelCount >= gelNeed
					&& potionCountA >= potionNeedA
					&& potionCountB >= potionNeedB){
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

			for (Item ingredient : ingredients){
				ingredient.quantity(ingredient.quantity() - 1);
			}


			return new PotionOfExplode().quantity(2);
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			return new WndBag.Placeholder(ItemSpriteSheet.POTION_HOLDER){
				{
					name = Messages.get(ExplosivesPreparing.class, "name");
				}

				@Override
				public String info() {
					return Messages.get(ExplosivesPreparing.class, "desc");
				}
			};
		}
	}
}
