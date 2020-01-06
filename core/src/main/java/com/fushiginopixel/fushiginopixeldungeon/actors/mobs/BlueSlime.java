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

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Corrosion;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Corruption;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Cripple;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Poison;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Roots;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Pushing;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.features.Door;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.BlueSlimeSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.RedSlimeSprite;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class BlueSlime extends Slime {

	{
		spriteClass = RedSlimeSprite.class;
		
		HP = HT = 250;
		//defenseSkill = 44;

		EXP = 30;

		corrodeStr = 2;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 3, 9 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 85;
	}
	*/

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}

	@Override
	public void die( Object cause, EffectType type ) {

		if(type.isExistAttachType(EffectType.MELEE) || type.isExistAttachType(EffectType.MISSILE)){
			destroy();
			((BlueSlimeSprite)sprite).knockedFlag = true;
			sprite.die();
		}else {
			super.die(cause, type);
		}
	}

	{
		immunities.add(new EffectType(0,0,Roots.class));
	}

	public void corrodeEnemy(Char enemy, int damage, EffectType type){
		Buff.affect(enemy, Corrosion.class, new EffectType(type.attachType,EffectType.CORRROSION)).set(2f, 6);
		if(Random.Int(3) == 0){
			if(enemy instanceof Hero) {
				corrodeEquip((Hero) enemy, corrodeStr, 1, true);
			}else{
				Buff.prolong(enemy, Cripple.class,corrodeStr * 5, new EffectType(type.attachType,EffectType.CORRROSION));
				Buff.prolong(enemy, Weakness.class,corrodeStr * 5, new EffectType(type.attachType,EffectType.CORRROSION));
			}
		}

	}

	protected BlueSlime split() {
		BlueSlime clone = new BlueSlime();
		clone.generation = generation + 1;
		clone.EXP = 0;
		if (buff( Burning.class ) != null) {
			Buff.affect( clone, Burning.class,new EffectType(0,EffectType.FIRE) ).reignite( clone );
		}
		if (buff( Poison.class ) != null) {
			Buff.affect( clone, Poison.class,new EffectType(0,EffectType.POISON) ).set(2);
		}
		if (buff(Corruption.class ) != null) {
			Buff.affect( clone, Corruption.class,new EffectType(0,EffectType.CORRROSION));
		}
		return clone;
	}
}
