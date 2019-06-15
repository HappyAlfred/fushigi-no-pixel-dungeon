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

public class SlimeSprite extends MobSprite {

	private Animation knocked;
	public boolean knockedFlag = false;
	public void init() {
		super.init();
		
		texture( Assets.SLIME );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 0, 1, 1);
		
		run = new Animation( 15, true );
		run.frames( frames, 0, 1, 2, 3, 4, 4, 5, 2, 1 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 2, 15, 2 );
		
		die = new Animation( 10, false );
		die.frames( frames, 12, 13, 14 );

		knocked = new Animation( 5, false );
		knocked.frames( frames, 6, 7, 8, 9, 10, 11 );
		
		play( idle );
	}

	@Override
	public void die(){
		if(knockedFlag){
			knocked();
		}else{
			super.die();
		}
	}
	
	public void knocked() {
		sleeping = false;
		play( knocked );
		dying = true;

		if (emo != null) {
			emo.killAndErase();
		}

		if (health != null){
			health.killAndErase();
		}
	}

	@Override
	public void onComplete( Animation anim ) {

		super.onComplete( anim );

		if (anim == knocked) {
			parent.add( new AlphaTweener( this, 0, FADE_TIME ) {
				@Override
				protected void onComplete() {
					SlimeSprite.this.killAndErase();
					parent.erase( this );
				};
			} );
		}
	}

	@Override
	public int blood() {
		return 0xFF00EC00;
	}
}
