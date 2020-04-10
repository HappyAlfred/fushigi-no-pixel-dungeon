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

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Charm;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Light;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Sleep;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfLullaby;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.CursingTrap;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CurseGirlSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.SuccubusSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CurseGirl extends Mob {
	
	{
		spriteClass = CurseGirlSprite.class;
		
		HP = HT = 36;
		//defenseSkill = 7;
		
		EXP = 5;
		
		loot = new ScrollOfRemoveCurse();
		lootChance = 0.05f;

		HUNTING = new Hunting();
		properties.add(Property.DEMONIC);
	}

	int curseStr = 1;
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 1, 9 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 10;
	}
	*/

	public void cursing( Char enemy ) {
		this.sprite.zap( enemy.pos );
		if(enemy instanceof Hero)
			CursingTrap.curse((Hero)enemy , curseStr, false);
		else{
			EffectType buffType = new EffectType(EffectType.MAGICAL_BOLT,EffectType.DARK);
			Buff.affect(enemy , Weakness.class, buffType).addUp(curseStr, buffType);
		}
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}

	private class Hunting extends Mob.Hunting{
		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;

			if ( enemyInFOV
					&& !isCharmedBy( enemy )
					&& canAttack( enemy )) {
				if(Random.Int(8) == 0){

					cursing(enemy);
					spend(TICK);
					next();
					return true;

				}
				else{
					return super.act(enemyInFOV, justAlerted);
				}
			}
			else {
				return super.act(enemyInFOV, justAlerted);
			}
		}
	}
}
