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
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Hunger;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Recharging;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.SpellSprite;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.Recipe;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfBeverage;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfRecharging;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;

public class Food extends Item {

	public static float TIME_TO_EAT	= 1f;
	public float time_to_eat	= TIME_TO_EAT;
	
	public static final String AC_EAT	= "EAT";
	
	public float energy = Hunger.HUNGRY;
	public String message = Messages.get(this, "eat_msg");
	
	{
		stackable = true;
		image = ItemSpriteSheet.ONIGIRI;

		bones = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_EAT );
		return actions;
	}
	
	@Override
	public void execute(Char hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_EAT )) {
			
			detach( hero.belongings.backpack );

			if(hero instanceof Hero) {
				(hero.buff(Hunger.class)).satisfy(energy);
				GLog.i(message);

				switch (((Hero)hero).heroClass) {
					case WARRIOR:
						if (hero.HP < hero.HT) {
							hero.HP = Math.min(hero.HP + 5, hero.HT);
							hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
						}
						break;
					case MAGE:
						//1 charge
						Buff.affect(hero, Recharging.class, 4f);
						ScrollOfRecharging.charge(hero);
						break;
					case ROGUE:
					case HUNTRESS:
						break;
				}

				hero.sprite.operate(hero.pos);
				hero.busy();
				SpellSprite.show(hero, SpellSprite.FOOD);
				Sample.INSTANCE.play(Assets.SND_EAT);

				hero.spend(time_to_eat);

				Statistics.foodEaten++;
				Badges.validateFoodEaten();

			}
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int price() {
		return 10 * quantity;
	}

	/*
	public static class OnigiriSlicing extends Recipe {

		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 1) return false;

			if (ingredients.get(0) instanceof BigOnigiri && ingredients.get(0).quantity() >= 0){
				return true;
			} else {
				return false;
			}
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 1;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			ingredients.get(0).quantity(ingredients.get(0).quantity() - 1);


			return new Food().quantity(2);
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			return new Food().quantity(2);
		}
	}
	public static class OnigiriProcess extends Recipe {

		public static int foodNeed = 1;
		public static int meatNeed = 1;
		public static int beverageNeed = 1;

		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			ArrayList<Item> temp = new ArrayList<>();

			int foodCount = 0;
			for(Item item : ingredients){
				if(item.getClass() == Food.class){
					foodCount += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != 1) return false;

			int meatCount = 0;
			for(Item item : ingredients){
				if(item.getClass() == ChargrilledMeat.class){
					meatCount += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != 2) return false;

			int beverageCount = 0;
			for(Item item : ingredients){
				if(item.getClass() == PotionOfBeverage.class && item.isIdentifiedForAutomatic()){
					beverageCount += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != ingredients.size()) return false;

			for(int i = 0;i < temp.size() ; i++){
				ingredients.set(i, temp.get(i));
			}

			if (foodCount >= foodNeed
					&& meatCount >= meatNeed
					&& beverageCount >= beverageNeed){
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


			return new SpecialOnigiri().quantity(3);
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			return new SpecialOnigiri().quantity(3);
		}
	}
	*/
	public static class OnigiriSlicing extends Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{BigOnigiri.class};
			inQuantity = new int[]{1};

			cost = 0;

			output = Food.class;
			outQuantity = 2;
		}

	}

	public static class OnigiriProcess extends Recipe.SimpleRecipe {

		{
			inputs =  new Class[]{Food.class, ChargrilledMeat.class, PotionOfBeverage.class};
			inQuantity = new int[]{1, 1, 1};

			cost = 0;

			output = SpecialOnigiri.class;
			outQuantity = 3;
		}

	}
}
