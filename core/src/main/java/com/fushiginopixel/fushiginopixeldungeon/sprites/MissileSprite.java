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

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.DM450;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.DM5000;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.SkeletonArcher;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.bombs.CannonBall;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.Crossbow;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Bolas;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Boomerang;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.FishingSpear;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Javelin;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Shuriken;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.ThrowingSpear;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.Trident;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.darts.Dart;
import com.fushiginopixel.fushiginopixeldungeon.levels.traps.Trap;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.watabou.noosa.Visual;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

import java.util.HashMap;

public class MissileSprite extends ItemSprite implements Tweener.Listener {

	private static final float SPEED	= 240f;
	
	private Callback callback;
	
	public void reset( int from, int to, Item item, Object src, Callback listener ) {
		reset( DungeonTilemap.tileToWorld( from ), DungeonTilemap.tileToWorld( to ), item, src, listener);
	}

	public void reset( Visual from, Visual to, Item item, Object src, Callback listener ) {
		reset(from.center(this), to.center(this), item, src, listener );
	}

	public void reset( Visual from, int to, Item item, Object src, Callback listener ) {
		reset(from.center(this), DungeonTilemap.tileToWorld( to ), item, src, listener );
	}
	
	public void reset( int from, Visual to, Item item, Object src, Callback listener ) {
		reset(DungeonTilemap.tileToWorld( from ), to.center(this), item, src, listener );
	}

	public void reset( PointF from, PointF to, Item item, Object src, Callback listener) {
		revive();

		if (item == null)   view(15, null);
		else                view(item.image(), item.glowing());

		setup( from,
				to,
				item,
				src,
				listener );
	}
	
	private static final int DEFAULT_ANGULAR_SPEED = 720;
	
	private static final HashMap<Class<?extends Item>, Integer> ANGULAR_SPEEDS = new HashMap<>();
	static {
		ANGULAR_SPEEDS.put(Dart.class,          0);
		ANGULAR_SPEEDS.put(ThrowingKnife.class, 0);
		ANGULAR_SPEEDS.put(FishingSpear.class,  0);
		ANGULAR_SPEEDS.put(ThrowingSpear.class, 0);
		ANGULAR_SPEEDS.put(Javelin.class,       0);
		ANGULAR_SPEEDS.put(Trident.class,       0);
		
		//720 is default
		
		ANGULAR_SPEEDS.put(Boomerang.class,     1440);
		ANGULAR_SPEEDS.put(Bolas.class,         1440);
		
		ANGULAR_SPEEDS.put(Shuriken.class,      2160);
	}

	//TODO it might be nice to have a source and destination angle, to improve thrown weapon visuals
	private void setup( PointF from, PointF to, Item item, Object src, Callback listener ){

		originToCenter();

		this.callback = listener;

		point( from );

		PointF d = PointF.diff( to, from );
		speed.set(d).normalize().scale(SPEED);
		
		angularSpeed = DEFAULT_ANGULAR_SPEED;
		for (Class<?extends Item> cls : ANGULAR_SPEEDS.keySet()){
			if (item != null && cls.isAssignableFrom(item.getClass())){
				angularSpeed = ANGULAR_SPEEDS.get(cls);
				break;
			}
		}

		if(src != null && src instanceof Trap)
			angularSpeed = 0;
		
		angle = 135 - (float)(Math.atan2( d.x, d.y ) / 3.1415926 * 180);
		
		if (d.x >= 0){
			flipHorizontal = false;
			updateFrame();
			
		} else {
			angularSpeed = -angularSpeed;
			angle += 90;
			flipHorizontal = true;
			updateFrame();
		}
		
		float speed = SPEED;

		//int formPos = (int)(from.x / 16) + (int)(from.y / 16) * Dungeon.level.width();
		//Char fromChar = Actor.findChar(formPos);

		//Evan's codes are shit!!!
		if (
				(item instanceof Dart && src != null && src == Dungeon.hero && Dungeon.hero.belongings.weapon instanceof Crossbow) ||
				(item instanceof ThrowingSpear && src != null && src instanceof SkeletonArcher)||
				(item instanceof CannonBall && src != null && (src instanceof DM450 || src instanceof DM5000))
				){
			speed *= 2f;
		}
		PosTweener tweener = new PosTweener( this, to, d.length() / speed );
		tweener.listener = this;
		parent.add( tweener );
	}

	@Override
	public void onComplete( Tweener tweener ) {
		kill();
		if (callback != null) {
			callback.call();
		}
	}
}
