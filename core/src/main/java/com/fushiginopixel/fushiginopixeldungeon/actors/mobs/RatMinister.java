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
import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Drowsy;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.FlavourBuff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.MagicalSleep;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.SkeletonKey;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.RatSceptre;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.properties.Sleepy;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.RatMinisterSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.RatSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class RatMinister extends Rat {

	{
		HP = HT = 120;
		EXP = 10;
		//defenseSkill = 7;
		spriteClass = RatMinisterSprite.class;

		lootChance = 1;

		properties.add(Property.BOSS);
	}

	private int summonCooldown = 10;

	private int delay = 0;

	private final String SUMMON_COOLDOWN = "summon_cooldown";

	@Override
	public boolean act() {

		delay--;

		return super.act();
	}

	//a gold sword
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 4, 10 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 10;
	}
	*/

	@Override
	protected boolean canAttack( Char enemy ) {
		if (canTryToSummon())
				return true;
		else{
			if(delay <= 5){
				return false;
			}
			return Dungeon.level.adjacent(pos, enemy.pos);
		}
	}

	@Override
	protected Item createLoot() {
		Item ratsceptre = new RatSceptre();
		ratsceptre.identify();
		return ratsceptre;
	}

	@Override
	protected boolean getCloser( int target ) {
		if (state == HUNTING && delay <= 5) {
			return enemySeen && getFurther( target );
		} else {
			return super.getCloser( target );
		}
	}

	@Override
	public boolean attack( Char enemy ) {
		if (canAttack(enemy) && canTryToSummon()) {
			summon();
			return true;
		} else if(canAttack(enemy)){
			return super.attack(enemy);
		}
		else return false;
	}

	@Override
	public int attackProc(KindOfWeapon weapon, Char enemy, int damage, EffectType type ) {

		damage = super.attackProc( weapon, enemy, damage, type);
		if (Sleepy.isSuccess(0)) {
			final EffectType effectType = type;
			new FlavourBuff(){
				{actPriority = VFX_PRIO;}
				public boolean act() {
					Buff.affect(target, MagicalSleep.class, new EffectType(effectType.attachType,EffectType.SPIRIT));
					return super.act();
				}
			}.attachTo(enemy);
			enemy.sprite.emitter().burst(Speck.factory(Speck.LIGHT), 12 );

		}
		return damage;
	}

	private boolean canTryToSummon() {

		int ratCount = 0;
		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){

			if (mob instanceof Rat){
				ratCount++;
			}
		}
		if (ratCount < 6 && delay <= 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int damage(int dmg, Object src, EffectType type) {
		int damage = super.damage( dmg, src ,type );
		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null && !isImmune(src.getClass(), new EffectType(0,0))) lock.addTime(damage*1.5f);
		return damage;
	}

	@Override
	public void move( int step ) {
		Dungeon.level.seal();
		super.move( step );
	}

	private void summon() {

		delay = (int)(summonCooldown - (5 - 5 * (float)HP/HT));

		sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.4f, 2 );
		Sample.INSTANCE.play( Assets.SND_CHALLENGE );

		for(int i = 0;i <= (2 + 5 * (1 -(float)HP/HT)) ; i++){
			int newPos = 0;
			do {
				newPos = Random.Int(Dungeon.level.length());
			} while (
					Dungeon.level.solid[newPos] ||
							Dungeon.level.distance(newPos, enemy.pos) < 8 ||
							Actor.findChar(newPos) != null);
			if (Random.Int(10) <= 5){
				Rat rat = new Rat();
				rat.state = rat.WANDERING;
				rat.pos = newPos;
				GameScene.add(rat);
				rat.beckon(pos );

			}else if(Random.Int(5) <= 2){
				WhiteRat rat = new WhiteRat();
				rat.state = rat.WANDERING;
				rat.pos = newPos;
				GameScene.add(rat);
				rat.beckon(pos );
			}else{
				HungryRat rat = new HungryRat();
				rat.state = rat.WANDERING;
				rat.pos = newPos;
				GameScene.add(rat);
				rat.beckon(pos );
			}
		}

		yell( Messages.get(this, "arise") );
	}

	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
		yell( Messages.get(this, "notice") );
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 2);
	}

	@Override
	public void die( Object cause, EffectType type ) {

		super.die( cause, type );

		Dungeon.level.unseal();

		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey( Dungeon.depth ), pos ).sprite.drop();

		yell( Messages.get(this, "defeated") );
	}

	@Override
	public void storeInBundle( Bundle bundle ) {

		super.storeInBundle( bundle );

		bundle.put( SUMMON_COOLDOWN , delay );
	}

	@Override
	public void restoreFromBundle( Bundle bundle ) {

		super.restoreFromBundle( bundle );

		delay = bundle.getInt( SUMMON_COOLDOWN );
		if (state != SLEEPING) BossHealthBar.assignBoss(this);
		if ((HP*2 <= HT)) BossHealthBar.bleed(true);

	}
}
