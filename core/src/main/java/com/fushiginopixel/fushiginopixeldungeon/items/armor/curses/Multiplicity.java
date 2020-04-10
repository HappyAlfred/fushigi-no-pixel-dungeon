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

package com.fushiginopixel.fushiginopixeldungeon.items.armor.curses;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mimic;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Statue;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Thief;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.MirrorImage;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Multiplicity extends Armor.Glyph {

	{
		curse = true;
	}

	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );

	@Override
	public float procSufferAttack(Armor armor, Object attacker, Char defender, int damage, EffectType type) {

		if (Random.Int(20) == 0) {
			Char at = (Char) attacker;
			ArrayList<Integer> spawnPoints = new ArrayList<>();

			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = defender.pos + PathFinder.NEIGHBOURS8[i];
				if (Actor.findChar(p) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
					spawnPoints.add(p);
				}
			}

			if (spawnPoints.size() > 0) {

				Mob m = null;
				if (Random.Int(2) == 0 && defender instanceof Hero) {
					m = new MirrorImage();
					((MirrorImage) m).duplicate((Hero) defender);

				} else {
					//FIXME should probably have a mob property for this
					if (at.properties().contains(Char.Property.BOSS) || at.properties().contains(Char.Property.MINIBOSS)
							|| at instanceof Mimic || at instanceof Statue) {
						m = Dungeon.level.createMob();
					} else {
						try {
							Actor.fixTime();

							m = (Mob) at.getClass().newInstance();
							Bundle store = new Bundle();
							at.storeInBundle(store);
							m.restoreFromBundle(store);
							m.HP = m.HT;

							//If a thief has stolen an item, that item is not duplicated.
							if (m instanceof Thief) {
								((Thief) m).item = null;
							}

						} catch (Exception e) {
							Fushiginopixeldungeon.reportException(e);
							m = null;
						}
					}

				}

				if (m != null) {
					GameScene.add(m);
					ScrollOfTeleportation.appear(m, Random.element(spawnPoints));
				}

			}
		}

		return 1;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}
}
