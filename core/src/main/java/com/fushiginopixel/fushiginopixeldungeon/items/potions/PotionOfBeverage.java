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

package com.fushiginopixel.fushiginopixeldungeon.items.potions;

import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Challenges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Healing;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Hunger;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.SpellSprite;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.Recipe;
import com.fushiginopixel.fushiginopixeldungeon.items.food.Blandfruit;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.plants.Plant;
import com.fushiginopixel.fushiginopixeldungeon.plants.Sungrass;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PotionOfBeverage extends Potion {

	{
		initials = 16;

		bones = true;
	}
	
	@Override
	public void apply( Hero hero ) {
		knownByUse();
		(hero.buff( Hunger.class )).satisfy( Math.round(Hunger.STARVING / 10) );
		GLog.p( Messages.get(this, "drinks") );
		SpellSprite.show( hero, SpellSprite.FOOD );
	}


	@Override
	public int price() {
		return isKnown() ? 5 * quantity : super.price();
	}

	public static class RemakePotion extends Recipe {

		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 2) return false;

			if (ingredients.get(0).getClass() == PotionOfBeverage.class && ingredients.get(0).isIdentifiedForAutomatic()) {
				if (!(ingredients.get(1) instanceof Plant.Seed)) {
					return false;
				}
			} else if (ingredients.get(0) instanceof Plant.Seed) {
				if (ingredients.get(1).getClass() == PotionOfBeverage.class && ingredients.get(0).isIdentifiedForAutomatic()) {
					Item temp = ingredients.get(0);
					ingredients.set(0, ingredients.get(1));
					ingredients.set(1, temp);
				} else {
					return false;
				}
			} else {
				return false;
			}

			PotionOfBeverage beverage = (PotionOfBeverage) ingredients.get(0);
			Plant.Seed seed = (Plant.Seed) ingredients.get(1);

			if (beverage.quantity() >= 1
					&& seed.quantity() >= 1 && seed.alchemyClass != null) {

				if (Dungeon.isChallenged(Challenges.NO_HEALING)
						&& seed instanceof Sungrass.Seed) {
					return false;
				}

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

			ingredients.get(0).quantity(ingredients.get(0).quantity() - 1);
			ingredients.get(1).quantity(ingredients.get(1).quantity() - 1);

			Class<? extends Item> plantclass = ((Plant.Seed) ingredients.get(1)).alchemyClass;

			Item result;
			if(Random.Int(5) == 0){
				result = Generator.random( Generator.Category.POTION );
			}
			else{
				try {
					result = plantclass.newInstance();
				}catch (Exception e) {

					Fushiginopixeldungeon.reportException(e);
					result = Generator.random( Generator.Category.POTION );

				}
			}
			while (result instanceof PotionOfHealing
					&& (Dungeon.isChallenged(Challenges.NO_HEALING))) {
				result = Generator.random(Generator.Category.POTION);
			}

			Statistics.potionsCooked++;
			Badges.validatePotionsCooked();

			return result;
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			return new WndBag.Placeholder(ItemSpriteSheet.POTION_HOLDER){
				{
					name = Messages.get(RemakePotion.class, "name");
				}

				@Override
				public String info() {
					return Messages.get(RemakePotion.class, "desc");
				}
			};
		}
	}

}
