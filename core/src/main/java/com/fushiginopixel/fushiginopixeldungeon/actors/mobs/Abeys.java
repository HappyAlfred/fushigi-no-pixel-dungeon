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
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.Blob;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.effects.Beam;
import com.fushiginopixel.fushiginopixeldungeon.effects.BlobEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Delay;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.PurpleParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.IronKey;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.AbeysSprite;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.fushiginopixel.fushiginopixeldungeon.ui.BossHealthBar;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Abeys extends Mob {

	private static final float TIME_TO_ZAP	= 1f;
	{
		spriteClass = AbeysSprite.class;
		
		HP = HT = 1200;
        EXP = 70;

		HUNTING = new Hunting();

		flying = true;
        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
	}

	private ArrayList<Float> laserRotations = new ArrayList<>();
    private int laserType = -1;
    public int index = -1;
    private int lastEnemyId = 0;
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 33, 57 );
	}

	/*
	@Override
	public int attackSkill( Char target ) {
		return 42;
	}
	*/
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(3, 25);
	}

	public void zap(Ballistica bolt ) {

		for (int c : bolt.subPath(1, Math.min(bolt.dist, 4))) {
            Char ch = Actor.findChar( c );
            if(ch == null) continue;

            int dmg = Random.Int( 10, 50 );
            ch.damage( dmg, this ,new EffectType(EffectType.MAGICAL_BOLT,EffectType.DARK));
            Buff.affect(ch, Blindness.class, 5);

            if (!ch.isAlive() && ch == Dungeon.hero) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "dark_kill") );
            }

		}
	}

    @Override
    public boolean act() {

        if(index > -1) {
            return laserSpread();
        }

        return super.act();
    }

	public void laserNet(){
	    int ct = (int) (9 -  6f * HP / HT);
	    int type = Random.Int(6);
	    int laserType = type;
        float rotation = Random.Float(0, 360);
	    for(int i = 0;i < ct ;i++){
	        switch (type){
                case 0: {
                    int wide = 30;
                    Point targetVector = Dungeon.level.cellToPoint(pos).vector(Dungeon.level.cellToPoint(enemy.pos));
                    float targetAngle = (float) (targetVector.getVectorAngle() - (ct - 1) / 2f * wide + i * wide);
                    final Ballistica shot = new Ballistica(pos, targetAngle, Ballistica.STOP_TERRAIN, Ballistica.USING_ROTATION);
                    laserRotations.add(targetAngle);
                    sprite.parent.add(new Delay((i + 1) / 2f, new Callback() {
                        public void call() {
                            sprite.parent.add(new Beam.LightRay(sprite.center(), shot.collisionPointF));
                        }
                    }));
                    if(i == ct - 1) {
                        sprite.parent.add(new Delay((i + 1) / 2f, new Callback() {
                            public void call() {
                                next();
                            }
                        }));
                    }
                    break;
                }
                //type 1, 2
                case 1: {
                    laserType = type + ct % 2;
                    int wide = 30;
                    Point targetVector = Dungeon.level.cellToPoint(pos).vector(Dungeon.level.cellToPoint(enemy.pos));
                    float targetAngle = (float) targetVector.getVectorAngle();
                    float left;
                    //ct % 0 == 0, -1 1 -2 2
                    //ct % 0 == 1, 0 -1 1 -2 2
                    int j = ct % 2;
                    if(j == 1 && i == 0){
                        left = 0;
                    }else{
                        left = (1 - j) / 2f + (i + j) / 2;
                        left *=((i + j) % 2 == 0) ? -1 : 1;
                    }
                    targetAngle += left * wide;
                    final Ballistica shot = new Ballistica(pos, targetAngle, Ballistica.STOP_TERRAIN, Ballistica.USING_ROTATION);
                    laserRotations.add(targetAngle);
                    sprite.parent.add(new Delay(((i + j) / 2 + 1) / 2f, new Callback() {
                        public void call() {
                            sprite.parent.add(new Beam.LightRay(sprite.center(), shot.collisionPointF));
                        }
                    }));
                    if(i == ct - 1) {
                        sprite.parent.add(new Delay((i + 1) / 2f, new Callback() {
                            public void call() {
                                next();
                            }
                        }));
                    }
                    break;
                }
                //type 3, 4
                default: {
                    float wide = 360f / ct;
                    int j;
                    //ct = 4, 0 2 1 3
                    //ct = 5, 1 2 3 4 5
                    if(ct % 2 == 0){
                        j = i / 2 + (i % 2) * (ct / 2);
                        laserType = 3;
                    }else{
                        laserType = 4;
                        j = i;
                    }
                    float targetAngle = rotation + wide * j;
                    final Ballistica shot = new Ballistica(pos, targetAngle, Ballistica.STOP_TERRAIN, Ballistica.USING_ROTATION);
                    laserRotations.add(targetAngle);
                    sprite.parent.add(new Delay(ct%2==0 ? (i + 2) / 2 / 2f : (i + 1) / 2f, new Callback() {
                        public void call() {
                            sprite.parent.add(new Beam.LightRay(sprite.center(), shot.collisionPointF));
                        }
                    }));
                    if(i == ct - 1) {
                        sprite.parent.add(new Delay(ct%2==0 ? (i + 2) / 2 / 2f : (i + 1) / 2f, new Callback() {
                            public void call() {
                                next();
                            }
                        }));
                    }
                }
            }
        }
        this.laserType = laserType;
        index = 0;
    }

    public boolean laserSpread(){
	    if(laserRotations.isEmpty()) return true;
	    boolean actioned = false;
	    switch (laserType){
            case (0):
            case (4):{
                actioned |= darkZap();
                if(index != -1) {
                    spend(TICK/2f);
                }else{
                    spend(TICK);
                }
                break;
            }
            case (1):{
                int ct = index == 0 ? 1 : 2;
                for(int i = 0; i < ct; i++){
                    actioned |= darkZap();
                }
                spend(TICK);
                break;
            }
            case (2):{
                actioned |= darkZap();
                spend(TICK);
                break;
            }
            case (3):{
                for(int i = 0; i < 2; i++){
                    actioned |= darkZap();
                }
                spend(TICK);
                break;
            }
            default:
        }
        return actioned;
    }

    public boolean darkZap(){
	    float rotation = laserRotations.get(index);
        index++;
        if(index >= laserRotations.size()){
            laserRotations.clear();
            laserType = -1;
            index = -1;
        }
	    Ballistica shot = new Ballistica(pos, rotation, Ballistica.STOP_TERRAIN, Ballistica.USING_ROTATION);
        if(shot.dist > 0) {
            if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[shot.collisionPos] ) {
                ((AbeysSprite)sprite).darkZap( shot );
                return false;
            } else {
                darkBeam(shot, true);
                return true;
            }
        }else{
            return true;
        }
    }

    public void darkBeam(final Ballistica shot, boolean reflect){
        boolean terrainAffected = false;

        for (int pos : shot.subPath(1, shot.dist)) {

            if (Dungeon.level.flamable[pos]) {

                Dungeon.level.destroy( pos );
                GameScene.updateMap( pos );
                terrainAffected = true;

            }

            Char ch = Actor.findChar( pos );
            if (ch == null) {
                continue;
            }

            int damage = Random.Int(30, 100);
            ch.damage(damage, this, new EffectType(EffectType.BEAM,EffectType.DARK) );

            if (Dungeon.level.heroFOV[pos]) {
                ch.sprite.flash();
                CellEmitter.center( pos ).burst( PurpleParticle.BURST, Random.IntRange( 1, 2 ) );
            }

            if (!ch.isAlive() && ch == Dungeon.hero) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "dark_kill") );
            }
        }
        final int from = shot.collisionPos;

        Char lastEnemy = null;
        if (lastEnemyId != 0){
            Actor a = Actor.findById(lastEnemyId);
            if (a != null){
                lastEnemy = (Char)a;
            } else {
                lastEnemyId = 0;
            }
        }
        if(lastEnemy == null) return;
        final int target = lastEnemy.pos;

        if(reflect) {
            GameScene.add(Blob.seed(from, 1, ReflectingLaser.class));
            Actor.addDelayed(new Actor() {
                {
                    //it's a visual effect, gets priority no matter what
                    actPriority = MOB_PRIO - 1;
                }

                @Override
                protected boolean act() {

                    Ballistica reflect = new Ballistica(from, target, Ballistica.STOP_TERRAIN);
                    if (reflect.dist > 0) {
                        if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[reflect.collisionPos]) {
                            Fushiginopixeldungeon.scene().add(new Beam.DeathRay(DungeonTilemap.raisedTileCenterToWorld(from), reflect.collisionPointF));
                        }
                        darkBeam(reflect, false);
                    }
                    Blob rl = Dungeon.level.blobs.get(ReflectingLaser.class);
                    int value = rl.cur[from];
                    if (value > 0) {
                        rl.cur[from] -= 1;
                        rl.volume -= 1;
                    }
                    Actor.remove(this);
                    return true;
                }
            }, 1);
        }
        if (terrainAffected) {
            Dungeon.observe();
        }
    }

	public void onZapComplete(Ballistica bolt) {
		zap(bolt);
		next();
	}

    @Override
    public int damage(int dmg, Object src, EffectType type) {
        int damage = super.damage(dmg, src, type);
        LockedFloor lock = Dungeon.hero.buff(LockedFloor.class);
        if (lock != null && !isImmune(src.getClass(),type)) lock.addTime(damage*1.5f);
        return damage;
    }

    @Override
    public void die( Object cause, EffectType type ) {

        super.die( cause, type );

        GameScene.bossSlain();
        Dungeon.level.drop( new IronKey( Dungeon.depth  ), pos ).sprite.drop();

        yell( Messages.get(this, "defeated") );
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        yell( Messages.get(this, "notice") );
    }

    private final String LASER_ROTATIONS = "laser_rotations";
    private final String LASER_TYPE = "laser_type";
    private final String INDEX = "index";
    private final String LASTENEMY_ID = "lastenemy_id";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );
        if(laserRotations != null) {
            float[] rotations = new float[laserRotations.size()];
            for (Float r : laserRotations) {
                rotations[laserRotations.indexOf(r)] = r;
            }
            bundle.put( LASER_ROTATIONS , rotations );
        }
        bundle.put( LASER_TYPE , laserType );
        bundle.put( INDEX , index );
        bundle.put( LASTENEMY_ID , lastEnemyId );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        float[] rotations = bundle.getFloatArray( LASER_ROTATIONS );
        if(rotations != null) {
            for (Float r : rotations) {
                laserRotations.add(r);
            }
        }
        laserType = bundle.getInt( LASER_TYPE );
        index = bundle.getInt( INDEX );
        lastEnemyId = bundle.getInt( LASTENEMY_ID );
        BossHealthBar.assignBoss(this);
    }

	protected class Hunting extends Mob.Hunting {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy )) {
                if(canAttack(enemy) && Random.Int(2) == 0){
                    return doAttack( enemy );
                }else {
                    Ballistica shot = new Ballistica(pos, enemy.pos, Ballistica.STOP_TERRAIN);
                    int c = Random.Int(4);
                    if (shot.subPath(1, Math.min(shot.dist, 4)).contains(enemy.pos) && c == 0) {
                        boolean visible = fieldOfView[pos] || fieldOfView[enemy.pos];
                        spend( TIME_TO_ZAP );
                        if (visible) {
                            sprite.zap( enemy.pos );
                        } else {
                            zap(shot);
                        }
                        return !visible;
                    }else if(Random.Int(4) == 1){
                        spend( TICK );
                        laserNet();
                        return false;
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

	public static class ReflectingLaser extends Blob{

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
