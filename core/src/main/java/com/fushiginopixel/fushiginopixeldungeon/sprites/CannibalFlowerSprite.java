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

public class CannibalFlowerSprite extends MobSprite {

	public Animation grow;

	public CannibalFlowerSprite() {
		super();
		
		texture( Assets.CANNIBAL_FLOWER );
		
		TextureFilm frames = new TextureFilm( texture, 24, 24 );

		int i = 21*0;
		idle = new Animation( 2, true );
		idle.frames( frames, 0+i, 1+i, 2+i, 1+i );
		
		run = idle.clone();
		
		attack = new Animation( 12, false );
		attack.frames( frames, 2+i, 3+i, 4+i, 5+i, 6+i, 7+i, 8+i );
		
		die = new Animation( 12, false );
		die.frames( frames, 9+i, 10+i, 11+i );

		grow = new Animation( 12, false );
		grow.frames( frames, 17+i, 12+i, 13+i, 14+i, 15+i, 16+i );
		
		play( idle );
	}
}
