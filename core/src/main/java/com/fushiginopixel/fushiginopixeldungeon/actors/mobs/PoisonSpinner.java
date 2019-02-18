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

package com.fushiginopixel.fushiginopixeldungeon.actors.mobs;

import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Web;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Poison;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.items.food.MysteryMeat;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.PoisonSpinnerSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.SpinnerSprite;
import com.watabou.utils.Random;

public class PoisonSpinner extends Spinner {

	{
		spriteClass = PoisonSpinnerSprite.class;

		HP = HT = 80;
		defenseSkill = 18;

		EXP = 14;

		loot = new MysteryMeat();
		lootChance = 0.125f;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20, 27);
	}

	@Override
	public int attackSkill(Char target) {
		return 36;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 6);
	}

	public void poisonAttack(Char enemy, int damage, EffectType type) {
		if (Random.Int(2) == 0) {
			Buff.affect(enemy, Poison.class,new EffectType(type.attachType,EffectType.POISON)).set(Random.Int(2, 4) );
			state = FLEEING;
		}
	}
}
