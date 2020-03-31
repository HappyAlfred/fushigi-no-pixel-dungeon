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

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;

public class Stone extends Armor.Glyph {

	private static ItemSprite.Glowing GREY = new ItemSprite.Glowing( 0x222222 );

	@Override
	public float proc(Armor armor, Object attacker, Char defender, int damage, EffectType type, int event ) {

		if (attacker != null && attacker instanceof Char && event == Armor.EVENT_SUFFER_ATTACK) {
			Char at = (Char) attacker;
			testing = true;
			float evasion = defender.totalDefenseSkill(at);
			float accuracy = at.totalAttackSkill(null, defender);
			testing = false;

			float hitChance;
			if (evasion >= accuracy) {
				hitChance = 1f - (1f - (accuracy / evasion)) / 2f;
			} else {
				hitChance = 1f - (evasion / accuracy) / 2f;
			}

			//60% of dodge chance is applied as damage reduction
			hitChance = (2f + 3f * hitChance) / 5f;

			return hitChance;
		}
		return 1f;
	}
	
	private boolean testing = false;
	
	public boolean testingEvasion(){
		return testing;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return GREY;
	}

}
