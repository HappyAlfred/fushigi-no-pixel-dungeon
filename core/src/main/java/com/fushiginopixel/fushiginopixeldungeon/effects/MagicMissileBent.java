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
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.CorrosionParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.FlameParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.LeafParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.RainbowParticle;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.ShadowParticle;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Visual;
import com.watabou.noosa.particles.Emitter;
import com.watabou.noosa.particles.PixelParticle;
import com.watabou.utils.Callback;
import com.watabou.utils.ColorMath;
import com.watabou.utils.Point;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MagicMissileBent extends Group {

	private static final double A = 180 / Math.PI;

	private float time = 0f;
	private int hit = 0;

	private Callback callback;

	private float interval = 0.05f;
	private ArrayList<PointF> nodes;
	private int type;
	private int quantity;
	private int count;

	public void reset(PointF from, ArrayList<PointF> nodes, int quantity, int type, Callback callback){

		reset(nodes, quantity, type, callback);
		nodes.set(0, from);
	}

	public void reset(ArrayList<PointF> nodes, int quantity, int type, Callback callback){

		this.callback = callback;

		this.nodes = (ArrayList<PointF>) nodes.clone();
		this.count = this.quantity = quantity;
		this.type = type;
	}

	protected void emit(final int ct) {

		if(nodes.size() < 2) {
			hit++;
			if(ct == 0 && callback != null){
				callback.call();
			}
			return;
		}
		final ArrayList<PointF> cNodes = (ArrayList<PointF>) nodes.clone();
		final PointF currentNode = cNodes.get(0);
		final PointF nextNode = cNodes.get(1);
		cNodes.remove(currentNode);

		((MagicMissile) parent.recycle(MagicMissile.class)).
				reset(type, currentNode, nextNode, new Callback() {
							@Override
							public void call() {
								if(cNodes.size() < 2){
									hit++;
									if(ct == 0 && callback != null){
										callback.call();
									}
								}else{
									((MagicMissileBent) parent.recycle(MagicMissileBent.class)).
											reset(cNodes, 1, type, ct == 0 ? callback : null
											);
								}
							}
						}
				);
	}

	@Override
	public void update() {
		time += Game.elapsed;
		while (time > interval && count > 0) {
			time -= interval;
			emit(quantity - count);
			count --;
		}
		if(hit >= quantity){
			killAndErase();
		}
	}

	//convenience method for the common case of a bolt going from a character to a tile or enemy
	public static void boltInLevel(Group group, Visual fromSprite, ArrayList<PointF> nodes, int quantity, int type, Callback callback){
		MagicMissileBent breath = ((MagicMissileBent)group.recycle( MagicMissileBent.class ));
		breath.reset(fromSprite.center(), nodes, quantity, type, callback);
	}

	public static void boltInLevel(Group group, ArrayList<PointF> nodes, int quantity, int type, Callback callback){
		MagicMissileBent breath = ((MagicMissileBent)group.recycle( MagicMissileBent.class ));
		breath.reset(nodes, quantity, type, callback);
	}
}
