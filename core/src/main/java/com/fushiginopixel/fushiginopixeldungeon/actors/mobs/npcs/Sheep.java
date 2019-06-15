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

package com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.sprites.CharSprite;
import com.fushiginopixel.fushiginopixeldungeon.sprites.SheepSprite;
import com.watabou.utils.Random;

public class Sheep extends NPC {

	private static final String[] LINE_KEYS = {"Baa!", "Baa?", "Baa.", "Baa..."};

	{
		spriteClass = SheepSprite.class;
	}

	public float lifespan;

	private boolean initialized = false;

	@Override
	protected boolean act() {
		if (initialized) {
			HP = 0;

			destroy();
			sprite.die();

		} else {
			initialized = true;
			spend( lifespan + Random.Float(2) );
		}
		return true;
	}

	@Override
	public int damage( int dmg, Object src ,EffectType type) {
		return 0;
	}

	@Override
	public void add( Buff buff ) {
	}

	@Override
	public boolean interact() {
		sprite.showStatus( CharSprite.NEUTRAL, Messages.get(this, Random.element( LINE_KEYS )) );
		Dungeon.hero.spendAndNext(1f);
		return false;
	}
}