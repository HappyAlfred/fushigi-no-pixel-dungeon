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
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Corruption;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.PinCushion;
import com.fushiginopixel.fushiginopixeldungeon.items.food.MysteryMeat;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CrabSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Random;

public class Crab extends Mob {

	{
		spriteClass = CrabSprite.class;
		
		HP = HT = 40;
		//defenseSkill = 10;
		baseSpeed = 1f;
		
		EXP = 6;
		
		loot = new MysteryMeat();
		lootChance = 0.167f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 4, 11 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 13;
	}
	*/
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}

	@Override
	public void add( Buff buff ) {
		if(buff instanceof PinCushion || buff instanceof Corruption){
			super.add(buff);
		}else damage( 2, buff ,new EffectType(EffectType.BUFF,0));
	}

	@Override
	public int damage( int dmg, Object src ,EffectType type){
		//crab blocks all attacks originating from the hero or enemy characters or traps if it is alerted.
		//All direct damage from these sources is negated, no exceptions. blob/debuff effects go through as normal.
		if ((src instanceof Wand || src instanceof Blob) && dmg > 1){
			if(Dungeon.level.heroFOV[pos] && (type.isExistAttachType(EffectType.MAGICAL_BOLT) || type.isExistAttachType(EffectType.BEAM)))
			GLog.n( Messages.get(this, "guard") );
			return super.damage( 2, src ,type);
		} else {
			return super.damage( dmg, src ,type);
		}
	}
}
