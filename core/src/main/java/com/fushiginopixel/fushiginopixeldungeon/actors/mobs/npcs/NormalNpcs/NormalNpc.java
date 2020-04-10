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

package com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NormalNpcs;

import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.Statistics;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.npcs.NPC;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndQuest;

import java.util.ArrayList;

public class NormalNpc extends NPC {

	{

		properties.add(Property.IMMOVABLE);
	}

	protected ArrayList<String> chat = new ArrayList<>();
	protected ArrayList<String> endChat = new ArrayList<>();

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
	protected Char chooseEnemy() {
		return null;
	}
	
	@Override
	public int damage( int dmg, Object src, EffectType type ) {
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
		if(Statistics.amuletObtained && !endChat.isEmpty()){
			WndQuest.chating(this,endChat);
		}else {
			WndQuest.chating(this, chat);
		}
		return true;
	}
}
