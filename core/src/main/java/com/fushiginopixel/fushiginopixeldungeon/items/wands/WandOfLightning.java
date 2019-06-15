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

package com.fushiginopixel.fushiginopixeldungeon.items.wands;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Actor;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.hero.Hero;
import com.fushiginopixel.fushiginopixeldungeon.effects.CellEmitter;
import com.fushiginopixel.fushiginopixeldungeon.effects.Lightning;
import com.fushiginopixel.fushiginopixeldungeon.effects.particles.SparkParticle;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.enchantments.Shocking;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MagesStaff;
import com.fushiginopixel.fushiginopixeldungeon.mechanics.Ballistica;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ItemSpriteSheet;
import com.fushiginopixel.fushiginopixeldungeon.tiles.DungeonTilemap;
import com.fushiginopixel.fushiginopixeldungeon.utils.BArray;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.watabou.noosa.Camera;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class WandOfLightning extends DamageWand {

	{
		initials = 7;
	}
	
	private ArrayList<Char> affected = new ArrayList<>();

	ArrayList<Lightning.Arc> arcs = new ArrayList<>();

	public int min(int lvl){
		return 1;
	}

	public int max(int lvl){
		return 40+7*lvl;
	}
	
	@Override
	protected void onZap( Ballistica bolt ) {

		//lightning deals less damage per-target, the more targets that are hit.
		float multipler = 0.4f + (0.6f/affected.size());
		//if the main target is in water, all affected take full damage
		if (Dungeon.level.water[bolt.collisionPos]) multipler = 1f;

		int min = 5 + level();
		int max = 10 + 6*level();

		for (Char ch : affected){
			processSoulMark(ch, chargesPerCast());
			float multipler1 = (ch instanceof Hero) ? multipler / 4 :multipler;
			ch.damage(Math.round(damageRoll() * multipler1), this, new EffectType(EffectType.MAGICAL_BOLT,EffectType.ELETRIC));

			if (ch == Dungeon.hero) Camera.main.shake( 2, 0.3f );
			ch.sprite.centerEmitter().burst( SparkParticle.FACTORY, 3 );
			ch.sprite.flash();
		}

		if (!Dungeon.hero.isAlive()) {
			Dungeon.fail( getClass() );
			GLog.n(Messages.get(this, "ondeath"));
		}
	}

	@Override
	public void onHit(MagesStaff staff, Char attacker, Char defender, int damage, EffectType type) {
		//acts like shocking enchantment
		new Shocking().proc(staff, attacker, defender, damage, type);
	}

	private void arc( Char ch ) {
		
		affected.add( ch );

		int dist;
		if (Dungeon.level.water[ch.pos] && !ch.flying)
			dist = 2;
		else
			dist = 1;

			PathFinder.buildDistanceMap( ch.pos, BArray.not( Dungeon.level.solid, null ), dist );
			for (int i = 0; i < PathFinder.distance.length; i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE){
					Char n = Actor.findChar( i );
					if (n == Dungeon.hero && PathFinder.distance[i] > 1)
						//the hero is only zapped if they are adjacent
						continue;
					else if (n != null && !affected.contains( n )) {
						arcs.add(new Lightning.Arc(ch.sprite.center(), n.sprite.center()));
						arc(n);
					}
				}
		}
	}
	
	@Override
	protected void fx( Ballistica bolt, Callback callback ) {

		affected.clear();
		arcs.clear();

		int cell = bolt.collisionPos;

		Char ch = Actor.findChar( cell );
		if (ch != null) {
			arcs.add( new Lightning.Arc(curUser.sprite.center(), ch.sprite.center()));
			arc(ch);
		} else {
			arcs.add( new Lightning.Arc(curUser.sprite.center(), DungeonTilemap.raisedTileCenterToWorld(bolt.collisionPos)));
			CellEmitter.center( cell ).burst( SparkParticle.FACTORY, 3 );
		}

		//don't want to wait for the effect before processing damage.
		curUser.sprite.parent.addToFront( new Lightning( arcs, null ) );
		callback.call();
	}

	@Override
	public void staffFx(MagesStaff.StaffParticle particle) {
		particle.color(0xFFFFFF);
		particle.am = 0.6f;
		particle.setLifespan(0.6f);
		particle.acc.set(0, +10);
		particle.speed.polar(-Random.Float(3.1415926f), 6f);
		particle.setSize(0f, 1.5f);
		particle.sizeJitter = 1f;
		particle.shuffleXY(1f);
		float dst = Random.Float(1f);
		particle.x -= dst;
		particle.y += dst;
	}
	
}
