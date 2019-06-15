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
import com.watabou.noosa.TextureFilm;

public class PoisonSpinnerSprite extends MobSprite {

	public void init() {
		super.init();

		perspectiveRaise = 0f;

		texture( Assets.SPINNER );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 10, true );
		idle.frames( frames, 16, 16, 16, 16, 16, 17, 16, 17 );
		
		run = new Animation( 15, true );
		run.frames( frames, 16, 18, 16, 19 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 16, 20, 21, 16 );
		
		die = new Animation( 12, false );
		die.frames( frames, 22, 23, 24, 25 );
		
		play( idle );
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		renderShadow = false;
	}

	@Override
	public int blood() {
		return 0xFFBFE5B8;
	}
}
