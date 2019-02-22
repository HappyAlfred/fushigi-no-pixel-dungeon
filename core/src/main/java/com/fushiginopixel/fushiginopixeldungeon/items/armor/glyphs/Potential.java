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
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite.Glowing;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Potential extends Glyph {
	
	private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF, 0.6f );
	
	@Override
	public float proc( Armor armor, Char attacker, Char defender, int damage , EffectType type ) {

		int level = Math.max( 0, armor.level() );

		/*
		if (defender instanceof Hero) {
			int wands = ((Hero) defender).belongings.charge(0.1f + level*0.05f);
			if (wands > 0) {
				defender.sprite.centerEmitter().burst(EnergyParticle.FACTORY, wands * (level + 2));
			}
		}
		*/

		if (Random.Int( level/2 + 50) >= 40 && type.isExistAttachType(EffectType.MELEE)) {

			affected.clear();
			affected.add(defender);

			arcs.clear();
			//arcs.add(new Lightning.Arc(attacker.sprite.center(), defender.sprite.center()));
			hit(attacker, Random.Int(1, level * 2 + 1), new EffectType(EffectType.MAGICAL_BOLT, EffectType.ELETRIC));

			defender.sprite.parent.addToFront( new Lightning( arcs, null ) );

		}
		
		return 1;
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
		ch.damage(Dungeon.level.water[ch.pos] && !ch.flying ?  2*damage : damage, this,new EffectType(type.attachType,EffectType.ELETRIC));

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
}
