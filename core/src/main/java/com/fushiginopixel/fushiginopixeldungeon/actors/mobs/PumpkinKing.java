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

import com.fushiginopixel.fushiginopixeldungeon.Challenges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Amok;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.sprites.PumpkinKingSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.PumpkinSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class PumpkinKing extends Pumpkin {

	{
		spriteClass = PumpkinKingSprite.class;
		
		HP = HT = 160;
		defenseSkill = 25;
        EXP = 19;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 28, 43 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 52;
	}

    @Override
    public int defenseSkill( Char enemy ) {
        int defenseSkill = super.defenseSkill(enemy);
        return Dungeon.level.solid[pos] ? defenseSkill * 2 : defenseSkill;
    }
	
	@Override
	public int drRoll() {
		return Dungeon.level.solid[pos] ? Random.NormalIntRange(15, 27) : Random.NormalIntRange(0, 12);
	}
}
