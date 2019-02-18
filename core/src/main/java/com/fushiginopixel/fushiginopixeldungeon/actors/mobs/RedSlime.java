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
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.properties.GildedArmor;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.GildedWeapon;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.features.Door;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.RedSlimeSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.SwarmSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collections;

public class RedSlime extends Slime {

	{
		spriteClass = RedSlimeSprite.class;
		
		HP = HT = 60;
		defenseSkill = 17;

		EXP = 17;

		corrodeStr = 2;
	}
	
	private static final float SPLIT_DELAY	= 1f;
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 2, 7 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 12;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}

	@Override
	public void die( Object cause, EffectType type ) {

		if(type.isExistAttachType(EffectType.MELEE) || type.isExistAttachType(EffectType.MISSILE)){
			destroy();
			((RedSlimeSprite)sprite).knockedFlag = true;
			sprite.die();
		}else {
			super.die(cause, type);
		}
	}

	{
		immunities.add(new EffectType(0,0,Roots.class));
	}

	public void corrodeEnemy(Char enemy, int damage, EffectType type){
		Buff.affect(enemy, Corrosion.class, new EffectType(type.attachType,EffectType.CORRROSION)).set(2f, Random.NormalIntRange( 7, 11 ));
		if(Random.Int(3) == 0){
			if(enemy instanceof Hero) {
				corrodeEquip((Hero) enemy, corrodeStr, 1, false);
			}else{
				Buff.prolong(enemy, Cripple.class,corrodeStr * 5, new EffectType(type.attachType,EffectType.CORRROSION));
				Buff.prolong(enemy, Weakness.class,corrodeStr * 5, new EffectType(type.attachType,EffectType.CORRROSION));
			}
		}

	}

	@Override
	public void damage( int dmg, Object src,EffectType type ) {
		super.damage( dmg, src ,type );

		if (isAlive() && type.isExistAttachType(EffectType.BURST)) {
			split( dmg, src,type );
		}
	}

	public void split( int damage, Object src,EffectType type ) {
		if (HP >= damage + 2 && isAlive()) {
			ArrayList<Integer> candidates = new ArrayList<>();
			boolean[] solid = Dungeon.level.solid;

			int[] neighbours = {pos + 1, pos - 1, pos + Dungeon.level.width(), pos - Dungeon.level.width()};
			for (int n : neighbours) {
				if (!solid[n] && Actor.findChar( n ) == null) {
					candidates.add( n );
				}
			}

			if (candidates.size() > 0) {

				RedSlime clone = split();
				clone.HP = (HP - damage) / 2;
				clone.pos = Random.element( candidates );
				clone.state = clone.HUNTING;

				if (Dungeon.level.map[clone.pos] == Terrain.DOOR) {
					Door.enter( clone.pos );
				}

				GameScene.add( clone, SPLIT_DELAY );
				Actor.addDelayed( new Pushing( clone, pos, clone.pos ), -1 );

				HP -= clone.HP;
			}
		}
	}
	
	private RedSlime split() {
		RedSlime clone = new RedSlime();
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
