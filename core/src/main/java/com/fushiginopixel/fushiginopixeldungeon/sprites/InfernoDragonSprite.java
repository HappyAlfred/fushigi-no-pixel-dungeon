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
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Dragon;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.InfernoDragon;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicBreath;
import com.fushiginopixel.fushiginopixeldungeon.effects.MagicMissile;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.BallisticaSector;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class InfernoDragonSprite extends MobSprite {

	public void init() {
		super.init();

		int ofs = 14;
		
		texture( Assets.DRAGON );
		
		TextureFilm frames = new TextureFilm( texture, 18, 18 );

		idle = new Animation( 2, true );
		idle.frames( frames, 0 + ofs, 1 + ofs, 2 + ofs ,2 + ofs, 1 + ofs ,0 + ofs);
		
		run = new Animation( 12, true );
		run.frames( frames, 0 + ofs, 1 + ofs, 2 + ofs ,2 + ofs, 1 + ofs ,0 + ofs);
		
		attack = new Animation( 12, false );
		attack.frames( frames, 3 + ofs, 4 + ofs, 5 + ofs ,1 + ofs );

		zap = attack.clone();
		
		die = new Animation( 12, false );
		die.frames( frames, 6 + ofs, 7 + ofs, 8 + ofs );
		
		play( idle );
	}

	public void zap( int cell ) {

		/*
		turnTo( ch.pos , cell );
		play( zap );
		final Ballistica shot = new Ballistica( ch.pos, cell, Ballistica.STOP_TERRAIN);

		((InfernoDragon)ch).dragonBreath(shot, new Callback() {
			public void call() {
				((InfernoDragon)ch).onZapComplete(shot);
			}
		});
		*/

		/*
		final Ballistica shot = new Ballistica( ch.pos, cell, Ballistica.STOP_TERRAIN);
		int dist = Math.min(shot.dist, 6);
		parent.add(new MagicBreath(this.center(), DungeonTilemap.raisedTileCenterToWorld(shot.path.get(dist)),
				60,
				1,
				1,
				MagicMissile.FIRE_CONE, new Callback() {
			public void call() {
				((InfernoDragon)ch).onZapComplete(shot);
			}
		}));
		*/

		final Ballistica shot = new Ballistica( ch.pos, cell, Ballistica.WONT_STOP);
		int dist = Math.min(shot.dist, 6);
		MagicBreath.breathInLevel(parent, this, shot.pathPointF.get(dist),
				60,
				1,
				Ballistica.STOP_TERRAIN,
				MagicMissile.FIRE_CONE,
				new Callback() {
					public void call() {
						((InfernoDragon)ch).onZapComplete(shot);
					}});
	}

	@Override
	public void onComplete( Animation anim ) {
		if (anim == zap) {
			idle();
		}
		super.onComplete( anim );
	}
}
