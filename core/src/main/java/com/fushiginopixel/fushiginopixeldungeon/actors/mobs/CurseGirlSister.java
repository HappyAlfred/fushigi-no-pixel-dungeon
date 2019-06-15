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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.CursingTrap;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CurseGirlSisterSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CurseGirlSprite;
import com.watabou.utils.Random;

public class CurseGirlSister extends CurseGirl {
	
	{
		spriteClass = CurseGirlSisterSprite.class;
		
		HP = HT = 85;
		//defenseSkill = 18;
		
		EXP = 14;
		
		//loot = new ScrollOfRemoveCurse();
		//lootChance = 0.05f;

		//HUNTING = new Hunting();
		//properties.add(Property.DEMONIC);
		curseStr = 2;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 22, 30 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 40;
	}
	*/
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}
}
