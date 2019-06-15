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
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.darts.ParalyticDart;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class GnollBomberSprite extends MobSprite {

	private Animation cast;

	public void init() {

		texture( Assets.GNOLL );

		TextureFilm frames = new TextureFilm( texture, 12, 15 );

		idle = new Animation( 2, true );
		idle.frames( frames, 42, 42, 42, 43, 42, 42, 43, 43 );

		run = new Animation( 12, true );
		run.frames( frames, 46, 47, 48, 49 );

		attack = new Animation( 12, false );
		attack.frames( frames, 44, 45, 42 );

		zap = attack.clone();

		die = new Animation( 12, false );
		die.frames( frames, 50, 51, 52 );

		play( idle );
	}
}
