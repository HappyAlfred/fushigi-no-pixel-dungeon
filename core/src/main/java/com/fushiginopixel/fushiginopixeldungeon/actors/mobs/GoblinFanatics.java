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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.BlastParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SmokeParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bomb;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bombs;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GoblinFanaticsSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.GoblinSapperSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class GoblinFanatics extends GoblinSapper {

	{
		spriteClass = GoblinFanaticsSprite.class;
		
		HP = HT = 190;
		defenseSkill = 30;
		
		EXP = 21;
		
		loot = new Bomb();
		lootChance = 0.05f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 28, 46 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 57;
	}

	protected boolean expAct(){

		if(buff(Burning.class) != null){
			suicide();
		}

		if(countDown != -1){
			countDown --;
			if(countDown <= 0){
				die(this ,new EffectType(EffectType.BURST,0));
				return true;
			}
		}

		return false;

	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 12);
	}

	@Override
	public void die( Object cause, EffectType type ) {

		if(cause instanceof Burning || type.isExistAttachType(EffectType.BURST)){
			selfExplode();

			destroy();
			((GoblinFanaticsSprite)sprite).explodeFlag = true;
			sprite.die();
		}else {
			super.die(cause, type);
		}
	}
	
}
