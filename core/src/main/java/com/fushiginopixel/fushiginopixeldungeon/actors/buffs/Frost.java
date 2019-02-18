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

package com.fushiginopixel.fushiginopixeldungeon.actors.buffs;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Thief;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.food.FrozenCarpaccio;
import com.fushiginopixel.fushiginopixeldungeon.items.food.MysteryMeat;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.Potion;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfMight;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfStrength;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.BuffIndicator;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Frost extends FlavourBuff {

	private static final float DURATION	= 5f;

	{
		type = buffType.NEGATIVE;
	}
	
	@Override
	public boolean attachTo( Char target, EffectType type ) {
		if (super.attachTo( target,type )) {
			
			target.paralysed++;
			Buff.detach( target, Burning.class );
			Buff.detach( target, Chill.class );

			if (target instanceof Hero) {

				Hero hero = (Hero)target;
				ArrayList<Item> freezable = new ArrayList<>();
				//does not reach inside of containers
				for (Item i : hero.belongings.backpack.items){
					if ((i instanceof Potion /*&& !(i instanceof PotionOfStrength || i instanceof PotionOfMight)*/)
						|| i instanceof MysteryMeat){
						freezable.add(i);
					}
				}
				
				if (!freezable.isEmpty()){
					Item toFreeze = Random.element(freezable).detach( hero.belongings.backpack );
					if (toFreeze instanceof Potion){
						((Potion) toFreeze).shatter(hero.pos);
					} else if (toFreeze instanceof MysteryMeat){
						FrozenCarpaccio carpaccio = new FrozenCarpaccio();
						if (!carpaccio.collect( hero.belongings.backpack )) {
							Dungeon.level.drop( carpaccio, target.pos ).sprite.drop();
						}
					}
					GLog.w( Messages.get(this, "freezes", toFreeze.toString()) );
				}
				
			} else if (target instanceof Thief) {

				Item item = ((Thief) target).item;

				if (item instanceof Potion /*&& !(item instanceof PotionOfStrength || item instanceof PotionOfMight)*/) {
					((Potion) ((Thief) target).item).shatter(target.pos);
					((Thief) target).item = null;
				} else if (item instanceof MysteryMeat){
					((Thief) target).item = new FrozenCarpaccio();;
				}

			}

			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void detach() {
		super.detach();
		if(target == null || !target.isAlive()){
			return;
		}
		if (target.paralysed > 0)
			target.paralysed--;
		if (Dungeon.level.water[target.pos])
			Buff.prolong(target, Chill.class, 4f,new EffectType(EffectType.BUFF,EffectType.ICE));
	}
	
	@Override
	public int icon() {
		return BuffIndicator.FROST;
	}

	@Override
	public void fx(boolean on) {
		if (on) target.sprite.add(CharSprite.State.FROZEN);
		else target.sprite.remove(CharSprite.State.FROZEN);
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}

	public static float duration( Char ch ) {
		return DURATION;
	}
}
