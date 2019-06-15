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
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ShadowParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.EquipableItem;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.KindofMisc;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.bags.Bag;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Boomerang;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class CursingTrap extends Trap {

	{
		color = VIOLET;
		shape = WAVES;
	}

	@Override
	public void activate() {
		if (Dungeon.level.heroFOV[ pos ]) {
			CellEmitter.get(pos).burst(ShadowParticle.UP, 5);
			Sample.INSTANCE.play(Assets.SND_CURSED);
		}

		Heap heap = Dungeon.level.heaps.get( pos );
		if (heap != null){
			for (Item item : heap.items){
				if (item.isUpgradable())
					curse(item);
			}
		}

		if (Dungeon.hero.pos == pos){
			curse(Dungeon.hero , 2, true);
		}
	}

	public static void curse(Char hero , int count , boolean equiponly){
		//items the trap wants to curse because it will create a more negative effect
		ArrayList<Item> priorityCurse = new ArrayList<>();
		//items the trap can curse if nothing else is available.
		ArrayList<Item> canCurse = new ArrayList<>();

		if(equiponly) {
			KindOfWeapon weapon = hero.belongings.weapon;
			if (weapon instanceof Weapon && !weapon.cursed && !(weapon instanceof Boomerang)) {
				if (((Weapon) weapon).enchantmentCount() < ((Weapon) weapon).LIMIT)
					priorityCurse.add(weapon);
				else
					canCurse.add(weapon);
			}

			Armor armor = hero.belongings.armor;
			if (armor != null && !armor.cursed) {
				if (armor.glyphCount() < armor.LIMIT)
					priorityCurse.add(armor);
				else
					canCurse.add(armor);
			}

			KindofMisc misc1 = hero.belongings.misc1;
			if (misc1 != null) {
				canCurse.add(misc1);
			}

			KindofMisc misc2 = hero.belongings.misc2;
			if (misc2 != null) {
				canCurse.add(misc2);
			}
		}else{
			for(Item itemhad : hero.belongings) {
				if(itemhad instanceof Weapon && !itemhad.cursed && !(itemhad instanceof Boomerang)){
					if (((Weapon) itemhad).enchantmentCount() < ((Weapon) itemhad).LIMIT)
						priorityCurse.add(itemhad);
					else
						canCurse.add(itemhad);
				}
				if(itemhad instanceof Armor && !itemhad.cursed){
					if (((Armor) itemhad).glyphCount() < ((Armor) itemhad).LIMIT)
						priorityCurse.add(itemhad);
					else
						canCurse.add(itemhad);
				}

				if(itemhad instanceof Bag) {
					for (Item item : hero.belongings.backpack.items) {

						if (item instanceof Weapon && !item.cursed && !(item instanceof Boomerang)) {
							if (((Weapon) item).enchantmentCount() < ((Weapon) item).LIMIT)
								priorityCurse.add(item);
							else
								canCurse.add(item);
						}
						if (item instanceof Armor && !item.cursed) {
							if (((Armor) item).glyphCount() < ((Armor) item).LIMIT)
								priorityCurse.add(item);
							else
								canCurse.add(item);
						}
						if ((item instanceof Wand || item instanceof KindofMisc) && !item.cursed) {
							canCurse.add(item);
						}

					}
				}
			}

		}

		Collections.shuffle(priorityCurse);
		Collections.shuffle(canCurse);

		int numCurses = Random.Int(count) + 1;

		for (int i = 0; i < numCurses; i++){
			if (!priorityCurse.isEmpty()){
				curse(priorityCurse.remove(0));
			} else if (!canCurse.isEmpty()){
				curse(canCurse.remove(0));
			}
		}

		EquipableItem.equipCursed(hero);
	}

	private static void curse(Item item){
		item.cursed = item.cursedKnown = true;

		if (item instanceof Weapon){
			Weapon w = (Weapon) item;
			if (w.enchantmentCount() < w.LIMIT){
				w.enchant(Weapon.Enchantment.randomCurse());
			}
		}
		if (item instanceof Armor){
			Armor a = (Armor) item;
			if (a.glyphCount() < a.LIMIT){
				a.inscribe(Armor.Glyph.randomCurse());
			}
		}
		GLog.n( Messages.get(CursingTrap.class, "curse" , item.name()) );
	}
}
