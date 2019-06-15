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

package com.fushiginopixel.fushiginopixeldungeon.items.wands;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Chill;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.FlavourBuff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Frost;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.EnergyParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.LloydsBeacon;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MagesStaff;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfMagician extends Wand {

	{
		initials = 6;
	}

	private boolean freeCharge = false;

	@Override
	protected void onZap(Ballistica bolt) {

		int cell = bolt.collisionPos;
		Char ch = Actor.findChar(cell);
		if (ch != null && ch != curUser){
			if (curUser.pos == -1 || cell == -1) {

				GLog.w( Messages.get(ScrollOfTeleportation.class, "no_tele") );

			} else if (ch.properties().contains(Char.Property.IMMOVABLE)) {

				GLog.w( Messages.get(LloydsBeacon.class, "tele_fail") );

			} else  {

				int temp = ch.pos;
				ch.pos = curUser.pos;
				curUser.pos = temp;

				curUser.sprite.place(curUser.pos);
				//curUser.sprite.visible = Dungeon.level.heroFOV[curUser.pos];

				ch.sprite.place(ch.pos);
				ch.sprite.visible = Dungeon.level.heroFOV[ch.pos];

				curUser.sprite.emitter().start( Speck.factory(Speck.LIGHT), 0.2f, 3 );
				ch.sprite.emitter().start( Speck.factory(Speck.LIGHT), 0.2f, 3 );
				Sample.INSTANCE.play( Assets.SND_TELEPORT );

				Dungeon.level.press(curUser.pos, curUser);
				Dungeon.level.press(ch.pos, ch);

				if(ch instanceof Hero|| curUser instanceof Hero ) {
					Dungeon.observe();
					GameScene.updateFog();
				}


			}

			processSoulMark(ch, chargesPerCast());
		} else {

			curUser.pos = cell;
			curUser.sprite.place(cell);

			Dungeon.observe();
			GameScene.updateFog();

			Dungeon.level.press(curUser.pos, curUser);
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar(curUser.sprite.parent,
				MagicMissile.BEACON,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage, EffectType type) {
		if (Random.Int( level()/5 + 100 ) >= 90){
			//grants a free use of the staff
			freeCharge = true;
			GLog.p( Messages.get(this, "charged") );
			attacker.sprite.centerEmitter().burst( EnergyParticle.FACTORY, 15 );
		}
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color(0x88CCFF);
		particle.am = 0.6f;
		particle.setLifespan(2f);
		float angle = Random.Float(PointF.PI2);
		particle.speed.polar( angle, 2f);
		particle.acc.set( 0f, 1f);
		particle.setSize( 0f, 1.5f);
		particle.radiateXY(Random.Float(1f));
	}

	private static final String FREECHARGE = "freecharge";

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		freeCharge = bundle.getBoolean( FREECHARGE );
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( FREECHARGE, freeCharge );
	}

}
