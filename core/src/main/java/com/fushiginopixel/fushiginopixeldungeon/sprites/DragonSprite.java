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
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Dragon;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class DragonSprite extends MobSprite {

	public DragonSprite() {
		super();
		
		texture( Assets.DRAGON );
		
		TextureFilm frames = new TextureFilm( texture, 18, 18 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 0, 1, 2 ,2, 1 ,0);
		
		run = new Animation( 12, true );
		run.frames( frames, 0, 1, 2 ,2, 1 ,0);
		
		attack = new Animation( 12, false );
		attack.frames( frames, 3, 4, 5 ,1 );

		zap = attack.clone();
		
		die = new Animation( 12, false );
		die.frames( frames, 6, 7, 8 );
		
		play( idle );
	}

	public void zap( int cell ) {

		turnTo( ch.pos , cell );
		play( zap );
		final Ballistica shot = new Ballistica( ch.pos, cell, Ballistica.MAGIC_BOLT);

		MagicMissile.boltFromChar( parent,
				MagicMissile.FIRE,
				this,
				cell,
				new Callback() {
					@Override
					public void call() {
						((Dragon)ch).onZapComplete(shot);
					}
				} );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}
}
