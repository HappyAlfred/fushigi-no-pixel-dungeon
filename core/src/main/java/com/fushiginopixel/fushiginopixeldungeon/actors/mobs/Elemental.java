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
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Chill;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Frost;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.Imp;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfLiquidFlame;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ElementalSprite;
import com.watabou.utils.Random;

public class Elemental extends Mob {

	{
		spriteClass = ElementalSprite.class;
		
		HP = HT = 120;
		//defenseSkill = 24;
		
		EXP = 18;
		
		flying = true;
		
		loot = new PotionOfLiquidFlame();
		lootChance = 0.1f;
		
		properties.add(Property.FIERY);
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 26, 42 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 45;
	}
	*/
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 10);
	}

	@Override
	public void rollToDropLoot() {
		Imp.Quest.process( this );

		super.rollToDropLoot();
	}

	@Override
	public int attackProc(KindOfWeapon weapon, Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( weapon, enemy, damage, type );
		if (Random.Int( 2 ) == 0) {
			EffectType buffType = new EffectType(type.attachType,EffectType.FIRE);
			Buff.affect( enemy, Burning.class, buffType ).reignite( buffType );
		}
		
		return damage;
	}
	
	@Override
	public void add( Buff buff ) {
		if (buff instanceof Frost || buff instanceof Chill) {
				if (Dungeon.level.water[this.pos])
					damage( Random.NormalIntRange( HT / 2, HT ), buff, new EffectType(EffectType.BUFF,EffectType.ICE) );
				else
					damage( Random.NormalIntRange( 1, HT * 2 / 3 ), buff, new EffectType(EffectType.BUFF,EffectType.ICE) );
		} else {
			super.add( buff );
		}
	}
	
}
