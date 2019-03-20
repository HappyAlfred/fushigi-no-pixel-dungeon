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

package com.fushiginopixel.fushiginopixeldungeon.items.pots;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.Recipe;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;

import java.util.ArrayList;
import java.util.Iterator;

public class PotOfAlchemy extends InventoryPot {

	{
		initials = 7;

		bones = true;
	}

	@Override
	public boolean clickAble(final Hero curuser, Ballistica shot , final int cell) {
		if(items.size() <= 0 || size <= 0) {
			return super.clickAble(curuser, shot, cell);
		}
		return true;
	}

	@Override
	public boolean click(final Hero curuser, Ballistica shot , final int cell) {
		if(!super.click(curuser, shot, cell)) {
			return false;
		}
		Sample.INSTANCE.play(Assets.SND_BEACON);
		Item output = items.get(items.size() - 1);
		items.remove(output);
		Dungeon.level.drop( output, curuser.pos ).sprite.drop();
		size --;
		curUser.spendAndNext( TIME_TO_ZAP );

		return true;
	}

	@Override
	public void onItemSelected( Item item ) {

		int index = items.indexOf(item);//items.size()-1
		ArrayList<Item> ingredients = new ArrayList<>();
		Recipe recipe = null;
		boolean disappear = false;
		Item result = null;

		/*ArrayList <Item> output= new ArrayList<>();
		if (recipe != null){
			Item output1;
			do{
				output1 = recipe.brew(ingredients);
				if(output1 != null && result == null){
					output.add(output1);
					result = output1;
				}else if(output1 != null && result != null){
					if(output1.stackable && output.get(output.size() - 1).isSimilar(output1)) {
						output.get(output.size() - 1).merge(output1);
					}else{
						output.add(output1);
					}
				}
				updateStack(result);
			}while (items.size() + output.size() <= size && output1 != null);
			//result = recipe.brew(ingredients);
		}*/

		boolean output = false;
		do{
			recipe = null;
		    //find ingredients
            /*
            if size = 5
            i = 0,1,2,3,4
            j max = 4,3,2,1,0
             */
			for(int i=0 ; i <= items.size() - 1 && recipe == null ; i++){
				ingredients.clear();
                //find recipe
				for(int j=0 ; j <= items.size() -1 - i && recipe == null ; j++){
					ingredients.add(items.get(i + j));
					if(Recipe.findRecipe(ingredients) != null){
						recipe = Recipe.findRecipe(ingredients);
					}
				}

			}

			//must have a recipe
			if (recipe == null){
				break;
			}

			//brew
            result = recipe.brew(ingredients);
			updateStack(result);
			if(result != null){
				GLog.i(Messages.get(this, "brew", result));
				if(!output){
					output = true;
				}

				if(result.stackable && !items.isEmpty() && items.get(items.size() - 1) != null && items.get(items.size() - 1).isSimilar(result)) {
                    items.get(items.size() - 1).merge(result);
                }else{
				    items.add(result);
                }
			}else{
                disappear = true;
            }

			//shatter or can't recipe
		}while (items.size() <= size && recipe != null);

		if(output){

			knownByUse();
            if(disappear){
                GLog.i(Messages.get(this, "disappear"));
            }
			if(size < items.size()){
				GLog.n(Messages.get(this,"shatter"));
				detach(curUser.belongings.backpack);
				shatter(curUser.pos);
			}
			/*items.add(result);
			if(size <= items.size()){
				GLog.i(Messages.get(this, "brew", result) + Messages.get(this, "shatter"));
				detach(curUser.belongings.backpack);
				shatter(curUser.pos);
			}else{
				GLog.i(Messages.get(this, "brew", result));
			}*/
		}else if(disappear){
		    //no output item but have recipe
			GLog.i(Messages.get(this, "disappear"));
		}

	}

	public void updateStack(Item result){
		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()){
			Item next = iterator.next();
			if(next.quantity() <= 0 || (!next.stackable && next == result)){
				iterator.remove();
			}
		}

	}

	@Override
	public int price() {
		return super.price() * 3;
	}
}
