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

package com.fushiginopixel.fushiginopixeldungeon.items.scrolls;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Recharging;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.SpellSprite;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.EnergyParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MagesStaff;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class ScrollOfRecharging extends InventoryScroll {

	public static final float BUFF_DURATION = 30f;

	{
		initials = 7;
	}

	@Override
	protected void onItemSelected( Item item ) {

		if(!(item instanceof Wand || item instanceof MagesStaff)){
			GLog.i(Messages.get(this, "nothing"));
			return;
		}

		//logic for telling the user when item properties change from upgrades
		//...yes this is rather messy
		Wand w = null;
		if(item instanceof Wand){
			w = (Wand)item;
		}else if(item instanceof MagesStaff){
			w = ((MagesStaff)item).wand;
		}

		if(w != null && w.curCharges < w.maxCharges){
			int chargeCount = Math.min(w.curCharges + w.initialCharges() , w.maxCharges) - w.curCharges;
			w.curCharges += chargeCount;
			GLog.i(Messages.get(this, "recharged",chargeCount));
			charge( curUser );
		}else {
			GLog.i(Messages.get(this, "not_recharged"));
		}
	}
/*
	@Override
	public void doRead() {

		Buff.affect(curUser, Recharging.class, BUFF_DURATION);
		charge(curUser);
		
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();

		GLog.i( Messages.get(this, "surge") );
		SpellSprite.show( curUser, SpellSprite.CHARGE );
		knownByUse();

		readAnimation();
	}
	*/
	
	@Override
	public void empoweredRead() {
		doRead();
		Buff.append(curUser, Recharging.class, BUFF_DURATION/3f);
	}
	
	public static void charge( Hero hero ) {
		hero.sprite.centerEmitter().burst( EnergyParticle.FACTORY, 15 );
	}
	
	@Override
	public int price() {
		return isKnown() ? 40 * quantity : super.price();
	}
}
