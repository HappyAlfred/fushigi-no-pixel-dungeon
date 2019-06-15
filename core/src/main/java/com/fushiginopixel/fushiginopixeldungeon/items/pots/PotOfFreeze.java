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
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Freezing;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.food.FrozenCarpaccio;
import com.fushiginopixel.fushiginopixeldungeon.items.food.MysteryMeat;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfFrost;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfLevitation;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfParalyticGas;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfTearGas;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class PotOfFreeze extends InventoryPot {

	{
		initials = 5;

		bones = true;
		collisionProperties = Ballistica.STOP_TERRAIN;
	}

	@Override
	public boolean clickAble(Char curuser, Ballistica shot, int cell) {
		if(items.size() >= size || size <= 0){
			return super.clickAble(curuser, shot, cell);
		}
		return true;
	}

	@Override
	public boolean click(final Char curuser, Ballistica shot , final int cell) {
		if(!super.click(curuser, shot, cell)) {
			return false;
		}
		Sample.INSTANCE.play(Assets.SND_ZAP);
		for (int c : shot.subPath(1, Math.min(shot.dist, 5))) {
			GameScene.add(Blob.seed(c, 5, Freezing.class));
		}
		size --;
		curUser.spendAndNext( TIME_TO_ZAP );
		knownByUse();

		return true;
	}

	@Override
	public void shatter( int cell ) {
		super.shatter(cell);
		if (Dungeon.level.heroFOV[cell]) {
			knownByUse();
		}
		for (int offset : PathFinder.NEIGHBOURS9){
			if (!Dungeon.level.solid[cell+offset]) {

				GameScene.add(Blob.seed(cell + offset, 10, Freezing.class));

			}
		}
	}

	@Override
	public void onItemSelected( Item item ) {

		if(item instanceof MysteryMeat) {
			Item frozenMeat = new FrozenCarpaccio();
			frozenMeat.quantity(item.quantity());
			GLog.i(Messages.get(this, "freeze", item));
			items.remove(item);
			items.add(frozenMeat);
			knownByUse();
		}else if(item instanceof Potion){
			if(item instanceof PotionOfFrost && isExpandable()){
				GLog.i(Messages.get(this, "expand", item));
				expand(2);
				knownByUse();
			} else if(item instanceof PotionOfFrost || item instanceof PotionOfTearGas || item instanceof PotionOfToxicGas || item instanceof PotionOfParalyticGas || item instanceof PotionOfLevitation) {
				((Potion) item).shatter(curUser.pos);
				GLog.i(Messages.get(this, "shatter", item));
				knownByUse();
			} else{
				GLog.i(Messages.get(this, "shatter_1", item));
			}
			items.remove(item);
		}

	}

	@Override
	public int price() {
		return super.price() * 2;
	}
}
