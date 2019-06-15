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
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.tweeners.AlphaTweener;

public class YellowSlimeSprite extends SlimeSprite {

	private Animation knocked;
	public boolean knockedFlag = false;
	public void init() {
		super.init();
		
		texture( Assets.SLIME );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );

		int i = 16 * 3;
		idle = new Animation( 10, true );
		idle.frames( frames, i, i+1, i+1);
		
		run = new Animation( 15, true );
		run.frames( frames, i, i+1, i+2, i+3, i+4, i+4, i+5, i+2, i+1 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, i+2, i+15, i+2 );
		
		die = new Animation( 10, false );
		die.frames( frames, i+12, i+13, i+14 );

		knocked = new Animation( 5, false );
		knocked.frames( frames, i+6, i+7, i+8, i+9, i+10, i+11 );
		
		play( idle );
	}

	@Override
	public void onComplete( Animation anim ) {

		super.onComplete( anim );

		if (anim == knocked) {
			parent.add( new AlphaTweener( this, 0, FADE_TIME ) {
				@Override
				protected void onComplete() {
					YellowSlimeSprite.this.killAndErase();
					parent.erase( this );
				};
			} );
		}
	}
	
	@Override
	public int blood() {
		return 0xFFECD800;
	}
}
