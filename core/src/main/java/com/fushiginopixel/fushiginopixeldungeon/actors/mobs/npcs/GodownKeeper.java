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
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.Gold;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.CeremonialCandle;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.CorpseDust;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.Embers;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.journal.Notes;
import com.fushiginopixel.fushiginopixeldungeon.levels.Level;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.Room;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.special.MassGraveRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.special.RotGardenRoom;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.standard.RitualSiteRoom;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.plants.Rotberry;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.WandmakerSprite;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBag;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndGodownKeeper;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndOptions;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndQuest;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndWandmaker;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class GodownKeeper extends NPC {

	{
		spriteClass = WandmakerSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	public static int cost = 200;
	
	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public int damage( int dmg, Object src ,EffectType type) {
		return 0;
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public boolean interact() {
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		GameScene.show( new WndGodownKeeper( this ) );

		return true;
	}
}
