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
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ParalyticGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Paralysis;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ElmoParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.artifacts.LloydsBeacon;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.CannonBall;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.SkeletonKey;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.Terrain;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.DM450Sprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.DM5000Sprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.MissileSprite;
import com.fushiginopixel.fushiginopixeldungeon.ui.BossHealthBar;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class DM450 extends Mob {
	
	{
		spriteClass = DM450Sprite.class;
		
		HP = HT = 220;
		EXP = 20;
		//defenseSkill = 27;

        HUNTING = new Hunting();

		properties.add(Property.INORGANIC);
        properties.add(Property.MACHANIC);
	}

	public int cooldown = 0;
	public boolean lastBomb = false;
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 30, 60 );
	}
	/*
	@Override
	public int attackSkill( Char target ) {
		return 35;
	}
	*/
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(1, 15);
	}

    @Override
    public boolean act() {

        cooldown --;

        return super.act();
    }

	@Override
	public void die( Object cause, EffectType type ) {
		
		super.die( cause, type );
		
		yell( Messages.get(this, "defeated") );
	}
	
	@Override
	public void notice() {
		super.notice();
		yell( Messages.get(this, "notice") );
	}
	
	/*{
		immunities.add( Terror.class );
	}*/


    private boolean firework(int target)
    {
        final CannonBall firework = new CannonBall();
        firework.enemyThrow = this;
        final int cell = new Ballistica( this.pos, target, Ballistica.MAGIC_BOLT ).collisionPos;
        this.sprite.zap( cell );

        Sample.INSTANCE.play( Assets.SND_CANNON, 0.6f, 0.6f, 1.5f );

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
                                    firework.lightThrow(cell, 0);
                                    next();
                                }
                            });
        }else{
            ((MissileSprite) this.sprite.parent.recycle(MissileSprite.class)).
                    reset(this.sprite,
                            cell,
                            firework,
                            this,
                            new Callback() {
                                @Override
                                public void call() {
                                    firework.lightThrow(cell, 0);
                                    next();
                                }
                            });
        }

        if(Dungeon.hero.HP <= 0){

        }
        return  true;
    }

    @Override
    public boolean canAttack( Char enemy ) {
        boolean canAttack = false;
        for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
            int p = enemy.pos + PathFinder.NEIGHBOURS9[i];
            if(new Ballistica( pos, p, Ballistica.MAGIC_BOLT).collisionPos == p)canAttack = true;
        }
        return canAttack && cooldown <= 0;
    }

    //tengu is always hunting
    private class Hunting extends Mob.Hunting{

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            enemySeen = enemyInFOV;
            if(enemyInFOV) lastBomb = true;
            if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {
                Ballistica attack = new Ballistica( pos, enemy.pos, Ballistica.MAGIC_BOLT);
                if(attack.collisionPos == enemy.pos)firework(enemy.pos);
                else{
                    ArrayList<Integer> canAttack = new ArrayList<>();
                    for (int i = 0; i < PathFinder.NEIGHBOURS9.length; i++) {
                        int p = enemy.pos + PathFinder.NEIGHBOURS9[i];
                        if(new Ballistica( pos, p, Ballistica.MAGIC_BOLT).collisionPos == p)canAttack.add(p);
                    }
                    int p = canAttack.get(Random.Int(canAttack.size()));
                    firework(p);
                }
                spend(TICK);
                cooldown = 3;
                return true;

            }else{

                int oldPos = pos;
                if(target !=-1 && lastBomb && new Ballistica( pos, target, Ballistica.MAGIC_BOLT).collisionPos == target && cooldown <= 0){
                    firework(target);
                    lastBomb = false;
                    spend(TICK);
                    cooldown = 3;
                    return true;
                }

                if (enemyInFOV) {
                    target = enemy.pos;
                } else if (enemy == null) {
                    state = WANDERING;
                    target = Dungeon.level.randomDestination();
                    return true;
                }

                //cannot reload when moving
                if(cooldown > 0){
                    spend( TICK );
                    return true;
                }
                else if (target != -1 && getCloser( target )) {

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
    }

    private final String COOLDOWN = "cannon";
    private final String LASTBOMB = "lastbomb";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );

        bundle.put( COOLDOWN , cooldown );
        bundle.put( LASTBOMB , lastBomb );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        cooldown = bundle.getInt( COOLDOWN );
        lastBomb = bundle.getBoolean( LASTBOMB );
    }


}
