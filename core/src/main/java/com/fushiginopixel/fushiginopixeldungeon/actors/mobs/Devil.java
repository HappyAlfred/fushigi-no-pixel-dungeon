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
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.GooWarn;
import com.fushiginopixel.fushiginopixeldungeon.actors.blobs.ToxicGas;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Blindness;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.LockedFloor;
import com.fushiginopixel.fushiginopixeldungeon.effects.Beam;
import com.fushiginopixel.fushiginopixeldungeon.effects.BlobEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Delay;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.BlastParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.PurpleParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.keys.IronKey;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.DevilSprite;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.fushiginopixel.fushiginopixeldungeon.ui.BossHealthBar;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class Devil extends Mob {

	private static final float TIME_TO_ZAP	= 1f;
	{
		spriteClass = DevilSprite.class;
		
		HP = HT = 1200;
        EXP = 70;

		HUNTING = new Hunting();

		flying = true;
        properties.add(Property.BOSS);
        properties.add(Property.DEMONIC);
	}

	private ArrayList<Float> boltRotations = new ArrayList<>();
    private int boltRange = -1;
    private int lastEnemyId = 0;
    public int index = -1;
    private int pumpedUp = -1;
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 33, 57 );
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(3, 25);
	}

	public void zap(Ballistica bolt ) {

		for (int c : bolt.subPath(1, Math.min(bolt.dist, 4))) {
            Char ch = Actor.findChar( c );
            if(ch == null) continue;

            int dmg = Random.Int( 10, 25 );
            ch.damage( dmg, this ,new EffectType(EffectType.MAGICAL_BOLT,EffectType.DARK));
            Buff.affect(ch, Blindness.class, 5, new EffectType(EffectType.MAGICAL_BOLT,EffectType.DARK));

            if (!ch.isAlive() && ch == Dungeon.hero) {
                Dungeon.fail( getClass() );
                GLog.n( Messages.get(this, "dark_kill") );
            }

		}
	}

    @Override
    public boolean act() {

	    if(pumpedUp > -1){
            return selfExplode();
        }
        if (state != HUNTING || paralysed > 0){
            index = -1;
        }

        return super.act();
    }

    private static final int EXPLODE_RANGE = 7;

    protected boolean selfExplode() {
        if (pumpedUp < EXPLODE_RANGE && pumpedUp > 0) {
            PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), pumpedUp );
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE)
                    GameScene.add(Blob.seed(i, 2, GooWarn.class));
            }
            pumpedUp++;

            spend( TICK );
        } else if (pumpedUp == EXPLODE_RANGE) {

            PathFinder.buildDistanceMap( pos, BArray.not( Dungeon.level.solid, null ), pumpedUp );
            Sample.INSTANCE.play( Assets.SND_BLAST );
            for (int i = 0; i < PathFinder.distance.length; i++) {
                if (PathFinder.distance[i] < Integer.MAX_VALUE){
                    CellEmitter.get(i).burst(BlastParticle.FACTORY, (pumpedUp - PathFinder.distance[i] + 1) * 3);
                    Char ch = Actor.findChar( i );
                    if (ch != null && ch != this) {
                        int dmg = 1000;
                        ch.damage( dmg, this ,new EffectType(EffectType.BURST,0));

                        if (ch == Dungeon.hero && !ch.isAlive()) {
                            Dungeon.fail(getClass());
                            GLog.n(Messages.get(this, "explode"));
                        }
                    }
                }
            }
            die(this, new EffectType(EffectType.BURST, 0));

            spend( TICK );

        }else {

            pumpedUp++;

            for (int i=0; i < PathFinder.NEIGHBOURS9.length; i++) {
                int j = pos + PathFinder.NEIGHBOURS9[i];
                if (!Dungeon.level.solid[j]) {
                    GameScene.add(Blob.seed(j, 2, GooWarn.class));
                }
            }

            if (Dungeon.level.heroFOV[pos]) {
                sprite.showStatus( CharSprite.NEGATIVE, Messages.get(this, "!!!") );
                GLog.n( Messages.get(this, "pumpup") );
            }

            spend( TICK );
        }

        return true;
    }

	public void setBolt(){
        boltRotations.clear();
        boltRange = -1;
	    final int ct = HP * 2 <= HT ? 4 : 2;
	    int type = Random.Int(2);
	    final int range = Random.Int(2, 6);
	    float wide = 360 / ct;
        float rotation = type == 0 ? 0 :  (ct == 2 ?  90 : 45);
	    for(int i = 0;i < ct ;i++){
            float targetAngle = rotation + wide * i;
            final Ballistica shot = new Ballistica(pos, targetAngle, Ballistica.STOP_TERRAIN, Ballistica.USING_ROTATION);
            boltRotations.add(targetAngle);

            final int current = i;
            ((MagicMissile)sprite.parent.recycle(MagicMissile.class)).reset(
                    MagicMissile.MAGIC_MISSILE,
                    sprite.center(),
                    shot.pathPointF.get(Math.min(shot.dist, range)),
                    new Callback() {
                        @Override
                        public void call() {
                            if(current == ct - 1) {
                                next();
                            }
                        }
                    });
            /*
            sprite.parent.add(new Delay(1 / 2f, new Callback() {
                public void call() {
                    sprite.parent.add(new Beam.LightRay(sprite.center(), shot.pathPointF.get(Math.min(shot.dist, range))));
                }
            }));
            */
        }
	    boltRange = range;
        index = 0;
        ((DevilSprite)sprite).charge( enemy.pos );
    }

    public boolean boltSpread(){
        //if(boltRotations.isEmpty()) return true;
        boolean actioned = darkZap();
        if(index == -1) {
            spend(TICK);
        }
        return actioned;
    }

    public boolean darkZap(){
        float rotation = boltRotations.get(index);
        int range = boltRange;
        index++;
        if(index >= boltRotations.size()){
            index = -1;
        }

	    Ballistica shot = new Ballistica(pos, rotation, Ballistica.MAGIC_BOLT, Ballistica.USING_ROTATION);
        int dist = Math.min(shot.dist, range);
        int collisionPos = shot.path.get(dist);
        if(shot.dist > 0) {
            if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[collisionPos] ) {
                ((DevilSprite)sprite).darkZap( shot, range, 4 );
                return false;
            } else {
                darkBolt(shot, range, 4);
                return true;
            }
        }else{
            return true;
        }
    }

    public void darkBolt(final Ballistica shot, final int range, final int chase){
        int dist = Math.min(shot.dist, range);
        int collisionPos = shot.path.get(dist);

        Char ch = Actor.findChar( collisionPos );
        if(ch != null) {
            int dmg = Random.Int(50, 70);
            ch.damage(dmg, this, new EffectType(EffectType.MAGICAL_BOLT, EffectType.DARK));

            if (!ch.isAlive() && ch == Dungeon.hero) {
                Dungeon.fail(getClass());
                GLog.n(Messages.get(this, "bolt_kill"));
            }
        }else{
            final int from = collisionPos;

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
            if(chase > 0) {
                GameScene.add(Blob.seed(from, 1, ReflectingBolt.class));
                Actor.addDelayed(new Actor() {
                    {
                        //it's a visual effect, gets priority no matter what
                        actPriority = MOB_PRIO - 1;
                    }

                    @Override
                    protected boolean act() {

                        final Ballistica reflect = new Ballistica(from, target, Ballistica.MAGIC_BOLT);
                        int dist = Math.min(reflect.dist, 3);
                        int collisionPos = reflect.path.get(dist);
                        boolean actioned = true;
                        if (reflect.dist > 0) {
                            if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[collisionPos]) {
                                actioned = false;
                                ((MagicMissile)Fushiginopixeldungeon.scene().recycle(MagicMissile.class)).reset(
                                        MagicMissile.SHADOW,
                                        DungeonTilemap.raisedTileCenterToWorld(from),
                                        reflect.pathPointF.get(dist),
                                        new Callback() {
                                            @Override
                                            public void call() {
                                                darkBolt(reflect, 3, chase - 1);
                                                next();
                                            }
                                        }
                                );
                            }else{
                                darkBolt(reflect, 3, chase - 1);
                            }
                        }
                        Blob rl = Dungeon.level.blobs.get(ReflectingBolt.class);
                        int value = rl.cur[from];
                        if (value > 0) {
                            rl.cur[from] -= 1;
                            rl.volume -= 1;
                        }
                        Actor.remove(this);
                        return actioned;
                    }
                }, 1);
            }

        }
    }

    public void onZapComplete(Ballistica bolt) {
        zap(bolt);
        next();
    }

    @Override
    public int damage(int dmg, Object src, EffectType type) {
	    if(HP <= dmg){
	        dmg = 0;
	        HP = 1;
	        if(pumpedUp == -1)
	            pumpedUp = 0;
        }

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
        Dungeon.level.drop( new IronKey( Dungeon.depth  ), pos ).sprite.drop();

        yell( Messages.get(this, "defeated") );
    }

    @Override
    public void notice() {
        super.notice();
        BossHealthBar.assignBoss(this);
        yell( Messages.get(this, "notice") );
    }

    private final String BOLT_ROTATIONS = "bolt_rotations";
    private final String BOLT_RANGE = "bolt_type";
    private final String INDEX = "index";
    private final String LASTENEMY_ID = "lastenemy_id";
    private final String PUMPEDUP = "pumpedup";

    @Override
    public void storeInBundle( Bundle bundle ) {

        super.storeInBundle( bundle );
        if(boltRotations != null) {
            float[] rotations = new float[boltRotations.size()];
            for (Float r : boltRotations) {
                rotations[boltRotations.indexOf(r)] = r;
            }
            bundle.put( BOLT_ROTATIONS , rotations );
        }
        bundle.put( BOLT_RANGE , boltRange );
        bundle.put( INDEX , index );
        bundle.put( LASTENEMY_ID , lastEnemyId );
        bundle.put( PUMPEDUP , pumpedUp );
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        float[] rotations = bundle.getFloatArray( BOLT_ROTATIONS );
        if(rotations != null) {
            for (Float r : rotations) {
                boltRotations.add(r);
            }
        }
        boltRange = bundle.getInt( BOLT_RANGE );
        index = bundle.getInt( INDEX );
        lastEnemyId = bundle.getInt( LASTENEMY_ID );
        pumpedUp = bundle.getInt( PUMPEDUP );
        BossHealthBar.assignBoss(this);
    }

	protected class Hunting extends Mob.Hunting {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            if(index > -1) {
                return boltSpread();
            }
			enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy )) {
                lastEnemyId = enemy.id();
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
                        setBolt();
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

	public static class ReflectingBolt extends Blob{

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
