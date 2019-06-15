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
import com.fushiginopixel.fushiginopixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;

public class DM5000Sprite extends MobSprite {

	private Animation rightshot;

	public void init() {
		
		texture( Assets.DM5000 );
		
		TextureFilm frames = new TextureFilm( texture, 27, 22 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1 );
		
		run = new Animation( 10, true );
		run.frames( frames, 0, 2 );
		
		attack = new Animation( 5, false );
		attack.frames( frames, 0, 3, 3, 0 );

		zap = attack.clone();

		rightshot = new Animation( 5, false );
		rightshot.frames( frames, 0, 4, 4, 0 );
		
		die = new Animation( 20, false );
		die.frames( frames, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 0, 5, 6 );
		
		play( idle );
	}

	public boolean leftshot = true;

	@Override
	public void zap( int cell ) {
		super.zap( cell );
		if (leftshot) {
			play( zap );
		}else{
			play(rightshot);
		}
	}
	
	@Override
	public void onComplete( Animation anim ) {
		
		super.onComplete( anim );
		
		if (anim == die) {
			emitter().burst( Speck.factory( Speck.WOOL ), 15 );
		}
	}
	
	@Override
	public int blood() {
		return 0xFFFFFF88;
	}
}
