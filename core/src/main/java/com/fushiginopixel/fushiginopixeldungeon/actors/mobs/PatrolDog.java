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
import com.fushiginopixel.fushiginopixeldungeon.Challenges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Amok;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Bleeding;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Terror;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.fushiginopixel.fushiginopixeldungeon.items.food.MysteryMeat;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.PatrolDogSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.PumpkinSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

public class PatrolDog extends Mob {

	{
		spriteClass = PatrolDogSprite.class;
		
		HP = HT = 50;
		defenseSkill = 18;
        EXP = 9;

        loot = new MysteryMeat();
        lootChance = 0.125f;

        WANDERING = new Wandering();
        HUNTING = new Hunting();
	}
    public boolean canAlarm = true;
	public int range = 6;
	public static int sniff = 4;
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 11, 25 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 25;
	}
	
	@Override
	public int drRoll() {
		return Random.NormalIntRange(0, 4);
	}

	@Override
    protected Char chooseEnemy() {

        Terror terror = buff( Terror.class );
        if (terror != null) {
            Char source = (Char)Actor.findById( terror.object );
            if (source != null) {
                return source;
            }
        }

        //find a new enemy if..
        boolean newEnemy = false;
        //we have no enemy, or the current one is dead
        if ( enemy == null || !enemy.isAlive() || state == WANDERING)
            newEnemy = true;
            //We are an ally, and current enemy is another ally.
        else if (alignment == Alignment.ALLY && enemy.alignment == Alignment.ALLY)
            newEnemy = true;
            //We are amoked and current enemy is the hero
        else if (buff( Amok.class ) != null && enemy == Dungeon.hero)
            newEnemy = true;

        if ( newEnemy ) {

            HashSet<Char> enemies = new HashSet<>();

            //if the mob is amoked...
            if ( buff(Amok.class) != null) {
                //try to find an enemy mob to attack first.
                for (Mob mob : Dungeon.level.mobs)
                    if (mob.alignment == Alignment.ENEMY && mob != this && (fieldOfView[mob.pos] || distance(mob) <= sniff))
                        enemies.add(mob);

                if (enemies.isEmpty()) {
                    //try to find ally mobs to attack second.
                    for (Mob mob : Dungeon.level.mobs)
                        if (mob.alignment == Alignment.ALLY && mob != this && (fieldOfView[mob.pos] || distance(mob) <= sniff))
                            enemies.add(mob);

                    if (enemies.isEmpty()) {
                        //try to find the hero third
                        if (fieldOfView[Dungeon.hero.pos] || distance(Dungeon.hero) <= sniff) {
                            enemies.add(Dungeon.hero);
                        }
                    }
                }

                //if the mob is an ally...
            } else if ( alignment == Alignment.ALLY ) {
                //look for hostile mobs that are not passive to attack
                for (Mob mob : Dungeon.level.mobs)
                    if (mob.alignment == Alignment.ENEMY
                            && (fieldOfView[mob.pos] || distance(mob) <= sniff)
                            && mob.state != mob.PASSIVE)
                        enemies.add(mob);

                //if the mob is an enemy...
            } else if (alignment == Alignment.ENEMY) {
                //look for ally mobs to attack
                for (Mob mob : Dungeon.level.mobs)
                    if (mob.alignment == Alignment.ALLY && (fieldOfView[mob.pos] || distance(mob) <= sniff))
                        enemies.add(mob);

                //and look for the hero
                if (fieldOfView[Dungeon.hero.pos] || distance(Dungeon.hero) <= sniff) {
                    enemies.add(Dungeon.hero);
                }

            }

            //neutral character in particular do not choose enemies.
            if (enemies.isEmpty()){
                return null;
            } else {
                //go after the closest potential enemy, preferring the hero if two are equidistant
                Char closest = null;
                for (Char curr : enemies){
                    if (closest == null
                            || Dungeon.level.distance(pos, curr.pos) < Dungeon.level.distance(pos, closest.pos)
                            || Dungeon.level.distance(pos, curr.pos) == Dungeon.level.distance(pos, closest.pos) && curr == Dungeon.hero){
                        closest = curr;
                    }
                }
                return closest;
            }

        } else
            return enemy;
    }

    private class Wandering extends Mob.Wandering{
        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            if (enemyInFOV && (justAlerted || Random.Int( distance( enemy ) / 2 + enemy.stealth()) == 0)) {

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

            }else if(enemy != null){
                enemySeen = false;
                int oldPos = pos;
                ArrayList<Integer> canMove = new ArrayList<>();

                for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
                    int p = pos + PathFinder.NEIGHBOURS8[i];
                    if (Actor.findChar( p ) == null && (Dungeon.level.passable[p] || Dungeon.level.avoid[p] || Dungeon.level.solid[p])) {
                        canMove.add( p );
                    }
                }

                if(!canMove.isEmpty()) {
                    int minDistance = 1000;

                    for (int i : canMove) {
                        if (Dungeon.level.distance(i, enemy.pos) <= minDistance) {
                            minDistance = Dungeon.level.distance(i, enemy.pos);
                        }
                    }

                    ArrayList<Integer> moveTo = new ArrayList<>();

                    for (int i : canMove) {
                        if (Dungeon.level.distance(i, enemy.pos) == minDistance) {
                            moveTo.add(i);
                        }
                    }

                    if (Random.Int(10) <= 7) {
                        target = moveTo.get(Random.Int(moveTo.size()));
                    } else target = canMove.get(Random.Int(canMove.size()));
                }else target = Dungeon.level.randomDestination();


                if (target != -1 && getCloser( target )) {
                    spend( 1 / speed() );
                    return moveSprite( oldPos, pos );
                }else spend(TICK);
            }
            else {

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
    protected class Hunting extends Mob.Hunting {

        @Override
        public boolean act( boolean enemyInFOV, boolean justAlerted ) {
            enemySeen = enemyInFOV;
            if (enemyInFOV && !isCharmedBy( enemy ) && canAttack( enemy )) {

                return doAttack( enemy );

            } else {

                if (enemyInFOV) {
                    if(canAlarm){
                        canAlarm = false;
                        for (Mob mob : Dungeon.level.mobs) {
                            if(distance(mob) <= range && mob.state != mob.HUNTING)
                            mob.beckon( pos );
                        }

                        if (Dungeon.level.heroFOV[pos]) {
                            GLog.w( Messages.get(PatrolDog.class, "alarm") );
                            CellEmitter.center(pos).start( Speck.factory(Speck.SCREAM), 0.3f, 3 );
                        }
                        Sample.INSTANCE.play( Assets.SND_ALERT );
                        spend( 1);
                        return true;
                    }
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
                        canAlarm = true;
                        sprite.showLost();
                        state = WANDERING;
                        target = Dungeon.level.randomDestination();
                    }
                    return true;
                }
            }
        }
    }
}
