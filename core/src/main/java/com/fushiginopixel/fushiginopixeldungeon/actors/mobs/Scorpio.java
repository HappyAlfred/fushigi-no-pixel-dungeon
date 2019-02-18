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
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Cripple;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Light;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Weakness;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.food.MysteryMeat;
import com.fushiginopixel.fushiginopixeldungeon.items.potions.PotionOfHealing;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.WeakeningTrap;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ScorpioSprite;
import com.watabou.utils.Random;

public class Scorpio extends Mob {
	
	{
		spriteClass = ScorpioSprite.class;
		
		HP = HT = 35;
		defenseSkill = 16;
		//viewDistance = Light.DISTANCE;
		
		EXP = 9;

		//properties.add(Property.DEMONIC);
	}

	protected int poisonStr = 1;

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 13, 30 );
	}

	@Override
	public int attackSkill( Char target ) {
		return 23;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 6);
	}

	public void poisonAttack(Char enemy, int damage, EffectType type) {
		if (Random.Int( 3 ) == 0 && !enemy.isImmune(getClass(), new EffectType(type.attachType, EffectType.POISON))) {
			if(enemy instanceof Hero){
				Hero hero = (Hero)enemy;
				int amount = Math.min(hero.STR, poisonStr);
				if(amount > 0){
					hero.sprite.showStatus(CharSprite.NEGATIVE, Messages.get(WeakeningTrap.class, "msg_1", amount));
					hero.STR -= amount;
				}
			}else{
				Buff.prolong(enemy, Weakness.class,poisonStr * 20, new EffectType(type.attachType,EffectType.POISON));
			}
		}
	}

	@Override
	public int attackProc( Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( enemy, damage, type );
		poisonAttack(enemy, damage, type);

		return damage;
	}

	/*
	@Override
	public int attackProc( Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( enemy, damage, type );
		if (Random.Int( 2 ) == 0) {
			Buff.prolong( enemy, Cripple.class, Cripple.DURATION,new EffectType(type.attachType,EffectType.POISON) );
		}

		return damage;
	}.

	@Override
	protected boolean canAttack( Char enemy ) {
		Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE);
		return !Dungeon.level.adjacent( pos, enemy.pos ) && attack.collisionPos == enemy.pos;
	}

	@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING) {
			return enemySeen && getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}
	*/
}
