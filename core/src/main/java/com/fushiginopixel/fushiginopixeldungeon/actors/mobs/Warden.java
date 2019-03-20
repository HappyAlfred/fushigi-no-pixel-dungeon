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
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Cripple;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.effects.Chains;
import com.fushiginopixel.fushiginopixeldungeon.effects.Pushing;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.PrisonMidBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.WardenSprite;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.fushiginopixel.fushiginopixeldungeon.ui.BossHealthBar;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class Warden extends Mob {

	{
		spriteClass = WardenSprite.class;

		HP = HT = 200;
		defenseSkill = 18;

		EXP = 30;

		properties.add(Property.UNDEAD);
		properties.add(Property.BOSS);
		
		WANDERING = new Wandering();
	}

	private boolean jumpFlag = false;
	public int progressState = 0;

	@Override
	protected void onAdd() {
		//when he's removed and re-added to the fight, his time is always set to now.
		spend(-cooldown());
		super.onAdd();
	}

	@Override
	public boolean act(){

		if(HP <= HT/2 && Random.Int(10) == 0){
			((PrisonMidBossLevel)Dungeon.level).toggleDoor();
			int mobCount = 0;
			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])){

				if (mob instanceof Pumpkin){
					mobCount++;
				}
			}
			if(mobCount < 5 - 5 * 2 * HP / HT){
				int newPos = 0;
				do {
					newPos = Random.Int(Dungeon.level.length());
				} while (
						Dungeon.level.solid[newPos] ||
								Dungeon.level.heroFOV[newPos] ||
								Actor.findChar(newPos) != null);

				Pumpkin pumpkin = new Pumpkin();
				pumpkin.state = pumpkin.WANDERING;
				pumpkin.pos = newPos;
				GameScene.add(pumpkin);
			}
		}

		if(jumpFlag && jump()){
			return true;
		}


		return super.act();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(15, 25);
	}

	@Override
	public int attackSkill( Char target ) {
		return 28;
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 10);
	}

	@Override
	public int attackProc( Char enemy, int damage, EffectType type ) {
		damage = super.attackProc(enemy,damage,type);
		if (Random.Int( 3 ) == 0) {

			Cripple.prolong(enemy, Cripple.class, 4f,new EffectType(type.attachType,0));

		}
		return damage;
	}

	@Override
	public void damage(int dmg, Object src, EffectType type) {

		int beforeHitHP = HP;
		super.damage(dmg, src, type);
		dmg = beforeHitHP - HP;

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) {
			int multiple = beforeHitHP > HT/2 ? 1 : 4;
			lock.addTime(dmg*multiple);
		}

		//phase 2 of the fight is over
		if (HP == 0 && beforeHitHP <= HT/2) {
			((PrisonMidBossLevel)Dungeon.level).progress();
			return;
		}

		int hpBracket = beforeHitHP > HT/2 ? 12 : 20;

		//phase 1 of the fight is over
		if (beforeHitHP > HT/2 && HP <= HT/2){
			HP = (HT/2)-1;
			yell(Messages.get(this, "interesting"));
			((PrisonMidBossLevel)Dungeon.level).progress();
			BossHealthBar.bleed(true);
			jumpFlag = true;

			//if tengu has lost a certain amount of hp, jump
		} else if (beforeHitHP / hpBracket != HP / hpBracket) {
			jumpFlag = true;
		}
	}@Override
	public boolean isAlive() {
		return Dungeon.level.mobs.contains(this); //Tengu has special death rules, see prisonbosslevel.progress()
	}

	@Override
	public void die( Object cause, EffectType type ) {

		GameScene.bossSlain();
		super.die( cause, type );

		yell( Messages.get(this, "defeated") );
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return (new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos && distance(enemy)<=3);
	}

	//tengu's attack is always visible
	@Override
	protected boolean doAttack(Char enemy) {
		if (enemy == Dungeon.hero)
			Dungeon.hero.resting = false;
		sprite.attack( enemy.pos );
		spend( attackDelay() );
		return true;
	}

	private boolean jump() {

		Level level = Dungeon.level;
		jumpFlag = false;

		//incase tengu hasn't had a chance to act yet
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
			Dungeon.level.updateFieldOfView( this, fieldOfView );
		}

		if (enemy == null) enemy = chooseEnemy();
		if (enemy == null) return false;

		int newPos;
		//if we're in phase 1, want to warp around within the room
		if (HP > HT/2) {

			int tries = 50;
			do {
				newPos = Random.IntRange(14, 18) + 33*Random.IntRange(14, 18);
			} while ( (level.adjacent(newPos, enemy.pos) || Actor.findChar(newPos) != null)
					&&--tries > 0);
			if (tries <= 0) return false;

			//otherwise go wherever, as long as it's a little bit away
		} else {
			do {
				newPos = Random.Int(level.length());
			} while (
					level.solid[newPos] ||
							level.distance(newPos, enemy.pos) < 5 ||
							Actor.findChar(newPos) != null);
			((PrisonMidBossLevel)Dungeon.level).toggleDoor();
		}

		final int newPosFinal = newPos;
		final Char warden = this;
		sprite.parent.add(new Chains(this.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(newPos), new Callback() {
			public void call() {
				Actor.addDelayed(new Pushing(warden, pos, newPosFinal, new Callback(){
					public void call() {
						pos = newPosFinal;
						Dungeon.level.press(newPosFinal, warden, true);
						((Warden)warden).spend( 1 / speed() );
						warden.next();

					}
				}),-1);
			}
			}));
		return false;
	}

	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
		if (HP <= HT/2) BossHealthBar.bleed(true);
		if (HP == HT) yell(Messages.get(this, "notice_mine"));
	}

	private static final String JUMPFLAG	= "jumpflag";
	private final String PROGRESS = "progress";

	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( JUMPFLAG, jumpFlag );
		bundle.put( PROGRESS, progressState );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
		jumpFlag = bundle.getBoolean( JUMPFLAG );
		progressState = bundle.getInt( PROGRESS );
		if (HP <= HT/2) BossHealthBar.bleed(true);
	}

	protected class Wandering extends Mob.Wandering {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV) {

				enemySeen = true;

				noticeByMyself();
				alerted = true;
				state = HUNTING;
				target = enemy.pos;

				if (Dungeon.isChallenged( Challenges.SWARM_INTELLIGENCE )) {
					for (Mob mob : Dungeon.level.mobs) {
						if (Dungeon.level.distance(pos, mob.pos) <= 8 && mob.state != mob.HUNTING) {
							mob.beckon( target );
						}
					}
				}

			} else {

				enemySeen = false;

				int oldPos = pos;
				if (target != -1 && getCloser( target )) {
					spend( 1 / speed() );
					return moveSprite( oldPos, pos );
				} else {
					target = Dungeon.level.randomDestination();
					spend( TICK );
				}

			}
			return true;
		}
	}
}
