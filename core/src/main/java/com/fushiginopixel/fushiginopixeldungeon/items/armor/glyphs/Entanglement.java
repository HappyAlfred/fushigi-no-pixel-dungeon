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

package com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs;

import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Roots;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.EarthParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor.Glyph;
import com.fushiginopixel.fushiginopixeldungeon.plants.Earthroot;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

public class Entanglement extends Glyph {
	
	private static ItemSprite.Glowing BROWN = new ItemSprite.Glowing( 0x663300 );
	
	@Override
	public float proc(Armor armor, Object attacker, final Char defender, final int damage , EffectType type, int event ) {

		final int level = Math.max( 0, armor.level() );
		
		final int pos = defender.pos;

		if (event == Armor.EVENT_SUFFER_ATTACK) {
			if (Random.Int(4) == 0) {

				Actor delay = new Actor() {

					{
						actPriority = HERO_PRIO + 1;
					}

					@Override
					protected boolean act() {

						Buff.affect(defender, Earthroot.Armor.class).level(4 * (level + 1));
						CellEmitter.bottom(defender.pos).start(EarthParticle.FACTORY, 0.05f, 8);
						Camera.main.shake(1, 0.4f);

						if (defender.buff(Roots.class) != null) {
							Buff.prolong(defender, Roots.class, 5, new EffectType(EffectType.BLOB, 0));
						} else {
							DelayedRoot root = Buff.append(defender, DelayedRoot.class, new EffectType(EffectType.BLOB, 0));
							root.setup(pos);
						}

						Actor.remove(this);
						return true;
					}
				};
				Actor.addDelayed(delay, defender.cooldown());

			}
		}

		return 1;
	}

	@Override
	public Glowing glowing() {
		return BROWN;
	}
	
	public static class DelayedRoot extends Buff{
		
		{
			actPriority = HERO_PRIO-1;
		}
		
		private int pos;
		
		@Override
		public boolean act() {
			
			if (target.pos == pos){
				Buff.prolong( target, Roots.class, 5,new EffectType(EffectType.BLOB,0) );
			}
			
			detach();
			return true;
		}
		
		private void setup( int pos ){
			this.pos = pos;
		}
		
		private static final String POS = "pos";
		
		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			pos = bundle.getInt(POS);
		}
		
		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(POS, pos);
		}
	}
}
