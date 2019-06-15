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
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.GelCube;
import com.fushiginopixel.fushiginopixeldungeon.items.food.SlimyGel;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class GelCubeSprite extends MobSprite {

	public void init() {

		texture( Assets.GELCUBE );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 8, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4 );
		
		run = new Animation( 15, true );
		run.frames( frames, 5, 0, 6, 7, 7, 6, 0 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 4, 8, 4 );

		zap = new Animation( 12, false );
		zap.frames( frames, 4, 9, 4, 10 ,11 ,12, 4, 9, 4);
		
		die = new Animation( 10, false );
		die.frames( frames, 13, 14, 15 );
		
		play( idle );
	}

	public void zap( int cell ) {

		turnTo( ch.pos , cell );
		play( zap );
		final Ballistica shot = new Ballistica( ch.pos, cell, Ballistica.PROJECTILE);

		((MissileSprite)parent.recycle( MissileSprite.class )).
				reset( ch.pos, cell, new SlimyGel(),ch, new Callback() {
					@Override
					public void call() {
						ch.next();
						((GelCube)ch).onZapComplete(shot);
					}
				} );
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}

	@Override
	public int blood() {
		return 0xFF0DD50D;
	}
}
