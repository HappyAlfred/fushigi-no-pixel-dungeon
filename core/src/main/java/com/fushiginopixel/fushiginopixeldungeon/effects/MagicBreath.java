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

package com.fushiginopixel.fushiginopixeldungeon.effects;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Fushiginopixeldungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.Visual;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class MagicBreath extends Group {

	private static final double A = 180 / Math.PI;

	private float time = 0f;
	private float duration;
	private int hit = 0;

	private Callback callback;

	private float distance;
	/*
	duration(s)
	density(quantity/tick)
	 */
	private float interval = 0.05f;
	private float wide;
	private int pierceParam;
	private int type;

	private PointF from, to;

	public void reset(PointF from, PointF to, float wide, float duration, int type, Callback callback){
		reset(from, to, wide, duration, 0, type, callback);
	}

	/*
	public MagicBreath(PointF from, PointF to, float wide, float duration, float density, int type, Callback callback){
		super();

		this.callback = callback;

		this.from = from;
		this.to = to;
		this.duration = duration;
		this.density = density;
		this.wide = wide;
		this.type = type;

		float dx = to.x - from.x;
		float dy = to.y - from.y;
		distance = (float)Math.hypot(dx, dy);

	}
	*/

	public void reset(PointF from, PointF to, float wide, float duration, int pierceParams, int type, Callback callback){

		this.callback = callback;

		this.from = from;
		this.to = to;
		this.duration = duration;
		this.wide = wide;
		this.pierceParam = pierceParams;
		this.type = type;

		float dx = to.x - from.x;
		float dy = to.y - from.y;
		distance = (float)Math.hypot(dx, dy);
	}

	protected void emit() {

		float dx = to.x - from.x;
		float dy = to.y - from.y;

		double rotation = Math.atan2( dy, dx ) * A;//angle
		rotation += Random.Float(-wide/2, wide/2);
		PointF target = new PointF();
		target.set((float)Math.cos(rotation / A) * distance + from.x, (float)Math.sin(rotation / A) * distance + from.y);

		/*
		((MagicMissile) Fushiginopixeldungeon.scene().recycle(MagicMissile.class)).
				reset(type, from, target, null
				);
		*/
		if(Fushiginopixeldungeon.scene() instanceof GameScene && pierceParam > 0){
			Ballistica ballistica = new Ballistica(Dungeon.level.pointFToCell(from), (float) rotation, pierceParam, Ballistica.USING_ROTATION);
			int dist = (int) (distance / DungeonTilemap.SIZE);
			if (ballistica.path.indexOf(ballistica.collisionPos) <= dist){
				target.set(ballistica.collisionPointF);
			}
		}
		((MagicMissile) parent.recycle(MagicMissile.class)).
				reset(type, from, target, new Callback() {
							@Override
							public void call() {
								hit++;
								if(hit == 1 && callback != null){
									callback.call();
								}
							}
						}
				);
	}

	@Override
	public void update() {
		time += Game.elapsed;
		duration -= Game.elapsed;
		while (time > interval) {
			time -= interval;
			emit();
		}
		if (duration < 0) {
			killAndErase();
		}

		/*
		if ((spent += Game.elapsed) > duration) {

			killAndErase();
			/*
			if (callback != null) {
				callback.call();
			}


			((MagicMissile) Fushiginopixeldungeon.scene().recycle(MagicMissile.class)).
					reset(type, from, to, callback
					);

		} else {

			float dx = to.x - from.x;
			float dy = to.y - from.y;
			float length = (float) Math.sqrt(dx * dx + dy * dy);

			for(int i = 0; i < density; i++){
				float rotation = (float)(Math.atan2( dy, dx ) * A);
				rotation += Random.Float(-deflection, deflection);
				PointF target = new PointF();
				target.set((float)Math.cos(rotation / A) * length, (float)Math.sin(rotation / A) * length);


				((MagicMissile) Fushiginopixeldungeon.scene().recycle(MagicMissile.class)).
						reset(type, from, target, null
						);
			}
		}
		*/
	}

	public static void breathInLevel(Group group, Visual fromSprite, PointF to, float wide, float duration, int pierceParams, int type, Callback callback){
		MagicBreath breath = ((MagicBreath)group.recycle( MagicBreath.class ));
		breath.reset(fromSprite.center(), to, wide, duration, pierceParams, type, callback);
	}

	//convenience method for the common case of a bolt going from a character to a tile or enemy
	public static void breathInLevel(Group group, Visual fromSprite, int to, float wide, float duration, int pierceParams, int type, Callback callback){
		MagicBreath breath = ((MagicBreath)group.recycle( MagicBreath.class ));
		if (Actor.findChar(to) != null){
			breath.reset(fromSprite.center(), Actor.findChar(to).sprite.center(), wide, duration, pierceParams, type, callback);
		} else {
			breath.reset(fromSprite.center(), DungeonTilemap.raisedTileCenterToWorld( to ), wide, duration, pierceParams, type, callback);
		}
	}
}
