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

package com.fushiginopixel.fushiginopixeldungeon.items.bombs;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Vertigo;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.BlastParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SmokeParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Heap;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.RingOfSharpshooting;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CannonBall extends Bombs {


	{
		image = ItemSpriteSheet.CANNONBALL;

		defaultAction = AC_LIGHTTHROW;
		usesTargeting = true;

		stackable = true;

		tier = 2;

		delay = 0f;
	}

	public Fuse fuse;

	public Char enemyThrow;

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions( hero );
		if(!false)
			actions.remove ( AC_LIGHTTHROW );
		return actions;
	}
	@Override
	public void execute(Char hero, String action) {

		if (action.equals(AC_LIGHTTHROW) && false) {
			lightingFuse = true;
			action = AC_THROW;
		} else if(action.equals(AC_LIGHTTHROW)) {
			lightingFuse = false;
			action = AC_THROW;
		} else{
			lightingFuse = false;
		}

		super.execute(hero, action);
	}

	public int proc(Char enemy , int damage){
		return damage;
	}

	public int min(){
		return Dungeon.depth + 5;
	}

	public int max(){
		return (Dungeon.depth+5) * 2;
	}

	public int damageRoll(int minDamage , int maxDamage, Boolean center){
		if(enemyThrow == null){
			return Random.NormalIntRange( center ? minDamage : 1, maxDamage );
		}else{
			int damage = center ? enemyThrow.damageRoll() : enemyThrow.damageRoll() / 2;
			if(enemyThrow == Dungeon.hero){
				damage = Math.max(Random.NormalIntRange( center ? minDamage : 1, maxDamage ) ,center ? enemyThrow.damageRoll() : enemyThrow.damageRoll() / 2);
				damage *= RingOfSharpshooting.damageMultiplier(Dungeon.hero);
			}
			return damage;
		}
		/*
		return enemyThrow == null ?
				Random.NormalIntRange( center ? minDamage : 1, maxDamage ) :
				center ? enemyThrow.damageRoll() : enemyThrow.damageRoll() / 2;
		*/
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public Item random() {
		return this;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return fuse != null ? new ItemSprite.Glowing( 0xFF0000, 0.6f) : null;
	}

	@Override
	public int price() {
		return 5 * quantity;
	}
}
