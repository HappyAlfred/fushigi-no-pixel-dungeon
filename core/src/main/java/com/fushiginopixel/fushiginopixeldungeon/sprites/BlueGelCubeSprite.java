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
import com.watabou.utils.Callback;

public class BlueGelCubeSprite extends GelCubeSprite {

	public void init() {
		super.init();

		texture( Assets.GELCUBE );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		int i = 16 * 2;
		idle = new Animation( 8, true );
		idle.frames( frames, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i+1, i+2, i+3, i+4 );
		
		run = new Animation( 15, true );
		run.frames( frames, i+5, i, i+6, i+7, i+7, i+6, i );
		
		attack = new Animation( 12, false );
		attack.frames( frames, i+4, i+8, i+4 );

		zap = new Animation( 12, false );
		zap.frames( frames, i+4, i+9, i+4, i+10, i+11,i+12, i+4, i+9, i+4);
		
		die = new Animation( 10, false );
		die.frames( frames, i+13, i+14, i+15 );
		
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
	public int blood() {
		return 0xFF0D2DB5;
	}
}
