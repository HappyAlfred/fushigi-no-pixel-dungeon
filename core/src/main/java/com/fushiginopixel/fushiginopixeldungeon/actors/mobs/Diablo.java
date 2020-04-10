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
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectResistance;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Fire;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.GooWarn;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Burning;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Chill;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.effects.BlobEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicBreath;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.Splash;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.BlastParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.KindOfWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.IronKey;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.SkeletonKey;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.BallisticaBentLine;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.BallisticaSector;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.DevilSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.DiabloSprite;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.fushiginopixel.fushiginopixeldungeon.ui.BossHealthBar;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class Diablo extends Mob {

	private static final float TIME_TO_ZAP	= 1f;
	{
		spriteClass = DiabloSprite.class;

		HP = HT = 1800;
        EXP = 90;

		HUNTING = new Hunting();

		flying = true;
        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
	}

    private int boltTarget = -1;
    private int boltType = -1;
    public int index = -1;

	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 55, 100 );
	}

	@Override
	public int drRoll() {
		return Random.NormalIntRange(3, 25);
	}

	@Override
	public float attackDelay() {
		return 2f;
	}

    @Override
    public boolean act() {
		if (state != HUNTING || paralysed > 0){
			index = -1;
		}
		if(HP * 2 < HT){
			doomOfFire();
		}

        return super.act();
    }

	public void doomOfFire(){
		ArrayList<Integer> field = new ArrayList<>();
		PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), 3 );
		for (int i = 0; i < PathFinder.distance.length; i++) {
			if (PathFinder.distance[i] < Integer.MAX_VALUE)
				field.add(i);
		}

		final Diablo user = this;
		int ct = Math.min(field.size() / 12, 3 );
		for(int i = 0; i < ct; i++){
			final Integer cell = field.remove(Random.index(field));

			GameScene.add(Blob.seed(cell, 1, Meteor.class));

			Actor.addDelayed(new Actor() {
				{
					//it's a visual effect, gets priority no matter what
					actPriority = MOB_PRIO - 1;
				}

				@Override
				protected boolean act() {

					((MagicMissile)Fushiginopixeldungeon.scene().recycle(MagicMissile.class)).reset(
							MagicMissile.FIRE,
							DungeonTilemap.raisedTileCenterToWorld(cell).offset(0, -DungeonTilemap.SIZE * 10),
							DungeonTilemap.raisedTileCenterToWorld(cell),
							new Callback() {
								@Override
								public void call() {
									user.meteorStrike(cell);
									next();
								}
							}
					);
						Blob rl = Dungeon.level.blobs.get(Meteor.class);
					int value = rl.cur[cell];
					if (value > 0) {
						rl.cur[cell] -= 1;
						rl.volume -= 1;
					}
					Actor.remove(this);
					return false;
				}
			}, 1);
		}
	}

	public void meteorStrike(int cell){
		Sample.INSTANCE.play( Assets.SND_BLAST );
		if (Dungeon.level.heroFOV[cell]) {
			CellEmitter.center( cell ).burst( BlastParticle.FACTORY, 30 );
		}

		for (int n : PathFinder.NEIGHBOURS9) {
			int c = cell + n;
			GameScene.add(Blob.seed(cell, 2, Fire.class));
			if (c >= 0 && c < Dungeon.level.length()) {
				Char ch = Actor.findChar( c );
				if (ch != null && ch != this) {
					//those not at the center of the blast take damage less consistently.

					int dmg = Random.Int(25, 50);
					ch.damage( dmg, this ,new EffectType(EffectType.BURST,EffectType.FIRE));

					if (ch == Dungeon.hero && !ch.isAlive())
						Dungeon.fail( getClass() );
				}
			}
		}
	}

	@Override
	public int attackProc(KindOfWeapon weapon, Char enemy, int damage, EffectType type ) {
		damage = super.attackProc( weapon, enemy, damage, type );
		if(Random.Int(4) == 0) {
			Buff.prolong(enemy, Chill.class, 5, new EffectType(type.attachType, EffectType.ICE));
			Splash.at(enemy.pos, 0xFFD2D6FF, 5);
		}
		return damage;
	}

	public void setBolt(){
		final Ballistica shot = new Ballistica(pos, enemy.pos, Ballistica.STOP_TERRAIN, Ballistica.USING_POSITION);
		((MagicMissile)sprite.parent.recycle(MagicMissile.class)).reset(
				MagicMissile.MAGIC_MISSILE,
				sprite.center(),
				shot.collisionPointF,
				new Callback() {
					@Override
					public void call() {
						next();
					}
				});
        boltType = 1;
        boltTarget = enemy.pos;
        index = 0;
    }

    public boolean boltSpread(){
        if(boltTarget == -1) return true;
        boolean actioned = boltZap();
		spend(TICK);
        return actioned;
    }

    public boolean boltZap(){
        int target = boltTarget;
		index = -1;

		int ct = Math.round (6 -  3f * HP / HT);
        ArrayList<BallisticaBentLine> shots = new ArrayList<>();
        for(int i = 0; i < ct; i++) {
			BallisticaBentLine shot = new BallisticaBentLine(pos, target, 60, 3, Ballistica.MAGIC_BOLT, Ballistica.USING_POSITION);
			if(shot.dist > 0) {
				shots.add(shot);
			}
		}
        if(shots.size() > 0) {
        	//always visible
			((DiabloSprite)sprite).boltZap( shots);
			return false;
        }else{
            return true;
        }
    }

    public void fireBolt(HashSet<Integer> affectedCells) {
		for (int cell : affectedCells) {

			//ignore caster cell
			if (cell == pos) {
				continue;
			}
			GameScene.add(Blob.seed(cell, 4, Fire.class));

			Char ch = Actor.findChar(cell);
			if (ch == null) continue;
			if (ch != null) {
				ch.damage(Random.Int(25, 50), this, new EffectType(EffectType.MAGICAL_BOLT, EffectType.FIRE));
				EffectType buffType = new EffectType(EffectType.MAGICAL_BOLT,EffectType.FIRE);
				Buff.affect(ch, Burning.class, buffType).reignite(buffType);
			}
			if (!ch.isAlive() && ch == Dungeon.hero) {
				Dungeon.fail(getClass());
			}
		}
	}

	public void setBreath(int ct){
		int target;
		boltType = -1;
		if(enemy == null){
			target = boltTarget;
			if(target != -1) {
				next();
			}
		}else{
			target = enemy.pos;
		}
		final Ballistica shot = new Ballistica(pos, target, Ballistica.WONT_STOP);
		int dist = Math.min(shot.dist, 8);
		MagicBreath.breathInLevel(sprite.parent, sprite, shot.pathPointF.get(dist), 30, 0.5f, Ballistica.STOP_TERRAIN, MagicMissile.MAGIC_MISSILE, new Callback() {
			@Override
			public void call() {
				next();
			}
		});
		boltType = 0;
		boltTarget = target;
		index = ct;
	}

	public boolean breathSpread(){
		if(boltTarget == -1) return true;
		boolean actioned = breathZap();
		spend(TICK);
		return actioned;
	}

	public boolean breathZap(){
		int target = boltTarget;
		index -= 1;

		BallisticaSector ballisticaSector = new BallisticaSector(pos, target, 20, 8, Ballistica.STOP_TERRAIN, Ballistica.USING_POSITION);
		//always visible
		((DiabloSprite)sprite).breathZap( ballisticaSector.affectedCells, target);
		return false;
	}

	public void fireBreath(HashSet<Integer> affectedCells) {
		for (int cell : affectedCells) {

			//ignore caster cell
			if (cell == pos) {
				continue;
			}
			Char ch = Actor.findChar(cell);
			if (ch == null) continue;
			if (ch != null) {
				int damage = Random.Int(25, 50);
				ch.damage(damage, this, new EffectType(EffectType.MAGICAL_BOLT, EffectType.FIRE));
				ch.damage(damage, this, new EffectType(EffectType.MAGICAL_BOLT, EffectType.ELETRIC));
			}
			if (!ch.isAlive() && ch == Dungeon.hero) {
				Dungeon.fail(getClass());
			}
		}
	}

	public void setAura(){
		final int ct = 24;
		int wide = 360 / ct;
		int rotation = Random.Int(wide);
		for(int i = 0;i < ct ;i++) {
			float targetAngle = rotation + wide * i;
			final Ballistica shot = new Ballistica(pos, targetAngle, Ballistica.STOP_TERRAIN, Ballistica.USING_ROTATION);

			final int current = i;
			((MagicMissile) sprite.parent.recycle(MagicMissile.class)).reset(
					MagicMissile.MAGIC_MISSILE,
					sprite.center(),
					shot.pathPointF.get(Math.min(shot.dist, 8)),
					new Callback() {
						@Override
						public void call() {
							if (current == ct - 1) {
								next();
							}
						}
					});
		}
		boltType = 2;
		boltTarget = rotation;
		index = 0;
	}

	public boolean auraSpread(){
		if(boltTarget == -1) return true;
		boolean actioned = auraZap();
		spend(TICK);
		return actioned;
	}

	public boolean auraZap(){
		int target = boltTarget;
		index = -1;

		int ct = 24;
		int wide = 360 / ct;
		ArrayList<Ballistica> shots = new ArrayList<>();
		for(int i = 0; i < ct; i++) {
			int rotation = target + wide * i;
			Ballistica shot = new Ballistica(pos, rotation, Ballistica.STOP_TERRAIN, Ballistica.USING_ROTATION);
			if(shot.dist > 0) {
				shots.add(shot);
			}
		}
		if(shots.size() > 0) {
			//always visible
			((DiabloSprite)sprite).auraZap( shots);
			return false;
		}else{
			return true;
		}
	}

	public void fireAura(HashSet<Integer> affectedCells) {
		for (int cell : affectedCells) {

			//ignore caster cell
			if (cell == pos) {
				continue;
			}

			Char ch = Actor.findChar(cell);
			if (ch == null) continue;
			if (ch != null) {
				ch.damage(Random.Int(25, 50), this, new EffectType(EffectType.MAGICAL_BOLT, EffectType.FIRE));
			}
			if (!ch.isAlive() && ch == Dungeon.hero) {
				Dungeon.fail(getClass());
			}
		}
	}

    @Override
    public int damage(int dmg, Object src, EffectType type) {
	    int damage = super.damage(dmg, src, type);
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !isImmune(src.getClass(),type)) lock.addTime(damage*1.5f);
        return damage;
    }

	{
		resistances.add( new EffectResistance(new EffectType(Blindness.class), 0) );
	}

    @Override
    public void die( Object cause, EffectType type ) {

        super.die( cause, type );

        GameScene.bossSlain();
        Dungeon.level.drop( new SkeletonKey( Dungeon.depth  ), pos ).sprite.drop();

        yell( Messages.get(this, "defeated") );
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        yell( Messages.get(this, "notice") );
    }

    private final String BOLT_TARGET = "bolt_target";
	private final String BOLT_TYPE = "bolt_type";
    private final String INDEX = "index";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );
        bundle.put( BOLT_TARGET , boltTarget );
        bundle.put( INDEX , index );
        bundle.put( BOLT_TYPE , boltType );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        boltTarget = bundle.getInt( BOLT_TARGET );
        index = bundle.getInt( INDEX );
        boltType = bundle.getInt( BOLT_TYPE );
        BossHealthBar.assignBoss(this);
    }

	protected class Hunting extends Mob.Hunting {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {

			if(index > -1) {
				switch (boltType) {
					case (0): {
						return breathSpread();
					}
					case (1): {
						return boltSpread();
					}
					case (2): {
						return auraSpread();
					}
				}
				return boltSpread();
			}
			enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy )) {
                if(canAttack(enemy) && Random.Int(2) == 0){
                    return doAttack( enemy );
                }else {
                    Ballistica shot = new Ballistica(pos, enemy.pos, Ballistica.STOP_TERRAIN);
                    int c = Random.Int(4);
                    if (shot.subPath(1, Math.min(shot.dist, 8)).contains(enemy.pos) && c == 0) {
                        c = Random.Int(3);
                        switch (c) {
                            case (0): {
                                setBreath(5);
                                spend(TIME_TO_ZAP);
                                return false;
                            }
                            case (1): {
								setBolt();
                                spend(TIME_TO_ZAP);
                                return false;
                            }
                            case (2): {
                                setAura();
                                spend(TIME_TO_ZAP);
                                return false;
                            }
                        }
                    }
                }
            }
			if (enemyInFOV) {
                target = enemy.pos;
            } else if (enemy == null) {
                state = WANDERING;
                target = Dungeon.level.randomDestination();
                return true;
            }

            int oldPos = pos;
            if (target != -1 && getCloser( target )) {

                spend( 1 / speed() );
                return moveSprite( oldPos,  pos );

            } else {
                spend( TICK );
                if (!enemyInFOV) {
                    sprite.showLost();
                    state = WANDERING;
                    target = Dungeon.level.randomDestination();
                }
                return true;
            }
		}
	}

	public static class Meteor extends Blob{

		@Override
		protected void evolve() {
			int cell;
			for (int i = area.left-1; i <= area.right; i++) {
				for (int j = area.top-1; j <= area.bottom; j++) {
					cell = i + j*Dungeon.level.width();
					if (cur[cell] > 0) {
						off[cell] = cur[cell];
						volume += off[cell];
					} else {
						off[cell] = 0;
					}
				}
			}

		}

		@Override
		public void use( BlobEmitter emitter ) {
			super.use( emitter );
			emitter.start( Speck.factory(Speck.LIGHT), 0.1f, 0 );
		}
	}
}

