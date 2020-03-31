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
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.DeathEye;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Devil;
import com.fushiginopixel.fushiginopixeldungeon.effects.Beam;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicBreath;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.FlameParticle;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class DevilSprite extends MobSprite {

    private Animation charging;
    private Emitter chargeParticles;

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

        charging = new Animation( 12, true);
        charging.frames( frames, 3, 4 );

        chargeParticles = centerEmitter();
        chargeParticles.autoKill = false;
        chargeParticles.pour(MagicMissile.MagicParticle.ATTRACTING, 0.05f);
        chargeParticles.on = false;
		
		die = new Animation( 8, false );
		die.frames( frames, 7, 8, 9 );
		
		play( idle );
	}

    @Override
    public void link(Char ch) {
        super.link(ch);
        if (((Devil)ch).index > -1) play(charging);
    }

    @Override
    public void update() {
        super.update();
        chargeParticles.pos(center());
        chargeParticles.visible = visible;
    }

    public void charge( int pos ){
        turnTo(ch.pos, pos);
        play(charging);
    }

    @Override
    public void play(Animation anim) {
        chargeParticles.on = anim == charging;
        super.play(anim);
    }

	public void zap( int cell ) {

		turnTo( ch.pos , cell );
		play( zap );
		final Ballistica shot = new Ballistica( ch.pos, cell, Ballistica.WONT_STOP);
        int dist = Math.min(shot.dist, 4);
        MagicBreath.breathInLevel(parent, this, shot.pathPointF.get(dist),
                20,
                1,
                Ballistica.MAGIC_BOLT,
                MagicMissile.SHADOW,
                new Callback() {
                    public void call() {
                        ((Devil)ch).onZapComplete(shot);
                    }});
        /*
		parent.add(new MagicBreath(this.center(), shot.pathPointF.get(dist),
				20,
				1,
				1,
				MagicMissile.SHADOW, new Callback() {
			public void call() {
				((Devil)ch).onZapComplete(shot);
			}
		}));
		*/
	}

    public void darkZap(final Ballistica shot, final int range, final int chase ) {

        if(((Devil)ch).index > -1){
            idle();
        }

        Sample.INSTANCE.play( Assets.SND_ZAP );
        int dist = Math.min(shot.dist, range);
        ((MagicMissile)parent.recycle( MagicMissile.class )).reset(
                MagicMissile.SHADOW,
                center(),
                shot.pathPointF.get(dist),
                new Callback() {
            public void call() {
                ((Devil)ch).darkBolt(shot, range, chase);
                ((Devil)ch).next();
            }
        });;
    }

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}else if (anim == die){
            chargeParticles.killAndErase();
            emitter().start( FlameParticle.FACTORY, 0.05f, 10 );
        }
		super.onComplete( anim );
	}
}
