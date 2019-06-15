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

public class GoblinFanaticsSprite extends GoblinSapperSprite {

	public void init() {
		super.init();
		
		texture( Assets.GOBLIN_SAPPER );
		
		TextureFilm frames = new TextureFilm( texture, 13, 14 );

		int i = 19;
		idle = new Animation( 2, true );
		idle.frames( frames, i, i, i, i+1);
		
		run = new Animation( 15, true );
		run.frames( frames, i+4, i+5, i+6, i+7, i+8, i+9 );
		
		attack = new Animation( 12, false );
		attack.frames( frames, i+2, i+3, i+2, i );
		
		die = new Animation( 12, false );
		die.frames( frames, i+10, i+11, i+12 );

		explode = new Animation( 12, false );
		explode.frames( frames, i+13, i+14, i+15, i+16 );
		
		play( idle );
	}
}
