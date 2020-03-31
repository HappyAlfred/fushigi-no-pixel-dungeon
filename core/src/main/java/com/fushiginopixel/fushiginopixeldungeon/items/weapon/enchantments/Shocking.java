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

package com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.effects.Lightning;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SparkParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Shocking extends Weapon.Enchantment {

	private static ItemSprite.Glowing WHITE = new ItemSprite.Glowing( 0xFFFFFF, 0.6f );

	@Override
	public float proc( Weapon weapon, Char attacker, Char defender, int damage , EffectType type ) {
		/*
		// lvl 0 - 33%
		// lvl 1 - 50%
		// lvl 2 - 60%
		int level = Math.max( 0, weapon.level() );
		
		if (Random.Int( level + 3 ) >= 2) {
			
			affected.clear();
			affected.add(attacker);

			arcs.clear();
			//arcs.add(new Lightning.Arc(attacker.sprite.center(), defender.sprite.center()));
			hit(defender, Random.Int(1, damage / 3), type);

			if(type.isExistAttachType(EffectType.MELEE)) {
				attacker.sprite.parent.addToFront(new Lightning(arcs, null));
			}else{
				defender.sprite.parent.addToFront(new Lightning(arcs, null));
			}
			
		}
		*/
		affected.clear();
		affected.add(attacker);

		arcs.clear();
		//arcs.add(new Lightning.Arc(attacker.sprite.center(), defender.sprite.center()));
		hit(defender, Random.Int(1, 40), type);

		if(type.isExistAttachType(EffectType.MELEE)) {
			attacker.sprite.parent.addToFront(new Lightning(arcs, null));
		}else{
			defender.sprite.parent.addToFront(new Lightning(arcs, null));
		}

		return 1;

	}

	@Override
	public ItemSprite.Glowing glowing() {
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
