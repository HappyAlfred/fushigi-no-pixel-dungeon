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
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.FallenAngel;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.FallenAngelDoctor;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;

public class FallenAngelDoctorSprite extends MobSprite {

	private Animation cure;

	private int zapPos;

	public void init() {
		super.init();
		
		texture( Assets.FALLEN_ANGEL );

		TextureFilm frames = new TextureFilm( texture, 16, 17 );

		int ofs = 32;

		idle = new Animation( 8, true );
		idle.frames( frames, 0 + ofs, 1 + ofs, 2 + ofs ,3 + ofs, 1 + ofs ,0 + ofs);

		run = new Animation( 12, true );
		run.frames( frames, 0 + ofs, 1 + ofs, 2 + ofs ,2 + ofs, 1 + ofs,0 + ofs);

		attack = new Animation( 18, false );
		attack.frames( frames, 4 + ofs, 5 + ofs, 6 + ofs ,7 + ofs ,8 + ofs ,9 + ofs ,10 + ofs ,1 + ofs ,0 + ofs );

		zap = attack.clone();

		cure = attack.clone();

		die = new Animation( 5, false );
		die.frames( frames, 11 + ofs, 12 + ofs, 13 + ofs, 14 + ofs, 15 + ofs );
		
		play( idle );
	}

	public void zap( int pos ) {

		zapPos = pos;
		super.zap(pos);
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	public void cure( int pos ) {

		zapPos = pos;
		turnTo( ch.pos, pos );
		play( cure );
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == cure) {
			idle();
			((FallenAngelDoctor)ch).onCureComplete();
		}else if(anim == zap){
			idle();
			((FallenAngelDoctor)ch).onZapComplete();
		}
		super.onComplete( anim );
	}
}
