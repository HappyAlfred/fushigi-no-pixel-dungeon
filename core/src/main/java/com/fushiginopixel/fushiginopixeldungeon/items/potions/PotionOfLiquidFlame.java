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

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Fire;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.Recipe;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Brimstone;
import com.fushiginopixel.fushiginopixeldungeon.items.food.Blandfruit;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.Scroll;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Blazing;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class PotionOfLiquidFlame extends Potion {

	{
		initials = 5;
	}

	@Override
	public void shatter( int cell ) {

		if (Dungeon.level.heroFOV[cell]) {
			knownByUse();

			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
		}

		for (int offset : PathFinder.NEIGHBOURS9){
			if (!Dungeon.level.solid[cell+offset]) {

				GameScene.add(Blob.seed(cell + offset, 2, Fire.class));

			}
		}
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}

	public static class AlchemyEnchantOfFire extends Recipe {

		public static int equipNeed = 1;
		public static int flamePotionNeed = 1;
		public static int scrollNeed = 1;
		public static int miscNeed = 1;
		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			ArrayList<Item> temp = new ArrayList<>();

			int equipCount = 0;
			for(Item item : ingredients){
				if((item instanceof Weapon || item instanceof Armor) && item.isIdentifiedForAutomatic()){
					equipCount += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != 1) return false;

			int flamePotionCount = 0;
			for(Item item : ingredients){
				if(item.getClass() == PotionOfLiquidFlame.class && item.isIdentifiedForAutomatic()){
					flamePotionCount += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != 2) return false;

			int scrollCount = 0;
			for(Item item : ingredients){
				if(item instanceof Scroll){
					scrollCount += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != 3) return false;

			int miscCount = 0;
			int type = 0;
			for(Item item : ingredients){
				if(item instanceof Blandfruit || (item.getClass() == PotionOfPurity.class && item.isIdentifiedForAutomatic())){
					if(temp.get(0) instanceof Weapon && (((Weapon) temp.get(0)).canEnchant(Blazing.class))){
						type = 1;
					}else if(temp.get(0) instanceof Armor && (((Armor) temp.get(0)).canInscribe(Brimstone.class))){
						type = 2;
					}
					miscCount += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != ingredients.size() || type == 0) return false;

			for(int i = 0;i < temp.size() ; i++){
				ingredients.set(i, temp.get(i));
			}

			if (equipCount >= equipNeed
					&& flamePotionCount >= flamePotionNeed
					&& scrollCount >= scrollNeed
					&& miscCount >= miscNeed){
				return true;
			}

			return false;
		}

		@Override
		public int cost(ArrayList<Item> ingredients) {
			return 4;
		}

		@Override
		public Item brew(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			int equip = equipNeed;
			int flamePotion = flamePotionNeed;
			int scroll = scrollNeed;
			int misc = miscNeed;
			Item equipOutput = null;
			for(int i=0 ; i<ingredients.size() ; i++){
				if((ingredients.get(i) instanceof Weapon || ingredients.get(i) instanceof Armor) && ingredients.get(i).quantity() > 0 && equip > 0){
					equipOutput = ingredients.get(i);
					equip --;
					i--;
					continue;
				}

				if(ingredients.get(i).getClass() == PotionOfLiquidFlame.class && ingredients.get(i).quantity() > 0 && flamePotion > 0){
					ingredients.get(i).quantity(ingredients.get(i).quantity() - 1);
					flamePotion --;
					i--;
					continue;
				}

				if(ingredients.get(i) instanceof Scroll && ingredients.get(i).quantity() > 0 && scroll > 0){
					ingredients.get(i).quantity(ingredients.get(i).quantity() - 1);
					scroll --;
					i--;
					continue;
				}

				if((ingredients.get(i) instanceof Blandfruit || ingredients.get(i).getClass() == PotionOfPurity.class) && ingredients.get(i).quantity() > 0 && misc > 0){
					ingredients.get(i).quantity(ingredients.get(i).quantity() - 1);
					misc --;
					i--;
					continue;
				}
			}

			if(equipOutput instanceof Weapon && (((Weapon)equipOutput).canEnchant(Blazing.class))){
				return ((Weapon) equipOutput).enchant(new Blazing());
			}else if(equipOutput instanceof Armor && (((Armor)equipOutput).canInscribe(Brimstone.class))){
				return ((Armor) equipOutput).inscribe(new Brimstone());
			}
			return null;

		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			Item equipOutput = ingredients.get(0);
			if(equipOutput instanceof Weapon && (((Weapon)equipOutput).canEnchant(Blazing.class))){
				return new WndBag.Placeholder(ItemSpriteSheet.WEAPON_HOLDER){
					{
						name = Messages.get(AlchemyEnchantOfFire.class, "name");
					}

					@Override
					public String info() {
						return Messages.get(AlchemyEnchantOfFire.class, "desc");
					}
				};
			}else if(equipOutput instanceof Armor && (((Armor)equipOutput).canInscribe(Brimstone.class))){
				return new WndBag.Placeholder(ItemSpriteSheet.ARMOR_HOLDER){
					{
						name = Messages.get(AlchemyEnchantOfFire.class, "name");
					}

					@Override
					public String info() {
						return Messages.get(AlchemyEnchantOfFire.class, "desc_1");
					}
				};
			}else return null;
		}
	}

	public static class Neutralization extends Recipe {

		public static int flamePotionNeed = 1;
		public static int frostPotionNeed = 1;
		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			ArrayList<Item> temp = new ArrayList<>();

			int flamePotionCount = 0;
			for(Item item : ingredients){
				if(item.getClass() == PotionOfLiquidFlame.class && item.isIdentifiedForAutomatic()){
					flamePotionCount += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != 1) return false;

			int frostPotionCount = 0;
			for(Item item : ingredients){
				if(item.getClass() == PotionOfFrost.class && item.isIdentifiedForAutomatic()){
					frostPotionCount += item.quantity();
					temp.add(item);
				}
			}
			if(temp.size() != ingredients.size()) return false;

			for(int i = 0;i < temp.size() ; i++){
				ingredients.set(i, temp.get(i));
			}

			if (flamePotionCount >= flamePotionNeed
					&& frostPotionCount >= frostPotionNeed){
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

			int flamePotion = flamePotionNeed;
			int frostPotion = frostPotionNeed;
			for(int i=0 ; i<ingredients.size() ; i++){
				if(ingredients.get(i).getClass() == PotionOfLiquidFlame.class && ingredients.get(i).quantity() > 0 && flamePotion > 0){
					ingredients.get(i).quantity(ingredients.get(i).quantity() - 1);
					flamePotion --;
					i--;
					continue;
				}

				if(ingredients.get(i).getClass() == PotionOfLiquidFlame.class && ingredients.get(i).quantity() > 0 && frostPotion > 0){
					ingredients.get(i).quantity(ingredients.get(i).quantity() - 1);
					frostPotion --;
					i--;
					continue;
				}
			}

			return null;

		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			return new WndBag.Placeholder(ItemSpriteSheet.SOMETHING){
					{
						name = Messages.get(AlchemyEnchantOfFire.class, "name");
					}

					@Override
					public String info() {
						return Messages.get(AlchemyEnchantOfFire.class, "desc");
					}
			};
		}
	}
}
