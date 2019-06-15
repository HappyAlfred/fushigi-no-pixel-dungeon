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
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Elemental;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Golem;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Mob;
import com.fushiginopixel.fushiginopixeldungeon.actors.mobs.Monk;
import com.fushiginopixel.fushiginopixeldungeon.items.Generator;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.DwarfToken;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.Embers;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.journal.Notes;
import com.fushiginopixel.fushiginopixeldungeon.levels.CityLevel;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.ImpSprite;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndImp;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndQuest;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Imp extends NPC {

	{
		spriteClass = ImpSprite.class;

		properties.add(Property.IMMOVABLE);
	}
	
	private boolean seenBefore = false;
	
	@Override
	protected boolean act() {
		
		if (!Quest.given && Dungeon.level.heroFOV[pos]) {
			if (!seenBefore) {
				yell( Messages.get(this, "hey", Dungeon.hero.givenName() ) );
			}
			seenBefore = true;
		} else {
			seenBefore = false;
		}
		
		throwItem();
		
		return super.act();
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public int damage( int dmg, Object src ,EffectType type) {
		return 1;
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
		if (Quest.given) {
			
			DwarfToken tokens = Dungeon.hero.belongings.getItem( DwarfToken.class );
			Embers embers = Dungeon.hero.belongings.getItem( Embers.class );
			if (tokens != null && ((Quest.type == 2 && tokens.quantity() >= 4) || (Quest.type == 3 && tokens.quantity() >= 4))) {
				GameScene.show( new WndImp( this, tokens ) );
			}
			else if(embers != null && Quest.type == 1 && embers.quantity() >= 4){
				GameScene.show( new WndImp( this, embers ) );
			}
			else {
				switch (Quest.type) {
					case 1:
					default:
						tell(Messages.get(this, "elemental_2", Dungeon.hero.givenName()));
						break;
					case 2:
						tell(Messages.get(this, "monks_2", Dungeon.hero.givenName()));
						break;
					case 3:
						tell(Messages.get(this, "golems_2", Dungeon.hero.givenName()));
						break;
				}
			}
			
		} else {
			switch (Quest.type) {
				case 1:
				default:
					tell(Messages.get(this, "elemental_1"));
					break;
				case 2:
					tell(Messages.get(this, "monks_1"));
					break;
				case 3:
					tell(Messages.get(this, "golems_1"));
					break;
			}
			Quest.given = true;
			Quest.completed = false;
			
			Notes.add( Notes.Landmark.IMP );
		}

		return false;
	}
	
	private void tell( String text ) {
		GameScene.show(
			new WndQuest( this, text ));
	}
	
	public void flee() {
		
		yell( Messages.get(this, "cya", Dungeon.hero.givenName()) );
		
		destroy();
		sprite.die();
	}

	public static class Quest {
		
		private static int type;
		
		private static boolean spawned;
		private static boolean given;
		private static boolean completed;

		public static Ring reward;
		
		public static void reset() {
			spawned = false;

			reward = null;
		}
		
		private static final String NODE			= "demon";
		
		private static final String TYPE			= "type";
		private static final String SPAWNED		= "spawned";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String REWARD		= "reward";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( TYPE, type );
				
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REWARD, reward );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				type	= node.getInt(TYPE);
				
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				reward = (Ring)node.get( REWARD );
			} else {
				reset();
			}
		}
		
		public static void spawn( CityLevel level ) {
			if (!spawned && Dungeon.depth > 31 && Random.Int( 35 - Dungeon.depth ) == 0) {
				
				Imp npc = new Imp();
				do {
					npc.pos = level.randomRespawnCell();
				} while (
						npc.pos == -1 ||
						level.heaps.get( npc.pos ) != null ||
						level.traps.get( npc.pos) != null ||
						level.findMob( npc.pos ) != null ||
						//The imp doesn't move, so he cannot obstruct a passageway
						!(level.passable[npc.pos + PathFinder.CIRCLE4[0]] && level.passable[npc.pos + PathFinder.CIRCLE4[2]]) ||
						!(level.passable[npc.pos + PathFinder.CIRCLE4[1]] && level.passable[npc.pos + PathFinder.CIRCLE4[3]]));
				level.mobs.add( npc );
				
				spawned = true;
				type = Dungeon.depth - 31;
				
				given = false;
				
				do {
					reward = (Ring)Generator.random( Generator.Category.RING );
				} while (reward.cursed);
				reward.upgrade( 2 );
				reward.cursed = true;
			}
		}
		
		public static void process( Mob mob ) {
			if (spawned && given && !completed) {
				if ((type == 2 && mob instanceof Monk) ||
						(type == 3 && mob instanceof  Golem)) {
					
					Dungeon.level.drop( new DwarfToken(), mob.pos ).sprite.drop();
				}
				else if(type == 1 && mob instanceof Elemental){
					Dungeon.level.drop( new Embers(), mob.pos ).sprite.drop();
				}
			}
		}
		
		public static void complete() {
			reward = null;
			completed = true;
			
			Notes.remove( Notes.Landmark.IMP );
		}
		
		public static boolean isCompleted() {
			return completed;
		}
	}
}
