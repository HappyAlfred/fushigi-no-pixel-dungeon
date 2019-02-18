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

public class VampireBatSprite extends MobSprite {

	public VampireBatSprite() {
		super();
		
		texture( Assets.BAT );
		
		TextureFilm frames = new TextureFilm( texture, 15, 15 );
		
		idle = new Animation( 8, true );
		idle.frames( frames, 16, 17 );
		
		run = new Animation( 12, true );
		run.frames( frames, 16, 17 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, 18, 19, 16, 17 );
		
		die = new Animation( 12, false );
		die.frames( frames, 20, 21, 22 );
		
		play( idle );
	}
}
