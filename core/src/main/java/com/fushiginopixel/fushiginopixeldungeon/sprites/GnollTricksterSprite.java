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
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.darts.ParalyticDart;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class GnollTricksterSprite extends MobSprite {

	private Animation cast;

	public void init() {

		texture( Assets.GNOLL );

		TextureFilm frames = new TextureFilm( texture, 12, 15 );

		idle = new MovieClip.Animation( 2, true );
		idle.frames( frames, 21, 21, 21, 22, 21, 21, 22, 22 );

		run = new MovieClip.Animation( 12, true );
		run.frames( frames, 25, 26, 27, 28 );

		attack = new MovieClip.Animation( 12, false );
		attack.frames( frames, 23, 24, 21 );

		cast = attack.clone();

		die = new MovieClip.Animation( 12, false );
		die.frames( frames, 29, 30, 31 );

		play( idle );
	}

	@Override
	public void attack( int cell ) {
		final Char enemy = Actor.findChar(cell);

		((MissileSprite)parent.recycle( MissileSprite.class )).
				reset( ch.pos, cell, new ParalyticDart(),ch, new Callback() {
					@Override
					public void call() {
						ch.next();
						if (enemy != null) ch.attack(enemy, new EffectType(EffectType.MISSILE ,0));
					}
				} );

		play( cast );
		turnTo( ch.pos , cell );
	}
}
