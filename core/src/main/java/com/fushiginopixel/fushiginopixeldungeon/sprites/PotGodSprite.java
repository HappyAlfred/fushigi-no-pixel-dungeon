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

public class PotGodSprite extends PotFairySprite {

	public void init() {
		super.init();

		texture( Assets.POT_FAIRY );

		TextureFilm frames = new TextureFilm( texture, 12, 15 );

		int i = 3 * 21;
		idle = new Animation( 2, true );
		idle.frames( frames, 0+i, 0+i, 0+i, 1+i );

		run = new Animation( 12, true );
		run.frames( frames, 2+i, 3+i, 4+i, 5+i );

		attack = new Animation( 12, false );
		attack.frames( frames, 6+i, 7+i, 0+i );

		zap = new Animation( 4, false );
		zap.frames( frames, 8+i, 9+i, 9+i, 9+i, 9+i, 9+i, 9+i, 8+i, 0+i );

		die = new Animation( 12, false );
		die.frames( frames, 10+i, 11+i, 12+i, 13+i, 14+i, 15+i, 16+i, 17+i, 18+i );

		play( idle );
	}
}
