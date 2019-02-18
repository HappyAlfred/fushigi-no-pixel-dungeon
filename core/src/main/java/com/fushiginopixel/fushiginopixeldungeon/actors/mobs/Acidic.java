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

package com.fushiginopixel.fushiginopixeldungeon.actors.mobs;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.AcidicSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Acidic extends GoldenScorpio {

	{
		spriteClass = AcidicSprite.class;
		
		properties.add(Property.ACIDIC);
	}
	
	@Override
	public int defenseProc( Char enemy, int damage, EffectType type  ) {
		
		int dmg = Random.IntRange( 0, damage );
		if (dmg > 0 && type.isExistAttachType(EffectType.MELEE)) {
			enemy.damage( dmg, this,new EffectType(0,EffectType.CORRROSION) );
			if (!enemy.isAlive() && enemy == Dungeon.hero) {
				Dungeon.fail(getClass());
				GLog.n(Messages.capitalize(Messages.get(Char.class, "kill", name)));
			}
		}
		
		return super.defenseProc( enemy, damage, type );
	}
	
}
