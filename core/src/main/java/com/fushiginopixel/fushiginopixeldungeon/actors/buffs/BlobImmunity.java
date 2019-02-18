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

import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ConfusionGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.CorrosiveGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Electricity;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Fire;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Freezing;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ParalyticGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Regrowth;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.StenchGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.TearGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Web;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class BlobImmunity extends FlavourBuff {
	
	public static final float DURATION	= 20f;
	
	@Override
	public int icon() {
		return BuffIndicator.IMMUNITY;
	}
	
	@Override
	public void tintIcon(Image icon) {
		greyIcon(icon, 5f, cooldown());
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	{
		//all harmful blobs
		immunities.add( new EffectType(ConfusionGas.class) );
		immunities.add( new EffectType(CorrosiveGas.class) );
		immunities.add( new EffectType(Electricity.class) );
		immunities.add( new EffectType(Fire.class) );
		immunities.add( new EffectType(Freezing.class) );
		immunities.add( new EffectType(ParalyticGas.class) );
		immunities.add( new EffectType(Regrowth.class) );
		immunities.add( new EffectType(StenchGas.class) );
		immunities.add( new EffectType(ToxicGas.class) );
		immunities.add( new EffectType(Web.class) );
		immunities.add( new EffectType(TearGas.class) );
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}
