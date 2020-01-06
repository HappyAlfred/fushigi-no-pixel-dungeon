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
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ShadowParticle;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class GuardSprite extends MobSprite {

	public void init() {
		super.init();

		texture( Assets.GUARD );

		TextureFilm frames = new TextureFilm( texture, 12, 16 );
		int i = 0;

		idle = new Animation( 2, true );
		idle.frames( frames, 0+i, 0+i, 0+i, 1+i, 0+i, 0+i, 1+i, 1+i );

		run = new Animation( 15, true );
		run.frames( frames, 2+i, 3+i, 4+i, 5+i, 6+i, 7+i );

		attack = new Animation( 12, false );
		attack.frames( frames, 8+i, 9+i, 10+i );

		die = new Animation( 8, false );
		die.frames( frames, 11+i, 12+i, 13+i, 14+i );

		play( idle );
	}

	@Override
	public void play( Animation anim ) {
		if (anim == die) {
			emitter().burst( ShadowParticle.UP, 4 );
		}
		super.play( anim );
	}
}