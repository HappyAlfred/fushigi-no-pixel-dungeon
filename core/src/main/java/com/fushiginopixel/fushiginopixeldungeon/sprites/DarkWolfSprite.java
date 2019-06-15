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

public class DarkWolfSprite extends PatrolDogSprite {

	public void init() {
		super.init();
		
		texture( Assets.DOG );
		
		TextureFilm frames = new TextureFilm( texture, 17, 14 );
		
		idle = new Animation( 2, true );
		idle.frames( frames, 15, 15 ,15 ,16 );
		
		run = new Animation( 10, true );
		run.frames( frames, 20, 21 ,22 ,23 ,24 ,25);
		
		attack = new Animation( 12, false );
		attack.frames( frames, 17, 18, 19, 15 );
		
		die = new Animation( 5, false );
		die.frames( frames, 26, 27, 28, 29 );
		
		play( idle );
	}
}
