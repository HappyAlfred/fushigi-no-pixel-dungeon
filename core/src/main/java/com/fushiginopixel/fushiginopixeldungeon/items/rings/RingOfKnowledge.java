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

package com.fushiginopixel.fushiginopixeldungeon.items.rings;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.watabou.utils.Bundle;

public class RingOfKnowledge extends Ring {

	private static float partialExp = 0f;
	
	@Override
	protected RingBuff buff( ) {
		return new Knowledge();
	}

	public static int expAdapt( Char target , int amount){
		float multiplier = 1;
		if(getBonus(target, Knowledge.class) > 5)
			multiplier *= 2f;
		else
			multiplier *= (float)Math.pow(1.1, getBonus(target, Knowledge.class));

		amount *= multiplier;
		partialExp += amount;
		int exp = 0;
		if (partialExp >= 1){
			exp += (int)partialExp;
			partialExp -= (int)partialExp;
		}
		return exp;
	}

		private static final String PARTIALEXP 	= "partialExp";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle(bundle);
		bundle.put( PARTIALEXP, partialExp );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		partialExp = bundle.getFloat(PARTIALEXP);
	}

	
	public class Knowledge extends RingBuff {
	}
}
