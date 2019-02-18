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
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Dragon;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.FallenAngel;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class FallenAngelSprite extends MobSprite {

	private Animation cure;

	private int zapPos;

	public FallenAngelSprite() {
		super();
		
		texture( Assets.FALLEN_ANGEL );
		
		TextureFilm frames = new TextureFilm( texture, 16, 17 );
		
		idle = new Animation( 8, true );
		idle.frames( frames, 0, 1, 2 ,3, 1 ,0);
		
		run = new Animation( 12, true );
		run.frames( frames, 0, 1, 2 ,2, 1 ,0);
		
		attack = new Animation( 18, false );
		attack.frames( frames, 4, 5, 6 ,7 ,8 ,9 ,10 ,1 ,0 );

		zap = attack.clone();

		cure = attack.clone();
		
		die = new Animation( 5, false );
		die.frames( frames, 11, 12, 13, 14, 15 );
		
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
			((FallenAngel)ch).onCureComplete();
		}else if(anim == zap){
			idle();
			((FallenAngel)ch).onZapComplete();
		}
		super.onComplete( anim );
	}
}
