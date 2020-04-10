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

import com.fushiginopixel.fushiginopixeldungeon.Assets;
import com.fushiginopixel.fushiginopixeldungeon.Badges;
import com.fushiginopixel.fushiginopixeldungeon.Dungeon;
import com.fushiginopixel.fushiginopixeldungeon.actors.Char;
import com.fushiginopixel.fushiginopixeldungeon.actors.EffectType;
import com.fushiginopixel.fushiginopixeldungeon.actors.buffs.Buff;
import com.fushiginopixel.fushiginopixeldungeon.items.BrokenSeal;
import com.fushiginopixel.fushiginopixeldungeon.items.EquipableItem;
import com.fushiginopixel.fushiginopixeldungeon.items.Item;
import com.fushiginopixel.fushiginopixeldungeon.items.armor.Armor;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.DarkGold;
import com.fushiginopixel.fushiginopixeldungeon.items.quest.Pickaxe;
import com.fushiginopixel.fushiginopixeldungeon.items.rings.Ring;
import com.fushiginopixel.fushiginopixeldungeon.items.scrolls.ScrollOfSkyBless;
import com.fushiginopixel.fushiginopixeldungeon.items.wands.Wand;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.Weapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.melee.MeleeWeapon;
import com.fushiginopixel.fushiginopixeldungeon.items.weapon.missiles.MissileWeapon;
import com.fushiginopixel.fushiginopixeldungeon.journal.Notes;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.Room;
import com.fushiginopixel.fushiginopixeldungeon.levels.rooms.standard.BlacksmithRoom;
import com.fushiginopixel.fushiginopixeldungeon.messages.Messages;
import com.fushiginopixel.fushiginopixeldungeon.scenes.GameScene;
import com.fushiginopixel.fushiginopixeldungeon.sprites.BlacksmithSprite;
import com.fushiginopixel.fushiginopixeldungeon.utils.GLog;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndBlacksmith;
import com.fushiginopixel.fushiginopixeldungeon.windows.WndQuest;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Blacksmith extends NPC {
	
	{
		spriteClass = BlacksmithSprite.class;

		properties.add(Property.IMMOVABLE);
	}
	
	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}
	
	@Override
	public boolean interact() {
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		
		if (!Quest.given) {
			
			GameScene.show( new WndQuest( this,
				Quest.alternative && Dungeon.depth < 24 ? Messages.get(this, "blood_1") : Messages.get(this, "gold_1") ) {
				
				@Override
				public void onBackPressed() {
					super.onBackPressed();
					
					Quest.given = true;
					Quest.completed = false;
					
					Pickaxe pick = new Pickaxe();
					pick.identify();
					if (pick.doPickUp( Dungeon.hero )) {
						GLog.i( Messages.get(Dungeon.hero, "you_now_have", pick.name() ));
					} else {
						Dungeon.level.drop( pick, Dungeon.hero.pos ).sprite.drop();
					}
				}
			} );
			
			Notes.add( Notes.Landmark.TROLL );
			
		} else if (!Quest.completed) {
			if (Quest.alternative) {
				
				Pickaxe pick = Dungeon.hero.belongings.getItem( Pickaxe.class );
				if (pick == null) {
					tell( Messages.get(this, "lost_pick") );
				} else if (!pick.bloodStained) {
					tell( Messages.get(this, "blood_2") );
				} else {
					if (pick.isEquipped( Dungeon.hero )) {
						pick.doUnequip( Dungeon.hero, false );
					}
					pick.detach( Dungeon.hero.belongings.backpack );
					tell( Messages.get(this, "completed") );
					
					Quest.completed = true;
					Quest.reforged = false;
				}
				
			} else {
				
				Pickaxe pick = Dungeon.hero.belongings.getItem( Pickaxe.class );
				DarkGold gold = Dungeon.hero.belongings.getItem( DarkGold.class );
				if (pick == null) {
					tell( Messages.get(this, "lost_pick") );
				} else if (gold == null || gold.quantity() < 8) {
					tell( Messages.get(this, "gold_2") );
				} else {
					if (pick.isEquipped( Dungeon.hero )) {
						if(!pick.doUnequip( Dungeon.hero, false ))
							return false;
					}
					pick.detach( Dungeon.hero.belongings.backpack );
					gold.detachAll( Dungeon.hero.belongings.backpack );
					tell( Messages.get(this, "completed") );
					
					Quest.completed = true;
					Quest.reforged = false;
				}
				
			}
		} else if (!Quest.reforged) {
			
			GameScene.show( new WndBlacksmith( this, Dungeon.hero ) );
			
		} else {
			
			tell( Messages.get(this, "get_lost") );
			
		}

		return false;
	}
	
	private void tell( String text ) {
		GameScene.show( new WndQuest( this, text ) );
	}

	public static boolean sameType(Item item1 ,Item item2){
		if(item1 instanceof MeleeWeapon && item2 instanceof MeleeWeapon)
			return true;
		else if(item1 instanceof MissileWeapon && item2 instanceof Weapon)
			return true;
		else if(item1 instanceof Ring && item2 instanceof Ring)
			return true;
		else if(item1 instanceof Wand && item2 instanceof Wand)
			return true;
		else if(item1 instanceof Armor && item2 instanceof Armor)
			return true;
		else return false;
	}

	public static void reforge(Item item1 ,Item item2){
		if(item1 instanceof MeleeWeapon && item2 instanceof MeleeWeapon) {
			if(item2.level() == 0 && item1.isUpgradable())
				item1.level(item1.level()+1);
			item1.fusion(item2);
		}
		else if(item1 instanceof MissileWeapon && item2 instanceof Weapon){
			if(item2.level() == 0 && item1.isUpgradable())
				item1.level(item1.level()+1);
			item1.fusion(item2);
		}
		else if(item1 instanceof Ring && item2 instanceof Ring){
			if(item2.level() == 0 && item1.isUpgradable())
				item1.level(item1.level()+1);
			item1.fusion(item2);
		}
		else if(item1 instanceof Wand && item2 instanceof Wand){

			if(item2.level() == 0)
				item1.level(item1.level()+1);
			item1.fusion(item2);
		}
		else if(item1 instanceof Armor && item2 instanceof Armor){

			if(item2.level() == 0)
				item1.level(item1.level()+1);
			item1.fusion(item2);
		}
	}

	public static String verify( Item item1, Item item2 ) {
		
		if (item1 == item2) {
			return Messages.get(Blacksmith.class, "same_item");
		}

		if (!sameType(item1,item2)) {
			return Messages.get(Blacksmith.class, "diff_type");
		}
		
		if (!item1.isIdentified() || !item2.isIdentified()) {
			return Messages.get(Blacksmith.class, "un_ided");
		}
		
		if (item1.cursed || item2.cursed) {
			return Messages.get(Blacksmith.class, "cursed");
		}
		
		if (item1.level() < 0 || item2.level() < 0) {
			return Messages.get(Blacksmith.class, "degraded");
		}
		
		if (!item1.isUpgradable()) {
			return Messages.get(Blacksmith.class, "cant_reforge");
		}
		
		return null;
	}
	
	public static void upgrade( Item item1, Item item2 ) {
		
		Item first, second;
		/*if (item2.level() > item1.level()) {
			first = item2;
			second = item1;
		} else {*/
			first = item1;
			second = item2;
		//}

		Sample.INSTANCE.play( Assets.SND_EVOKE );
		ScrollOfSkyBless.upgrade( Dungeon.hero );
		Item.evoke( Dungeon.hero );
		
		if (first.isEquipped( Dungeon.hero )) {
			((EquipableItem)first).doUnequip( Dungeon.hero, true );
		}
		reforge(first,second);
		//first.level(first.level()+1); //prevents on-upgrade effects like enchant/glyph removal
		Dungeon.hero.spendAndNext( 2f );
		Badges.validateItemLevelAquired( first );
		
		if (second.isEquipped( Dungeon.hero )) {
			((EquipableItem)second).doUnequip( Dungeon.hero, false );
		}
		second.detachAll( Dungeon.hero.belongings.backpack );
		
		if (second instanceof Armor){
			BrokenSeal seal = ((Armor) second).checkSeal();
			if (seal != null){
				Dungeon.level.drop( seal, Dungeon.hero.pos );
			}
		}
		
		Quest.reforged = true;
		
		Notes.remove( Notes.Landmark.TROLL );
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public int damage( int dmg, Object src , EffectType type) {
		return 0;
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}

	public static class Quest {
		
		private static boolean spawned;
		
		private static boolean alternative;
		private static boolean given;
		private static boolean completed;
		private static boolean reforged;
		
		public static void reset() {
			spawned		= false;
			given		= false;
			completed	= false;
			reforged	= false;
		}
		
		private static final String NODE	= "blacksmith";
		
		private static final String SPAWNED		= "spawned";
		private static final String ALTERNATIVE	= "alternative";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String REFORGED	= "reforged";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( ALTERNATIVE, alternative );
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REFORGED, reforged );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				alternative	=  node.getBoolean( ALTERNATIVE );
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				reforged = node.getBoolean( REFORGED );
			} else {
				reset();
			}
		}
		
		public static ArrayList<Room> spawn( ArrayList<Room> rooms ) {
			if (!spawned && Dungeon.depth > 21 && Random.Int( 25 - Dungeon.depth ) == 0) {

				rooms.add(new BlacksmithRoom());
				spawned = true;
				alternative = Random.Int( 2 ) == 0;
				
				given = false;
				
			}
			return rooms;
		}
	}
}
