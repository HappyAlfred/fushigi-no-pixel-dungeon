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

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectResistance;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.EarthParticle;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public class EarthImbue extends FlavourBuff {

	public static final float DURATION	= 30f;

	public void proc(Char enemy){
		Buff.affect(enemy, Roots.class, 2);
		CellEmitter.bottom(enemy.pos).start(EarthParticle.FACTORY, 0.05f, 8);
	}

	@Override
	public int icon() {
		return BuffIndicator.ROOTS;
	}
	
	@Override
	public void tintIcon(Image icon) {
		greyIcon(icon, 5f, cooldown());
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}

	{
		resistances.add( new EffectResistance(new EffectType(Paralysis.class), 0) );
		resistances.add( new EffectResistance(new EffectType(Roots.class), 0) );
		resistances.add( new EffectResistance(new EffectType(Slow.class), 0) );
		/*
		immunities.add( new EffectType(Paralysis.class) );
		immunities.add( new EffectType(Roots.class) );
		immunities.add( new EffectType(Slow.class) );
		*/
	}
}