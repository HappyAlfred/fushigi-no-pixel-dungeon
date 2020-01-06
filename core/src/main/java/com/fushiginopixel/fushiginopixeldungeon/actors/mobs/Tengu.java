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
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Poison;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.HeroClass;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.HeroSubClass;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.TomeOfMastery;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.LloydsBeacon;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bomb;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Bombs;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.Firework;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.PrisonBossLevel;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.GrippingTrap;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.MissileSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.TenguSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.BossHealthBar;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Tengu extends Mob {
	
	{
		spriteClass = TenguSprite.class;
		
		HP = HT = 400;
		EXP = 40;
		//defenseSkill = 25;

		HUNTING = new Hunting();

		flying = true; //doesn't literally fly, but he is fleet-of-foot enough to avoid hazards

		properties.add(Property.BOSS);
	}

	public int progressState = 0;
	@Override
	protected void onAdd() {
		//when he's removed and re-added to the fight, his time is always set to now.
		spend(-cooldown());
		super.onAdd();
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 18, 35 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 32;
	}
	*/
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 6);
	}

	@Override
	public int damage(int dmg, Object src,EffectType type) {

		int beforeHitHP = HP;
		int damage = super.damage( dmg, src ,type );
		dmg = beforeHitHP - HP;

		LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
		if (lock != null) {
			int multiple = beforeHitHP > HT/2 ? 1 : 4;
			lock.addTime(damage*multiple);
		}

		//phase 2 of the fight is over
		if (HP == 0 && beforeHitHP <= HT/2) {
			((PrisonBossLevel)Dungeon.level).progress(PrisonBossLevel.State.WON);
			return 0;
		}

		int hpBracket = beforeHitHP > HT/2 ? 24 : 40;

		//phase 1 of the fight is over
		if (beforeHitHP > HT/2 && HP <= HT/2){
			HP = (HT/2)-1;
			yell(Messages.get(this, "interesting"));
			((PrisonBossLevel)Dungeon.level).progress(PrisonBossLevel.State.MAZE);
			BossHealthBar.bleed(true);

		//if tengu has lost a certain amount of hp, jump
		} else if (beforeHitHP / hpBracket != HP / hpBracket) {
			jump();
		}
		return damage;
	}

	@Override
	public boolean isAlive() {
		return Dungeon.level.mobs.contains(this); //Tengu has special death rules, see prisonbosslevel.progress()
	}

	@Override
	public void die( Object cause, EffectType type ) {
		
		if (Dungeon.hero.subClass == HeroSubClass.NONE && Dungeon.hero.heroClass != HeroClass.FUURAI) {
			Dungeon.level.drop( new TomeOfMastery(), pos ).sprite.drop();
		}
		
		GameScene.bossSlain();
		super.die( cause, type );
		
		Badges.validateBossSlain();

		LloydsBeacon beacon = Dungeon.hero.belongings.getItem(LloydsBeacon.class);
		if (beacon != null) {
			beacon.toUpgrade();
		}
		
		yell( Messages.get(this, "defeated") );
	}

	@Override
	protected boolean canAttack( Char enemy ) {
		return new Ballistica( pos, enemy.pos, Ballistica.PROJECTILE).collisionPos == enemy.pos;
	}

	//tengu's attack is always visible
	@Override
	protected boolean doAttack(Char enemy) {
		if (enemy == Dungeon.hero)
			Dungeon.hero.resting = false;
		sprite.attack( enemy.pos );
		spend( totalAttackDelay() );
		return true;
	}

	@Override
	public boolean attack(Char enemy, EffectType type) {
		if(type.isExistAttachType(EffectType.MISSILE)) {
			Ballistica throwing = new Ballistica(pos, enemy.pos, Ballistica.PROJECTILE);
			for (int c : throwing.subPath(1, throwing.dist)) {
				GameScene.add(Blob.seed(c, 3, ToxicGas.class));
			}
		}
		return super.attack(enemy, type);
	}

	private void jump() {
		
		Level level = Dungeon.level;
		
		//incase tengu hasn't had a chance to act yet
		if (fieldOfView == null || fieldOfView.length != Dungeon.level.length()){
			fieldOfView = new boolean[Dungeon.level.length()];
			Dungeon.level.updateFieldOfView( this, fieldOfView );
		}
		
		if (enemy == null) enemy = chooseEnemy();
		if (enemy == null) return;

		int newPos;
		//if we're in phase 1, want to warp around within the room
		if (HP > HT/2) {
			
			//place new traps
			for (int i=0; i < 4; i++) {
				int trapPos;
				do {
					trapPos = Random.Int( level.length() );
				} while (level.map[trapPos] != Terrain.INACTIVE_TRAP
						&& level.map[trapPos] != Terrain.TRAP);
				
				if (level.map[trapPos] == Terrain.INACTIVE_TRAP) {
					level.setTrap( new GrippingTrap().reveal(), trapPos );
					Level.set( trapPos, Terrain.TRAP );
					ScrollOfMagicMapping.discover( trapPos );
				}
			}
			
			int tries = 50;
			boolean bomb = false;
			do {
				newPos = Random.IntRange(3, 7) + 32*Random.IntRange(26, 30);

				for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
					int p = newPos + PathFinder.NEIGHBOURS9[i];
					if (Dungeon.level.heaps.get(p) != null && !Dungeon.level.heaps.get( p ).items.isEmpty() && Dungeon.level.heaps.get( p ).items.getFirst() instanceof Bombs) {
						bomb = true;
						break;
					}else{
                        bomb = false;
                    }
				}
			} while ( (level.adjacent(newPos, enemy.pos)
					&& bomb &&--tries > 0) || Actor.findChar(newPos) != null);
			if (tries <= 0) return;

		//otherwise go wherever, as long as it's a little bit away
		} else {
            boolean bomb = false;
			do {
				newPos = Random.Int(level.length());
                for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
                    int p = newPos + PathFinder.NEIGHBOURS9[i];
                    if (Dungeon.level.heaps.get(p) != null &&!Dungeon.level.heaps.get( p ).items.isEmpty() && Dungeon.level.heaps.get( p ).items.getFirst() instanceof Bombs) {
                        bomb = true;
                        break;
                    }else{
                        bomb = false;
                    }
                }
			} while (
					level.solid[newPos] ||
					level.distance(newPos, enemy.pos) < 8 ||
					Actor.findChar(newPos) != null ||
                    bomb);
		}
		
		if (level.heroFOV[pos]) CellEmitter.get( pos ).burst( Speck.factory( Speck.WOOL ), 6 );

		sprite.move( pos, newPos );
		move( newPos );
		
		if (level.heroFOV[newPos]) CellEmitter.get( newPos ).burst( Speck.factory( Speck.WOOL ), 6 );
		Sample.INSTANCE.play( Assets.SND_PUFF );
		
		spend( 1 / speed() );
	}
	
	@Override
	public void notice() {
		super.notice();
		BossHealthBar.assignBoss(this);
		if (HP <= HT/2) BossHealthBar.bleed(true);
		if (HP == HT) {
			yell(Messages.get(this, "notice_mine", Dungeon.hero.givenName()));
		} else {
			yell(Messages.get(this, "notice_face", Dungeon.hero.givenName()));
		}
	}
	
	{
		//resistances.add( ToxicGas.class );
		resistances.add( new EffectType(EffectType.GAS,0) );
		resistances.add( new EffectType(0,EffectType.POISON) );
	}

	private final String PROGRESS = "progress";

	@Override
	public void storeInBundle( Bundle bundle ) {

		super.storeInBundle( bundle );

		bundle.put( PROGRESS , progressState );
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		BossHealthBar.assignBoss(this);
		if (HP <= HT/2) BossHealthBar.bleed(true);
		progressState = bundle.getInt( PROGRESS );
	}

	private boolean firework(int target)
	{
		final Bomb firework = new Bomb();
		firework.enemyThrow = this;
		final int cell = new Ballistica( this.pos, target, Ballistica.PROJECTILE ).collisionPos;
		this.sprite.zap( cell );

		Sample.INSTANCE.play( Assets.SND_MISS, 0.6f, 0.6f, 1.5f );

		Char enemy = Actor.findChar( cell );

		final float delay = firework.castDelay(this, target);

		if (enemy != null) {
			((MissileSprite) this.sprite.parent.recycle(MissileSprite.class)).
					reset(this.sprite,
							enemy.sprite,
							firework,
							this,
							new Callback() {
								@Override
								public void call() {
									firework.lightThrow(cell, 2f);
								}
							});
		} else {
			((MissileSprite) this.sprite.parent.recycle(MissileSprite.class)).
					reset(this.sprite,
							cell,
							firework,
							this,
							new Callback() {
								@Override
								public void call() {
									firework.lightThrow(cell, 2f);
								}
							});
		}
		return  true;
	}

	//tengu is always hunting
	private class Hunting extends Mob.Hunting{

		@Override
		public boolean act(boolean enemyInFOV, boolean justAlerted) {
			enemySeen = enemyInFOV;
			if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy ) && distance(enemy) > 1) {

				if( Random.Int(6) <= 1){

					firework(enemy.pos);
					spend(TICK);
					return true;
				}else if(distance(enemy) > 2 && Random.Int(6) <= 1 && HP < HT/2){
					ArrayList<Integer> candidates = new ArrayList<>();
					int count = 0;
					for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
						int p = enemy.pos + PathFinder.NEIGHBOURS9[i];
						if (new Ballistica( pos, p, Ballistica.PROJECTILE).collisionPos == p && (Dungeon.level.passable[p] || Dungeon.level.avoid[p])) {
							candidates.add( p );
						}
					}
					for(int i=0;i<3 && candidates.size() > 0;i++){
						int index = Random.index( candidates );
						firework(candidates.remove( index ));
						count++;
					}
					if(count <= 1) {
                        firework(enemy.pos);
					}
                    spend(TICK);
                    return true;
				}
				else return doAttack( enemy );

			} else if(enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {
                return doAttack( enemy );
            }else{
				if (enemyInFOV) {
					target = enemy.pos;
				} else {
					chooseEnemy();
					if (enemy != null) {
						target = enemy.pos;
					}
				}

				spend( TICK );
				return true;

			}
		}
	}
}
