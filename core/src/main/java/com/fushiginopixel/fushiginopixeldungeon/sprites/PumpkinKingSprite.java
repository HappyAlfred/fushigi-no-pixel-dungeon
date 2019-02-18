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

public class PumpkinKingSprite extends PumpkinSprite {

	public PumpkinKingSprite() {
		super();
		
		texture( Assets.PUMPKIN );
		
		TextureFilm frames = new TextureFilm( texture, 14, 17 );

		int i = 18;
		idle = new Animation( 2, true );
		idle.frames( frames, i, i+1, i+1, i+2, i+2, i+1, i+1, i );
		
		run = new Animation( 5, true );
		run.frames( frames, i, i+3, i+4, i+3);
		
		attack = new Animation( 15, false );
		attack.frames( frames, i, i+5, i+5, i+6, i+7 );
		
		die = new Animation( 10, false );
		die.frames( frames, i+8, i+9, i+10, i+11, i+12, i+13, i+14, i+15 );
		
		play( idle );
	}
}
