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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Chill;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.FlavourBuff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Frost;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Roots;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.Splash;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.EarthParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Blazing;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Chilling;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MagesStaff;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class WandOfElements extends DamageWand {

	{
		initials = 4;
	}

	private int elementType = 0;
	public static final int FIRE = 0;
	public static final int ICE = 1;
	public static final int EARTH = 2;
	public static final int WIND = 3;

	public int min(int lvl){
		return 15+lvl;
	}

	public int max(int lvl){
		return 30+5*lvl;
	}

	@Override
	protected void onZap(Ballistica bolt) {

		Heap heap = Dungeon.level.heaps.get(bolt.collisionPos);
		if (heap != null) {
			if(elementType == FIRE){
				heap.burn();
			}else if(elementType == ICE) {
				heap.freeze();
			}
		}

		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null){

			int damage = damageRoll();

			switch (elementType) {
				default:
				case (FIRE):
					processSoulMark(ch, chargesPerCast());
					ch.damage(damageRoll(), this, new EffectType(EffectType.MAGICAL_BOLT,EffectType.FIRE));
					EffectType buffType = new EffectType(EffectType.MAGICAL_BOLT, EffectType.FIRE);
					Buff.affect( ch, Burning.class, buffType ).reignite( buffType );
					break;
				case(ICE):
					if (ch.buff(Frost.class) != null) {
						return; //do nothing, can't affect a frozen target
					}
					if (ch.buff(Chill.class) != null) {
						//7.5% less damage per turn of chill remaining
						float chill = ch.buff(Chill.class).cooldown();
						damage = (int) Math.round(damage * Math.pow(0.9f, chill));
					} else {
						ch.sprite.burst(0xFF99CCFF, level() / 2 + 2);
					}

					processSoulMark(ch, chargesPerCast());
					ch.damage(damage, this, new EffectType(EffectType.MAGICAL_BOLT, EffectType.ICE));

					if (ch.isAlive()) {
						if (Dungeon.level.water[ch.pos])
							Buff.prolong(ch, Chill.class, 4 + level(), new EffectType(EffectType.MAGICAL_BOLT, EffectType.ICE));
						else
							Buff.prolong(ch, Chill.class, 2 + level(), new EffectType(EffectType.MAGICAL_BOLT, EffectType.ICE));
					}
					break;
				case(EARTH):
					CellEmitter.bottom(ch.pos).start(EarthParticle.FACTORY, 0.05f, 8);
					processSoulMark(ch, chargesPerCast());
					ch.damage(damage, this, new EffectType(EffectType.MAGICAL_BOLT, 0));
					Buff.affect(ch, Roots.class, 5);
					break;
				case(WIND):
					CellEmitter.get( ch.pos ).burst( Speck.factory( Speck.JET ), 5 );
					processSoulMark(ch, chargesPerCast());
					ch.damage(damage, this, new EffectType(EffectType.MAGICAL_BOLT, EffectType.AIR));
					Buff.affect(ch, Vertigo.class, 5, new EffectType(EffectType.MAGICAL_BOLT, EffectType.AIR));
					break;
			}
		} else {
			Dungeon.level.press(bolt.collisionPos, null, true);
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		elementType = Random.Int(4);
		int missleType = 0;
		switch (elementType){
			default:
			case(FIRE):
				missleType = MagicMissile.FIRE;break;
			case(ICE):
				missleType = MagicMissile.FROST;break;
			case(EARTH):
				missleType = MagicMissile.FOLIAGE;break;
			case(WIND):
				missleType = MagicMissile.MAGIC_MISSILE;break;
		}
		MagicMissile.boltFromChar(curUser.sprite.parent,
				missleType,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage, EffectType type) {
		switch (Random.Int(4)){
			case(FIRE):
				new Blazing().procInAttack(staff, attacker, defender, damage, type);break;
			case(ICE):
				new Chilling().procInAttack(staff, attacker, defender, damage, type);break;
			case(EARTH): {
				int level = Math.max(0, staff.level());

				if (Random.Int(level / 2 + 100) >= 88) {

					Buff.affect(defender, Roots.class, Random.Float(3f, 7f), new EffectType(type.attachType, 0));
					CellEmitter.bottom(defender.pos).start(EarthParticle.FACTORY, 0.05f, 8);

				}break;
			}case(WIND): {
				int level = Math.max(0, staff.level());

				if (Random.Int(level / 2 + 100) >= 88) {

					Buff.prolong(defender, Vertigo.class, 5, new EffectType(type.attachType, EffectType.AIR));
					CellEmitter.get( defender.pos ).burst( Speck.factory( Speck.JET ), 5 );

				}break;
			}
		}
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {

		particle.color(Random.element(new Integer[]{0xFFFFFF,0x88CCFF,0xEE7722, ColorMath.random(0x004400, 0x88CC44)}));
		particle.am = 0.6f;
		particle.setLifespan(2f);
		float angle = Random.Float(PointF.PI2);
		particle.speed.polar( angle, 2f);
		particle.acc.set( 0f, 1f);
		particle.setSize( 0f, 1.5f);
		particle.radiateXY(Random.Float(1f));
	}

}
