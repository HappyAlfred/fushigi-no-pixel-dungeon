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
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ConfusionGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.CorrosiveGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ParalyticGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.StenchGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.TearGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.BlobImmunity;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.Honeypot;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.Recipe;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class PotionOfPurity extends Potion {
	
	private static final int DISTANCE	= 3;
	
	private static ArrayList<Class> affectedBlobs;

	{
		initials = 9;
		
		affectedBlobs = new ArrayList<Class>(){
			{
				add(ConfusionGas.class);
				add(CorrosiveGas.class );
				//add(Electricity.class );
				//add(Fire.class );
				//add(Freezing.class );
				add(ParalyticGas.class );
				//add(Regrowth.class );
				add(StenchGas.class );
				add(ToxicGas.class );
				//add(Web.class );
				add(TearGas.class );
			}
		};
	}

	@Override
	public void shatter( int cell ) {
		
		PathFinder.buildDistanceMap( cell, BArray.not( Dungeon.level.solid, null ), DISTANCE );
		
		ArrayList<Blob> blobs = new ArrayList<>();
		for (Class c : affectedBlobs){
			Blob b = Dungeon.level.blobs.get(c);
			if (b != null && b.volume > 0){
				blobs.add(b);
			}
		}
		
		for (int i=0; i < Dungeon.level.length(); i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE) {
				
				for (Blob blob : blobs) {
					
					int value = blob.cur[i];
					if (value > 0) {
						
						blob.clear(i);
						blob.cur[i] = 0;
						blob.volume -= value;
						
					}
					
				}
				
				if (Dungeon.level.heroFOV[i]) {
					CellEmitter.get( i ).burst( Speck.factory( Speck.DISCOVER ), 2 );
				}
				
			}
		}
		
		
		if (Dungeon.level.heroFOV[cell]) {
			splash(cell);
			Sample.INSTANCE.play(Assets.SND_SHATTER);

			knownByUse();
			GLog.i(Messages.get(this, "freshness"));
		}
		
	}
	
	@Override
	public void apply( Hero hero ) {
		GLog.w( Messages.get(this, "protected") );
		Buff.prolong( hero, BlobImmunity.class, BlobImmunity.DURATION, new EffectType(EffectType.INSIDE,0));
		knownByUse();
	}
	
	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}

	public static class ExtractHoney extends Recipe {

		@Override
		//also sorts ingredients if it can
		public boolean testIngredients(ArrayList<Item> ingredients) {
			if (ingredients.size() != 2) return false;

			if (ingredients.get(0).getClass() == PotionOfPurity.class && ingredients.get(0).isIdentifiedForAutomatic()) {
				if (!(ingredients.get(1) instanceof Honeypot.ShatteredPot)) {
					return false;
				}
			} else if (ingredients.get(0) instanceof Honeypot.ShatteredPot) {
				if (ingredients.get(1).getClass() == PotionOfPurity.class && ingredients.get(0).isIdentifiedForAutomatic()) {
					Item temp = ingredients.get(0);
					ingredients.set(0, ingredients.get(1));
					ingredients.set(1, temp);
				} else {
					return false;
				}
			} else {
				return false;
			}

			PotionOfPurity purity = (PotionOfPurity) ingredients.get(0);
			Honeypot.ShatteredPot pot = (Honeypot.ShatteredPot) ingredients.get(1);

			if (purity.quantity() >= 1
					&& pot.quantity() >= 1) {

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

			ingredients.get(1).quantity(ingredients.get(1).quantity() - 1);

			return new PotionOfBeverage().quantity(1);
		}

		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			return new WndBag.Placeholder(ItemSpriteSheet.POTION_HOLDER){
				{
					name = Messages.get(ExtractHoney.class, "name");
				}

				@Override
				public String info() {
					return Messages.get(ExtractHoney.class, "desc");
				}
			};
		}
	}
}
