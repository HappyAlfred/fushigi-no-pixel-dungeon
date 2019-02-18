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

package com.fushiginopixel.fushiginopixeldungeon.levels.features;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Barkskin;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.HeroSubClass;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.LeafParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Dewdrop;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.glyphs.Camouflage;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.SandalsOfNature;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.plants.BlandfruitBush;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.watabou.utils.Random;

public class HighGrass {

	public static void trample( Level level, int pos, Char ch ) {
		
		Level.set( pos, Terrain.GRASS );
		GameScene.updateMap( pos );
		
		int naturalismLevel = 0;

		if (ch != null) {
			SandalsOfNature.Naturalism naturalism = ch.buff( SandalsOfNature.Naturalism.class );
			if (naturalism != null) {
				if (!naturalism.isCursed()) {
					naturalismLevel = naturalism.itemLevel() + 1;
					naturalism.charge();
				} else {
					naturalismLevel = -1;
				}
			}
		}

		if (naturalismLevel >= 0) {
			// Seed, scales from 1/16 to 1/4
			//adjust 1/12 to 1/4
			if (Random.Int(12 - ((int) (naturalismLevel * 2))) == 0) {
				Item seed = Generator.random(Generator.Category.SEED);

				level.drop(seed, pos).sprite.drop();
			}

			// Dew, scales from 1/6 to 1/3
			//adjust 1/5 to 1/3
			if (Random.Int(10 - naturalismLevel) <= 1) {
				level.drop(new Dewdrop(), pos).sprite.drop();
			}
		}

		int leaves = 4;
		

		if (ch instanceof Hero) {
			Hero hero = (Hero)ch;

			// Barkskin
			if (hero.subClass == HeroSubClass.WARDEN) {
				Buff.affect(ch, Barkskin.class).level(ch.HT / 3);
				leaves += 4;
			}

			//Camouflage
			if (hero.belongings.armor != null && hero.belongings.armor.hasGlyph(Camouflage.class)){
				Buff.affect(hero, Camouflage.Camo.class).set(3 + hero.belongings.armor.level());
				leaves += 4;
			}
		}
		
		CellEmitter.get( pos ).burst( LeafParticle.LEVEL_SPECIFIC, leaves );
		if (Dungeon.level.heroFOV[pos]) Dungeon.observe();
	}
}
