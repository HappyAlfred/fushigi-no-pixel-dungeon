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
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Invisibility;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.BlastParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SmokeParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bombs;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Firework;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class ScrollOfSelfDestruct extends Scroll {

	{
		initials = 15;

		bones = true;
	}
	
	@Override
	public void doRead() {
		
		Sample.INSTANCE.play( Assets.SND_BLAST );
		Invisibility.dispel();

		CellEmitter.center( curUser.pos ).burst( BlastParticle.FACTORY, 20 );

		boolean terrainAffected = false;

		for (int n : PathFinder.NEIGHBOURS9) {
			int c = curUser.pos + n;
			if (c >= 0 && c < Dungeon.level.length()) {
				if (Dungeon.level.heroFOV[c]) {
					CellEmitter.get( c ).burst( SmokeParticle.FACTORY, 3 );
				}
				terrainAffected = Bombs.blowUp(c , 2);

				Char ch = Actor.findChar( c );
				if (ch != null) {
					if(ch instanceof Hero && ch.HP > 1){
						ch.damage( ch.HP - 1, this ,new EffectType(EffectType.BURST,0));
					}
					else if (ch.HP > 0) {
						ch.damage( ch.HP, this,new EffectType(EffectType.BURST,0) );
					}
				}
			}
		}

		if (terrainAffected) {
			Dungeon.observe();
		}

		knownByUse();
		
		readAnimation();

		if (!curUser.isAlive()) {
			Dungeon.fail( getClass() );
			GLog.n( Messages.get(this, "ondeath") );
		}
	}
	
	@Override
	public void empoweredRead() {
	}
	
	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
