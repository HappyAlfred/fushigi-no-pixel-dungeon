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

package com.fushiginopixel.fushiginopixeldungeon.sprites;

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Devil;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Diablo;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicBreath;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissileBent;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.FlameParticle;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.BallisticaBentLine;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;

import java.util.ArrayList;
import java.util.HashSet;

public class DiabloSprite extends MobSprite {

	public void init() {
		
		texture( Assets.EYE );
		
		TextureFilm frames = new TextureFilm( texture, 16, 18 );
		
		idle = new Animation( 8, true );
		idle.frames( frames, 0, 1, 2 );
		
		run = new Animation( 12, true );
		run.frames( frames, 5, 6 );
		
		attack = new Animation( 8, false );
		attack.frames( frames, 4, 3 );
		zap = attack.clone();
		
		die = new Animation( 8, false );
		die.frames( frames, 7, 8, 9 );
		
		play( idle );
	}

	public void breathZap(final HashSet<Integer> affectedCells, int target ) {

		turnTo( ch.pos , target );
		play( zap );
        final Ballistica shot = new Ballistica(ch.pos, target, Ballistica.WONT_STOP);
        int dist = Math.min(shot.dist, 8);
        final int ct = ((Diablo) ch).index;
        MagicBreath.breathInLevel(parent, this, shot.pathPointF.get(dist),
                20,
                1,
                Ballistica.STOP_TERRAIN,
                MagicMissile.FIRE,
                new Callback() {
                    public void call() {
                        ((Diablo)ch).fireBreath(affectedCells);
                        if(ct != 0) {
                            ((Diablo) ch).setBreath(ct);
                        }else{
                            ((Diablo) ch).index = -1;
                            ch.next();
                        }
                    }});
        MagicBreath.breathInLevel(parent, this, shot.pathPointF.get(dist),
                20,
                1,
                Ballistica.STOP_TERRAIN,
                MagicMissile.MAGIC_MISSILE,
                null);
	}

    public void boltZap(ArrayList<BallisticaBentLine> shots ) {

        turnTo( ch.pos , shots.get(0).collisionPos );
        if(curAnim != zap)
            play( zap );

        final HashSet<Integer> affectedCells = new HashSet<>();
        for(BallisticaBentLine b : shots){
            affectedCells.addAll(b.path);
        }
        Sample.INSTANCE.play( Assets.SND_ZAP );
        for(int i = 0; i < shots.size(); i++){
            final int current = i;
            MagicMissileBent.boltInLevel(parent, this, shots.get(i).nodes, 10, MagicMissile.FIRE_CONE, new Callback() {
                @Override
                public void call() {
                    if(current == 0){
                        ((Diablo)ch).fireBolt(affectedCells);
                        ch.next();
                    }
                }
            });
        }
    }

    public void auraZap(ArrayList<Ballistica> shots ) {

        turnTo( ch.pos , shots.get(0).collisionPos );
        play( zap );

        final HashSet<Integer> affectedCells = new HashSet<>();
        for(Ballistica b : shots){
            affectedCells.addAll(b.path);
        }
        Sample.INSTANCE.play( Assets.SND_ZAP );
        for(int i = 0; i < shots.size(); i++){
            final int current = i;
            int dist = Math.min(shots.get(i).dist, 8);
            ((MagicMissile)parent.recycle( MagicMissile.class )).reset(MagicMissile.FIRE, center(), shots.get(i).pathPointF.get(dist), new Callback() {
                @Override
                public void call() {
                    if(current == 0){
                        ((Diablo)ch).fireAura(affectedCells);
                        ch.next();
                    }
                }
            });
        }
    }

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}else if (anim == die){
            emitter().start( FlameParticle.FACTORY, 0.05f, 10 );
        }
		super.onComplete( anim );
	}
}
