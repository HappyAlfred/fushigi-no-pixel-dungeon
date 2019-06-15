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

package com.fushiginopixel.fushiginopixeldungeon.levels.traps;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Poison;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.darts.PoisonDart;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.sprites.MissileSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class PoisonDartTrap extends Trap {

	{
		color = GREEN;
		shape = CROSSHAIR;
		alwaysVisible = true;
	}
	
	@Override
	public void activate() {
		Char target = Actor.findChar(pos);
		
		//find the closest char that can be aimed at
		if (target == null){
			for (Char ch : Actor.chars()){
				Ballistica bolt = new Ballistica(pos, ch.pos, Ballistica.PROJECTILE);
				if (bolt.collisionPos == ch.pos &&
						(target == null || Dungeon.level.trueDistance(pos, ch.pos) < Dungeon.level.trueDistance(pos, target.pos))){
					target = ch;
				}
			}
		}
		if (target != null) {
			final Char finalTarget = target;
			final PoisonDartTrap trap = this;
			if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[target.pos]) {
				Actor.add(new Actor() {
					
					{
						//it's a visual effect, gets priority no matter what
						actPriority = VFX_PRIO;
					}
					
					@Override
					protected boolean act() {
						final Actor toRemove = this;
						((MissileSprite) Fushiginopixeldungeon.scene().recycle(MissileSprite.class)).
							reset(pos, finalTarget.sprite, new PoisonDart(), trap, new Callback() {
								@Override
								public void call() {
									int dmg = Random.NormalIntRange(1, 4) - finalTarget.drRoll();
									finalTarget.damage(dmg, trap, new EffectType(EffectType.MISSILE,0));
									if (finalTarget == Dungeon.hero && !finalTarget.isAlive()){
										Dungeon.fail( trap.getClass() );
									}
									Buff.affect( finalTarget, Poison.class, new EffectType(EffectType.MISSILE,EffectType.POISON) )
											.set( 4 + Dungeon.depth );
									Sample.INSTANCE.play(Assets.SND_HIT, 1, 1, Random.Float(0.8f, 1.25f));
									finalTarget.sprite.bloodBurstA(finalTarget.sprite.center(), dmg);
									finalTarget.sprite.flash();
									Actor.remove(toRemove);
									next();
								}
							});
						return false;
					}
				});
			} else {
				finalTarget.damage(Random.NormalIntRange(1, 4) - finalTarget.drRoll(), trap, new EffectType(EffectType.MISSILE,0));
				Buff.affect( finalTarget, Poison.class, new EffectType(EffectType.MISSILE,EffectType.POISON) )
						.set( 4 + Dungeon.depth );
			}
		}
	}
}
