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

package com.fushiginopixel.fushiginopixeldungeon.items.stones;

import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.Recipe;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Balance;
import com.fushiginopixel.fushiginopixeldungeon.items.food.Blandfruit;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfPurity;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.Scroll;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.BalanceAttack;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.scenes.PixelScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.ui.RedButton;
import com.fushiginopixel.fushiginopixeldungeon.ui.RenderedTextMultiline;
import com.fushiginopixel.fushiginopixeldungeon.ui.Window;
import com.fushiginopixel.fushiginopixeldungeon.windows.IconTitle;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;

import java.util.ArrayList;

public class StoneOfAugmentation extends InventoryStone {
	
	{
		mode = WndBag.Mode.WEAPON;//WndBag.Mode.ENCHANTABLE;
		image = ItemSpriteSheet.STONE_YNGVI;
	}
	
	@Override
	protected void onItemSelected(Item item) {
		
		GameScene.show(new WndAugment( item));
		
	}
	
	public void apply( Weapon weapon, Weapon.Augment augment ) {
		
		weapon.augment = augment;
		useAnimation();
		ScrollOfUpgrade.upgrade(curUser);
		
	}
	
	/*public void apply( Armor armor, Armor.Augment augment ) {
		
		armor.augment = augment;
		useAnimation();
		ScrollOfUpgrade.upgrade(curUser);
	}*/
	
	@Override
	public int price() {
		return 30 * quantity;
	}
	
	public class WndAugment extends Window {
		
		private static final int WIDTH			= 120;
		private static final int MARGIN 		= 2;
		private static final int BUTTON_WIDTH	= WIDTH - MARGIN * 2;
		private static final int BUTTON_HEIGHT	= 20;
		
		public WndAugment( final Item toAugment ) {
			super();
			
			IconTitle titlebar = new IconTitle( toAugment );
			titlebar.setRect( 0, 0, WIDTH, 0 );
			add( titlebar );
			
			RenderedTextMultiline tfMesage = PixelScene.renderMultiline( Messages.get(this, "choice"), 8 );
			tfMesage.maxWidth(WIDTH - MARGIN * 2);
			tfMesage.setPos(MARGIN, titlebar.bottom() + MARGIN);
			add( tfMesage );
			
			float pos = tfMesage.top() + tfMesage.height();
			
			if (toAugment instanceof Weapon){
				for (final Weapon.Augment aug : Weapon.Augment.values()){
					if (((Weapon) toAugment).augment != aug){
						RedButton btnSpeed = new RedButton( Messages.get(this, aug.name()) ) {
							@Override
							protected void onClick() {
								hide();
								StoneOfAugmentation.this.apply( (Weapon)toAugment, aug );
							}
						};
						btnSpeed.setRect( MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT );
						add( btnSpeed );
						
						pos = btnSpeed.bottom();
					}
				}
				
			}/* else if (toAugment instanceof Armor){
				for (final Armor.Augment aug : Armor.Augment.values()){
					if (((Armor) toAugment).augment != aug){
						RedButton btnSpeed = new RedButton( Messages.get(this, aug.name()) ) {
							@Override
							protected void onClick() {
								hide();
								StoneOfAugmentation.this.apply( (Armor) toAugment, aug );
							}
						};
						btnSpeed.setRect( MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT );
						add( btnSpeed );
						
						pos = btnSpeed.bottom();
					}
				}
			}*/
			
			RedButton btnCancel = new RedButton( Messages.get(this, "cancel") ) {
				@Override
				protected void onClick() {
					hide();
					StoneOfAugmentation.this.collect();
				}
			};
			btnCancel.setRect( MARGIN, pos + MARGIN, BUTTON_WIDTH, BUTTON_HEIGHT );
			add( btnCancel );
			
			resize( WIDTH, (int)btnCancel.bottom() + MARGIN );
		}
		
		@Override
		public void onBackPressed() {
			StoneOfAugmentation.this.collect();
			super.onBackPressed();
		}
	}

	public static class AlchemyEnchantOfBalance extends Recipe {

		public static int equipNeed = 1;
		public static int stoneNeed = 1;
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

			int stoneCount = 0;
			for(Item item : ingredients){
				if(item.getClass() == StoneOfAugmentation.class && item.isIdentifiedForAutomatic()){
					stoneCount += item.quantity();
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
					if(temp.get(0) instanceof Weapon && (((Weapon) temp.get(0)).canEnchant(BalanceAttack.class))){
						type = 1;
					}else if(temp.get(0) instanceof Armor && (((Armor) temp.get(0)).canInscribe(Balance.class))){
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
					&& stoneCount >= stoneNeed
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
			int stone = stoneNeed;
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

				if(ingredients.get(i).getClass() == StoneOfAugmentation.class && ingredients.get(i).quantity() > 0 && stone > 0){
					ingredients.get(i).quantity(ingredients.get(i).quantity() - 1);
					stone --;
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

			if(equipOutput instanceof Weapon && (((Weapon)equipOutput).canEnchant(BalanceAttack.class))){
				return ((Weapon) equipOutput).enchant(new BalanceAttack());
			}else if(equipOutput instanceof Armor && (((Armor)equipOutput).canInscribe(Balance.class))){
				return ((Armor) equipOutput).inscribe(new Balance());
			}
			return null;

		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			if (!testIngredients(ingredients)) return null;

			Item equipOutput = ingredients.get(0);
			if(equipOutput instanceof Weapon && (((Weapon)equipOutput).canEnchant(BalanceAttack.class))){
				return new WndBag.Placeholder(ItemSpriteSheet.WEAPON_HOLDER){
					{
						name = Messages.get(AlchemyEnchantOfBalance.class, "name");
					}

					@Override
					public String info() {
						return Messages.get(AlchemyEnchantOfBalance.class, "desc");
					}
				};
			}else if(equipOutput instanceof Armor && (((Armor)equipOutput).canInscribe(Balance.class))){
				return new WndBag.Placeholder(ItemSpriteSheet.ARMOR_HOLDER){
					{
						name = Messages.get(AlchemyEnchantOfBalance.class, "name");
					}

					@Override
					public String info() {
						return Messages.get(AlchemyEnchantOfBalance.class, "desc_1");
					}
				};
			}else return null;
		}
	}
}
