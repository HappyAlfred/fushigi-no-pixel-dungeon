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

import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;

public class PotionOfStrength extends Potion {

	{
		initials = 10;
		bones = true;
	}
	
	@Override
	public void apply( Char hero ) {
		knownByUse();

		if(hero instanceof Hero) {
			if (((Hero)hero).STR < ((Hero)hero).STRMAX) {
				((Hero)hero).STR++;
			} else {
				((Hero)hero).STR++;
				((Hero)hero).STRMAX++;
			}
			hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this, "msg_1"));
			GLog.p(Messages.get(this, "msg_2"));

			Badges.validateStrengthAttained();
		}
	}

	@Override
	public int price() {
		return isKnown() ? 50 * quantity : super.price();
	}
}
