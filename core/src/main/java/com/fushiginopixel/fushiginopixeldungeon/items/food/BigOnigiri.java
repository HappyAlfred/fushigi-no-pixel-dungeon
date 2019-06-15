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

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Hunger;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Recharging;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfRecharging;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;

import java.util.Calendar;

public class BigOnigiri extends Food {

	//TODO: implement fun stuff for other holidays
	//TODO: probably should externalize this if I want to add any more festive stuff.
	private enum Holiday{
		NONE,
		DBF, //Dragon Boat Festival, May 27th to June 26th
	}


	private static Holiday holiday;

	public static float TIME_TO_EAT = holiday == Holiday.DBF ? 6f : 0;

	static{

		holiday = Holiday.NONE;

		final Calendar calendar = Calendar.getInstance();
		switch(calendar.get(Calendar.MONTH)){
			case Calendar.MAY:
				if (calendar.get(Calendar.DAY_OF_MONTH) >= 27)
					holiday = Holiday.DBF;
				break;
			case Calendar.JUNE:
				if (calendar.get(Calendar.DAY_OF_MONTH) <= 26)
					holiday = Holiday.DBF;
				break;
		}
	}

	{
		reset();

		energy = Hunger.STARVING;

		bones = true;
	}
	
	@Override
	public void reset() {
		super.reset();
		switch(holiday){
			case NONE:
				name = Messages.get(this, "big_onigiri");
				image = ItemSpriteSheet.BIG_ONIGIRI;
				break;
			case DBF:
				name = Messages.get(this, "chimaki");
				image = ItemSpriteSheet.CHIMAKI;
				break;
		}
	}
	
	@Override
	public void execute(Char hero, String action) {
		super.execute(hero, action);

		if (action.equals(AC_EAT)){
			switch(holiday){
				case NONE:
					break; //do nothing extra
				case DBF:
					//heals for 20% max hp
					hero.HP = Math.min(hero.HP + hero.HT/5, hero.HT);
					hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
					break;
			}
		}
	}

	@Override
	public String info() {
		switch(holiday){
			case NONE: default:
				return Messages.get(this, "big_onigiri_desc");
			case DBF:
				return Messages.get(this, "chimaki_desc");
		}
	}
	
	@Override
	public int price() {
		return 20 * quantity;
	}
}
