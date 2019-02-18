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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Recharging;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Warlock;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.effects.SpellSprite;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MagesStaff;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class WandOfMagicMissile extends DamageWand {

	{
		initials = 8;
	}

	public int min(int lvl){
		return 15+lvl;
	}

	public int max(int lvl){
		return 30+2*lvl;
	}
	
	@Override
	protected void onZap( Ballistica bolt ) {
				
		Char ch = Actor.findChar( bolt.collisionPos );
		if (ch != null) {

			processSoulMark(ch, chargesPerCast());
			ch.damage(damageRoll(), this, new EffectType(EffectType.MAGICAL_BOLT,0));

			ch.sprite.burst(0xFFFFFFFF, level() / 2 + 2);

		} else {
			Dungeon.level.press(bolt.collisionPos, null, true);
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage, EffectType type) {

		int level = Math.max( 0, staff.level() );

		final Ballistica bolt = new Ballistica(attacker.pos,defender.pos,collisionProperties);
		if (Random.Int( level / 2 + 100 ) >= 80 && bolt.collisionPos != attacker.pos) {
			if(defender != null){
				MagicMissile.boltFromChar( attacker.sprite.parent,
						MagicMissile.MAGIC_MISSILE,
						attacker.sprite,
						defender.pos,
						new Callback() {
							@Override
							public void call() {
							}
						} );
				onZap(bolt);
				Sample.INSTANCE.play( Assets.SND_ZAP );
			}
		}
		/*
		Buff.prolong( attacker, Recharging.class, 1 + staff.level()/2f,new EffectType(0,0));
		SpellSprite.show(attacker, SpellSprite.CHARGE);
		*/
	}
	
	public int initialCharges() {
		return super.initialCharges() + 1;
	}

}
