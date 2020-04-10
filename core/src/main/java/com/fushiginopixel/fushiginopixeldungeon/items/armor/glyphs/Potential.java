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

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Lightning;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.EnergyParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SparkParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor.Glyph;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite.Glowing;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Potential extends Glyph {
	
	private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF, 0.6f );
	private int energy = 0;
	
	@Override
	public float procBeforeDamage( Armor armor, Object attacker, Char defender, int damage , EffectType type ) {

		int level = Math.max( 0, armor.level() );

		/*
		if (defender instanceof Hero) {
			int wands = ((Hero) defender).belongings.charge(0.1f + level*0.05f);
			if (wands > 0) {
				defender.sprite.centerEmitter().burst(EnergyParticle.FACTORY, wands * (level + 2));
			}
		}
		*/

		/*if (attacker != null && attacker instanceof Char && event == Armor.EVENT_SUFFER_ATTACK) {
			if (Random.Int(level / 2 + 50) >= 40 && type.isExistAttachType(EffectType.MELEE)) {
				Char at = (Char) attacker;

				affected.clear();
				//affected.add(defender);

				arcs.clear();
				//arcs.add(new Lightning.Arc(attacker.sprite.center(), defender.sprite.center()));
				//hit(at, Random.Int(1, level * 2 + 1), new EffectType(EffectType.MAGICAL_BOLT, EffectType.ELETRIC));
				hit(at, defender);
				onZap(at, level);

				defender.sprite.parent.addToFront(new Lightning(arcs, null));

			}
		}else*/
			if (/*Random.Int(level / 2 + 50) >= 40 &&*/ damage > 0
					&& !type.isExistAttachType(EffectType.REFLECT)
					&& type.isExistEffectType(EffectType.ELETRIC)
					&& (attacker.getClass() == null || !attacker.getClass().isAssignableFrom(getClass()))){

				energy = damage;
				return 0;

			}
		
		return 1;
	}
	@Override
	public void procAfterDamage( Armor armor, Object attacker, Char defender, int damage , EffectType type ) {
		if(energy > 0){
			affected.clear();
			//affected.add(defender);

			arcs.clear();
			int dist;
			if (Dungeon.level.water[defender.pos] && !defender.flying)
				dist = 2;
			else
				dist = 1;

			int en = energy;
			energy = 0;

			PathFinder.buildDistanceMap( defender.pos, BArray.not( Dungeon.level.solid, null ), dist );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE){
					Char n = Actor.findChar( i );
					if (n != null && !affected.contains( n ) && n != defender) {
						hit(n, defender);
						onZapDamage(n, en);
						defender.sprite.parent.addToFront(new Lightning(arcs, null));
						return;
					}
				}
			}

			defender.damage(en, this, new EffectType(EffectType.MAGICAL_BOLT + EffectType.REFLECT,EffectType.ELETRIC));

			CharSprite s = defender.sprite;
			if (s != null && s.parent != null) {
				ArrayList<Lightning.Arc> arcs = new ArrayList<>();
				arcs.add(new Lightning.Arc(new PointF(s.x, s.y + s.height / 2), new PointF(s.x + s.width, s.y + s.height / 2)));
				arcs.add(new Lightning.Arc(new PointF(s.x + s.width / 2, s.y), new PointF(s.x + s.width / 2, s.y + s.height)));
				s.parent.add(new Lightning(arcs, null));

				if (defender == Dungeon.hero) Camera.main.shake( 2, 0.3f );
				s.centerEmitter().burst( SparkParticle.FACTORY, 3 );
				s.flash();
			}

			if (!Dungeon.hero.isAlive()) {
				Dungeon.fail( getClass() );
				GLog.n(Messages.get(this, "ondeath"));
			}
		}

	}

	@Override
	public Glowing glowing() {
		return WHITE;
	}

	private ArrayList<Char> affected = new ArrayList<>();

	private ArrayList<Lightning.Arc> arcs = new ArrayList<>();

	private void hit( Char ch, int damage, EffectType type ) {

		if (damage < 1) {
			return;
		}

		affected.add(ch);
		ch.damage(Dungeon.level.water[ch.pos] && !ch.flying ?  2*damage : damage, this,new EffectType(type.attachType | EffectType.REFLECT,EffectType.ELETRIC));

		ch.sprite.centerEmitter().burst(SparkParticle.FACTORY, 3);
		ch.sprite.flash();

		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			Char n = Actor.findChar( ch.pos + PathFinder.NEIGHBOURS8[i] );
			if (n != null && !affected.contains( n )) {
				arcs.add(new Lightning.Arc(ch.sprite.center(), n.sprite.center()));
				hit(n, Random.Int(damage / 2, damage), type);
			}
		}
	}

	private void hit(Char ch, Char user){

		affected.add( ch );

		int dist;
		if (Dungeon.level.water[ch.pos] && !ch.flying)
			dist = 2;
		else
			dist = 1;

		PathFinder.buildDistanceMap( ch.pos, BArray.not( Dungeon.level.solid, null ), dist );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE){
				Char n = Actor.findChar( i );
				if (n != null && !affected.contains( n ) && n != user) {
					arcs.add(new Lightning.Arc(ch.sprite.center(), n.sprite.center()));
					hit(n, user);
				}
			}
		}

	}

	private void onZap(Char at, int level){
		float multipler = 0.4f + (0.6f/affected.size());
		//if the main target is in water, all affected take full damage
		if (Dungeon.level.water[at.pos]) multipler = 1f;

		int min = 1;
		int max = 1 + 2*level;

		for (Char ch : affected){
			ch.damage(Math.round(Random.NormalIntRange(min, max) * multipler), this, new EffectType(EffectType.MAGICAL_BOLT + EffectType.REFLECT,EffectType.ELETRIC));

			if (ch == Dungeon.hero) Camera.main.shake( 2, 0.3f );
			ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
			ch.sprite.flash();
		}

		if (!Dungeon.hero.isAlive()) {
			Dungeon.fail( getClass() );
			GLog.n(Messages.get(this, "ondeath"));
		}

	}
	private void onZapDamage(Char at, int damage){
		float multipler = 0.4f + (0.6f/affected.size());
		//if the main target is in water, all affected take full damage
		if (Dungeon.level.water[at.pos]) multipler = 1f;

		for (Char ch : affected){
			float multipler1 = (ch instanceof Hero) ? multipler / 4 :multipler;
			ch.damage(Math.round(damage * multipler1), this, new EffectType(EffectType.MAGICAL_BOLT + EffectType.REFLECT,EffectType.ELETRIC));

			if (ch == Dungeon.hero) Camera.main.shake( 2, 0.3f );
			ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
			ch.sprite.flash();
		}

		if (!Dungeon.hero.isAlive()) {
			Dungeon.fail( getClass() );
			GLog.n(Messages.get(this, "ondeath"));
		}

	}

	private static final String ENERGY = "energy";

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		energy = bundle.getInt(ENERGY);
	}

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ENERGY, energy);
	}
}
