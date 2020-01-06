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
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bleeding;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Cripple;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.watabou.noosa.audio.Sample;

public class FlashingTrap extends Trap {

	{
		color = GREY;
		shape = STARS;
		durability = MULTIPLE;
	}
	
	@Override
	public void activate() {
		
		Char c = Actor.findChar( pos );
		
		if (c != null) {
			//int damage = Math.max( 0,  (4 + Dungeon.depth) - c.drRoll() );
			//Buff.affect( c, Bleeding.class, new EffectType(EffectType.MELEE,0) ).set( damage );
			Buff.prolong( c, Blindness.class, 10f,new EffectType(EffectType.BEAM,EffectType.LIGHT));
			Buff.prolong( c, Cripple.class, 20f );
			
			if (c instanceof Mob) {
				if (((Mob)c).state == ((Mob)c).HUNTING) ((Mob)c).state = ((Mob)c).WANDERING;
				((Mob)c).beckon( Dungeon.level.randomDestination() );
			}
		}
		
		if (Dungeon.level.heroFOV[pos]) {
			GameScene.flash(0xFFFFFF);
			Sample.INSTANCE.play( Assets.SND_BLAST );
		}
		
	}

}
