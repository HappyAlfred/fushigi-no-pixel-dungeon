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

public class MechaSteamTortoiseSprite extends MobSprite {


	public void init() {
		super.init();
		
		texture( Assets.MECHANICAL_BEAST );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		int i = 16 * 4;
		
		idle = new Animation( 5, true );
		idle.frames( frames, 0+i, 1+i );

		run = new Animation( 8, true );
		run.frames( frames, 2+i, 3+i, 4+i, 5+i);

		attack = new Animation( 12, false );
		attack.frames( frames, 6+i, 7+i, 0+i );

		die = new Animation( 5, false );
		die.frames( frames, 8+i, 9+i, 10+i, 11+i, 12+i);
		
		play( idle );
	}

	@Override
	public int blood() {
		return 0xFF6f6c71;
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == die) {
			emitter().burst( Speck.factory(Speck.STEAM), 15 );
		}
		super.onComplete( anim );
	}
}
