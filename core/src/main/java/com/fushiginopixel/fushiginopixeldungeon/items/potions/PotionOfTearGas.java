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
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.TearGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;

public class PotionOfTearGas extends Potion {

	{
		initials = 13;
	}

	@Override
	public void shatter( int cell ) {

		if (Dungeon.level.heroFOV[cell]) {
			knownByUse();

			splash( cell );
			Sample.INSTANCE.play( Assets.SND_SHATTER );
		}

		GameScene.add( Blob.seed( cell, 1000, TearGas.class ) );
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
