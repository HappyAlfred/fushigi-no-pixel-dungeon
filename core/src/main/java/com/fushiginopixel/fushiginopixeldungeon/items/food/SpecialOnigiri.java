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
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Hunger;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bless;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Levitation;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.MindVision;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.watabou.utils.Random;

public class SpecialOnigiri extends Food {

	{
		image = ItemSpriteSheet.SPECIAL_ONIGIRI;
		energy = Hunger.HUNGRY/2f;
	}

	@Override
	public void execute(Char hero, String action ) {

		super.execute( hero, action );

		if (action.equals( AC_EAT )) {
			effect(hero);
		}
	}

	public static void effect(Char hero){
		switch (Random.Int( 4 )) {
			case 0:
				GLog.i( Messages.get(SpecialOnigiri.class, "mindvr") );
				Buff.affect( hero, MindVision.class, MindVision.DURATION, new EffectType(EffectType.INSIDE,0) );
				break;
			case 1:
				GLog.i( Messages.get(SpecialOnigiri.class, "bless") );
				Buff.affect( hero, Bless.class, Bless.DURATION, new EffectType(EffectType.INSIDE,0) );
				break;
			case 2:
				GLog.i( Messages.get(SpecialOnigiri.class, "light") );
				Buff.affect( hero, Levitation.class, Levitation.DURATION, new EffectType(EffectType.INSIDE,0) );
				break;
			case 3:
				GLog.i( Messages.get(FrozenCarpaccio.class, "better") );
				if (hero.HP < hero.HT) {
					hero.HP = Math.min( hero.HP + hero.HT / 4, hero.HT );
					hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				}
				break;
		}
	}
	
	@Override
	public int price() {
		return 10 * quantity;
	}
}
