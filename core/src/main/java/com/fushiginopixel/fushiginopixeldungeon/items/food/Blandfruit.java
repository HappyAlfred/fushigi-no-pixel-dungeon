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

import com.fushiginopixel.fushiginopixeldungeon.Challenges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.EarthImbue;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.FireImbue;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Hunger;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.ToxicImbue;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.Recipe;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfExperience;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfFrost;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfHealing;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfInvisibility;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfLevitation;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfLiquidFlame;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfMindVision;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfPanacea;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfParalyticGas;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfPurity;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfStrength;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.plants.Plant.Seed;
import com.fushiginopixel.fushiginopixeldungeon.plants.Sungrass;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class Blandfruit extends Food {

	public Potion potionAttrib = null;
	public ItemSprite.Glowing potionGlow = null;

	{
		stackable = true;
		image = ItemSpriteSheet.BLANDFRUIT;

		//only applies when blandfruit is cooked
		energy = Hunger.STARVING;

		bones = true;
	}

	@Override
	public boolean isSimilar( Item item ) {
		if (item instanceof Blandfruit){
			if (potionAttrib == null){
				if (((Blandfruit)item).potionAttrib == null)
					return true;
			} else if (((Blandfruit)item).potionAttrib != null){
				if (((Blandfruit)item).potionAttrib.getClass() == potionAttrib.getClass())
					return true;
			}
		}
		return false;
	}

	@Override
	public void execute(Char hero, String action ) {

		if (action.equals( AC_EAT ) && potionAttrib == null) {

			GLog.w( Messages.get(this, "raw"));
			return;

		}

		super.execute(hero, action);

		if (action.equals( AC_EAT ) && potionAttrib != null){

			if (potionAttrib instanceof PotionOfFrost) {
				GLog.i(Messages.get(this, "ice_msg"));
				FrozenCarpaccio.effect(hero);
			} else if (potionAttrib instanceof PotionOfLiquidFlame){
				GLog.i(Messages.get(this, "fire_msg"));
				Buff.affect(hero, FireImbue.class, new EffectType(EffectType.INSIDE,0)).set(FireImbue.DURATION);
			} else if (potionAttrib instanceof PotionOfToxicGas) {
				GLog.i(Messages.get(this, "toxic_msg"));
				Buff.affect(hero, ToxicImbue.class, new EffectType(EffectType.INSIDE,0)).set(ToxicImbue.DURATION);
			} else if (potionAttrib instanceof PotionOfParalyticGas) {
				GLog.i(Messages.get(this, "para_msg"));
				Buff.affect(hero, EarthImbue.class, EarthImbue.DURATION, new EffectType(EffectType.INSIDE,0));
			} else {
				potionAttrib.apply(hero);
			}

		}
	}

	@Override
	public String desc() {
		if (potionAttrib== null) return super.desc();
		else return Messages.get(this, "desc_cooked");
	}

	@Override
	public int price() {
		return 20 * quantity;
	}

	public Item cook(Seed seed){

		try {
			return imbuePotion((Potion)seed.alchemyClass.newInstance());
		} catch (Exception e) {
			Fushiginopixeldungeon.reportException(e);
			return null;
		}

	}

	public Item imbuePotion(Potion potion){

		potionAttrib = potion;
		potionAttrib.ownedByFruit = true;

		potionAttrib.image = ItemSpriteSheet.BLANDFRUIT;

		if (potionAttrib instanceof PotionOfHealing){
			name = Messages.get(this, "sunfruit");
			potionGlow = new ItemSprite.Glowing( 0x2EE62E );
		} else if (potionAttrib instanceof PotionOfStrength){
			name = Messages.get(this, "rotfruit");
			potionGlow = new ItemSprite.Glowing( 0xCC0022 );
		} else if (potionAttrib instanceof PotionOfParalyticGas){
			name = Messages.get(this, "earthfruit");
			potionGlow = new ItemSprite.Glowing( 0x67583D );
		} else if (potionAttrib instanceof PotionOfInvisibility){
			name = Messages.get(this, "blindfruit");
			potionGlow = new ItemSprite.Glowing( 0xE5D273 );
		} else if (potionAttrib instanceof PotionOfLiquidFlame){
			name = Messages.get(this, "firefruit");
			potionGlow = new ItemSprite.Glowing( 0xFF7F00 );
		} else if (potionAttrib instanceof PotionOfFrost){
			name = Messages.get(this, "icefruit");
			potionGlow = new ItemSprite.Glowing( 0x66B3FF );
		} else if (potionAttrib instanceof PotionOfMindVision){
			name = Messages.get(this, "fadefruit");
			potionGlow = new ItemSprite.Glowing( 0xB8E6CF );
		} else if (potionAttrib instanceof PotionOfToxicGas){
			name = Messages.get(this, "sorrowfruit");
			potionGlow = new ItemSprite.Glowing( 0xA15CE5 );
		} else if (potionAttrib instanceof PotionOfLevitation) {
			name = Messages.get(this, "stormfruit");
			potionGlow = new ItemSprite.Glowing( 0x1C3A57 );
		} else if (potionAttrib instanceof PotionOfPanacea) {
			name = Messages.get(this, "dreamfruit");
			potionGlow = new ItemSprite.Glowing( 0x8E2975 );
		} else if (potionAttrib instanceof PotionOfExperience) {
			name = Messages.get(this, "starfruit");
			potionGlow = new ItemSprite.Glowing( 0xA79400 );
		}

		return this;
	}

	public static final String POTIONATTRIB = "potionattrib";
	
	@Override
	protected void onThrow(int cell) {
		if (Dungeon.level.map[cell] == Terrain.WELL || Dungeon.level.pit[cell]) {
			super.onThrow( cell );
			
		} else if (potionAttrib instanceof PotionOfLiquidFlame ||
				potionAttrib instanceof PotionOfToxicGas ||
				potionAttrib instanceof PotionOfParalyticGas ||
				potionAttrib instanceof PotionOfFrost ||
				potionAttrib instanceof PotionOfLevitation ||
				potionAttrib instanceof PotionOfPurity) {
			
			Dungeon.level.press( cell, null, true );
			potionAttrib.shatter( cell );
			
		} else {
			super.onThrow( cell );
		}
	}
	
	@Override
	public void reset() {
		if (potionAttrib != null)
			imbuePotion(potionAttrib);
		else
			super.reset();
	}
	
	@Override
	public void storeInBundle(Bundle bundle){
		super.storeInBundle(bundle);
		bundle.put( POTIONATTRIB , potionAttrib);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		if (bundle.contains(POTIONATTRIB)) {
			imbuePotion((Potion) bundle.get(POTIONATTRIB));
		}
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return potionGlow;
	}
	
	public static class CookFruit extends Recipe {
		
		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 2) return false;
			
			if (ingredients.get(0) instanceof Blandfruit){
				if (!(ingredients.get(1) instanceof Seed)){
					return false;
				}
			} else if (ingredients.get(0) instanceof Seed){
				if (ingredients.get(1) instanceof Blandfruit){
					Item temp = ingredients.get(0);
					ingredients.set(0, ingredients.get(1));
					ingredients.set(1, temp);
				} else {
					return false;
				}
			} else {
				return false;
			}
			
			Blandfruit fruit = (Blandfruit) ingredients.get(0);
			Seed seed = (Seed) ingredients.get(1);
			
			if (fruit.quantity() >= 1 && fruit.potionAttrib == null
				&& seed.quantity() >= 1 && seed.alchemyClass != null){

				if (Dungeon.isChallenged(Challenges.NO_HEALING)
						&& seed instanceof Sungrass.Seed){
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
			
			
			return new Blandfruit().cook((Seed) ingredients.get(1));
		}
		
		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;
			
			return new Blandfruit().cook((Seed) ingredients.get(1));
		}
	}

}
