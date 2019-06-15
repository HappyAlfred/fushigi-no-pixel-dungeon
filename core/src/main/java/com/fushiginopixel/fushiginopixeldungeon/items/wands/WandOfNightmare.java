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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Amok;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.FlavourBuff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Roots;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.EarthParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ShadowParticle;
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

import java.util.ArrayList;
import java.util.Iterator;

public class WandOfNightmare extends Wand {

	{
		initials = 9;
	}

	private static final ArrayList<Class<? extends Buff>> DEBUFFS = new ArrayList<>();{
		DEBUFFS.add(Terror.class);
		DEBUFFS.add(Amok.class);
		DEBUFFS.add(Vertigo.class);
		DEBUFFS.add(Paralysis.class);
	}

	@Override
	protected void onZap(Ballistica bolt) {

		Char ch = Actor.findChar(bolt.collisionPos);
		if (ch != null){

			ch.sprite.emitter().burst( ShadowParticle.CURSE, 5 + level()/10 );
			processSoulMark(ch, chargesPerCast());
			debuffEnemy(ch,DEBUFFS,new EffectType(EffectType.MAGICAL_BOLT,0));
		} else {
			Dungeon.level.press(bolt.collisionPos, null, true);
		}
	}

	private void debuffEnemy( Char enemy, ArrayList<Class<? extends Buff>> category,EffectType type ){

		//do not consider buffs which are already assigned, or that the enemy is immune to.
		ArrayList<Class<? extends Buff>> debuffs = new ArrayList<>(category);
		for (Buff existing : enemy.buffs()){
			if (debuffs.contains(existing.getClass())) {
				debuffs.remove(existing.getClass());
			}
		}
		if(debuffs.isEmpty()){
			debuffs.addAll(category);
		}
		/*for (Class<?extends Buff> toAssign : debuffs.toArray(new Class[debuffs.size()])){
			if (enemy.isImmune(toAssign,new EffectType(type.attachType,EffectType.SPIRIT))){
				debuffs.remove(toAssign);
			}
		}*/

		Iterator<Class<?extends Buff>> iterator = debuffs.iterator();
		while (iterator.hasNext()){
			Class<?extends Buff> next = iterator.next();
			if(enemy.isImmune(next,new EffectType(type.attachType,EffectType.SPIRIT))){
				iterator.remove();
			}
		}

		Class<?extends FlavourBuff> debuffCls = (Class<? extends FlavourBuff>) Random.element(debuffs);

		if (debuffCls != null){
			Buff.prolong(enemy, debuffCls, 10 + level() / 2,new EffectType(type.attachType,EffectType.SPIRIT));
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.boltFromChar(curUser.sprite.parent,
				MagicMissile.SHADOW,
				curUser.sprite,
				bolt.collisionPos,
				callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage, EffectType type) {
		int level = level();
		if (Random.Int( level / 2 + 100 ) >= 80) {

			debuffEnemy(defender,DEBUFFS,type);
			defender.sprite.emitter().burst( ShadowParticle.CURSE, 5 + level()/10 );

		}
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {

		particle.color(0x110022);
		particle.am = 0.6f;
		particle.setLifespan(2f);
		float angle = Random.Float(PointF.PI2);
		particle.speed.polar( angle, 2f);
		particle.acc.set( 0f, 1f);
		particle.setSize( 0f, 1.5f);
		particle.radiateXY(Random.Float(1f));
	}

}
