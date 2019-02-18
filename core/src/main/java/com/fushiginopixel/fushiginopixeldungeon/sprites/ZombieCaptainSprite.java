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
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Zombie;
import com.watabou.noosa.TextureFilm;

public class ZombieCaptainSprite extends ZombieSprite {

	public ZombieCaptainSprite() {
		super();
		
		texture( Assets.ZOMBIE );
		
		TextureFilm frames = new TextureFilm( texture, 15, 15 );

		int i = 17*2;
		idle = new Animation( 2, true );
		idle.frames( frames, i );
		
		run = new Animation( 10, true );
		run.frames( frames, i+1, i, i+2, i);
		
		attack = new Animation( 12, false );
		attack.frames( frames, i+3, i+4, i+3, i );
		
		die = new Animation( 12, false );
		die.frames( frames, i+5, i+6 );
		
		play( idle );
	}
}
